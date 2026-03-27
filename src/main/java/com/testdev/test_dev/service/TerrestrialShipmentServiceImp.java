package com.testdev.test_dev.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testdev.test_dev.model.ShipmentStatus;
import com.testdev.test_dev.model.TerrestrialShipment;
import com.testdev.test_dev.repository.TerrestrialShipmentRepository;

@Service
public class TerrestrialShipmentServiceImp implements TerrestrialShipmentService {


    // Constantes para generación de número de guía
    private static final String GUIDE_PREFIX = "TER";
    private static final int GUIDE_NUMBER_LENGTH = 7;
    private static final BigDecimal DISCOUNT_STEP_PERCENT = new BigDecimal("5");
    private static final BigDecimal PRODUCTS_PER_DISCOUNT_STEP = new BigDecimal("10");
    private static final BigDecimal MAX_DISCOUNT_PERCENT = new BigDecimal("50");
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final DateTimeFormatter[] SUPPORTED_DATE_FORMATS = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
    };

    private final TerrestrialShipmentRepository terrestrialShipmentRepository;

    /**
     * Constructor con inyeccion del repositorio de envios terrestres.
     */
    public TerrestrialShipmentServiceImp(TerrestrialShipmentRepository terrestrialShipmentRepository) {
        this.terrestrialShipmentRepository = terrestrialShipmentRepository;
    }


    // Al obtener todos los envíos, también verificamos si alguno debe actualizar su estado a retrasado antes de devolver la lista
    /**
     * Lista envios y sincroniza estado automaticamente segun fecha de entrega.
     */
    @Override
    public List<TerrestrialShipment> getAllTerrestrialShipments() {
        List<TerrestrialShipment> shipments = terrestrialShipmentRepository.findAll();
        boolean hasChanges = false;
        for (TerrestrialShipment shipment : shipments) {
            hasChanges |= applyAutomaticStatusIfNecessary(shipment);
        }

        if (hasChanges) {
            terrestrialShipmentRepository.saveAll(shipments);
        }

        return shipments;
    }


    // Guardamos el envío con un número de guía temporal, luego actualizamos con el número de guía definitivo basado en el ID generado
    /**
     * Guarda un envio aplicando validaciones, descuento y numero de guia final.
     */
    @Override
    @Transactional
    public TerrestrialShipment save(TerrestrialShipment terrestrialShipment) {
        validateSingleOriginAndDestination(terrestrialShipment);
        applyAutomaticDiscountAndTotal(terrestrialShipment);
        applyAutomaticStatusIfNecessary(terrestrialShipment);
        terrestrialShipment.setGuideNumber(generateTemporaryGuideNumber());
        TerrestrialShipment savedShipment = terrestrialShipmentRepository.save(terrestrialShipment);
        savedShipment.setGuideNumber(formatGuideNumber(savedShipment.getId()));
        return terrestrialShipmentRepository.save(savedShipment);
    }

    /**
     * Elimina un envio terrestre por id.
     */
    @Override
    public void delete(Long id) {
        terrestrialShipmentRepository.deleteById(id);
    }


    // Al actualizar, si el número de guía no se proporciona, mantenemos el existente para evitar sobrescribirlo con un valor nulo o vacío
    /**
     * Actualiza envio terrestre preservando guia existente si no se envia una nueva.
     */
    @Override
    @Transactional
    public TerrestrialShipment update(TerrestrialShipment terrestrialShipment) {
        validateSingleOriginAndDestination(terrestrialShipment);
        applyAutomaticDiscountAndTotal(terrestrialShipment);
        applyAutomaticStatusIfNecessary(terrestrialShipment);
        TerrestrialShipment existingShipment = terrestrialShipmentRepository.findById(terrestrialShipment.getId()).orElse(null);
        if (existingShipment != null && (terrestrialShipment.getGuideNumber() == null || terrestrialShipment.getGuideNumber().isBlank())) {
            terrestrialShipment.setGuideNumber(existingShipment.getGuideNumber());
        }
        return terrestrialShipmentRepository.save(terrestrialShipment);
    }


    // Al obtener por ID, también verificamos si el estado debe actualizarse a retrasado antes de devolver el resultado
    /**
     * Obtiene un envio por id y ajusta su estado si corresponde.
     */
    @Override
    public TerrestrialShipment getTerrestrialShipmentById(long id) {
        TerrestrialShipment shipment = terrestrialShipmentRepository.findById(id).orElse(null);
        if (shipment == null) {
            return null;
        }

        if (applyAutomaticStatusIfNecessary(shipment)) {
            shipment = terrestrialShipmentRepository.save(shipment);
        }

        return shipment;
    }

    /**
     * Reglas de cambio automatico de estado por fecha de entrega.
     */
    private boolean applyAutomaticStatusIfNecessary(TerrestrialShipment shipment) {
        ShipmentStatus currentStatus = shipment.getStatus();
        if (currentStatus == ShipmentStatus.DELIVERED || currentStatus == ShipmentStatus.CANCELLED) {
            return false;
        }

        LocalDate deliveryDate = parseDate(shipment.getDeliveryDate());
        if (deliveryDate == null) {
            return false;
        }

        LocalDate now = LocalDate.now();

        if (now.isAfter(deliveryDate.plusDays(3)) && currentStatus != ShipmentStatus.CANCELLED) {
            shipment.setStatus(ShipmentStatus.CANCELLED);
            return true;
        }

        if (now.isAfter(deliveryDate.plusDays(1)) && currentStatus != ShipmentStatus.DELAYED) {
            shipment.setStatus(ShipmentStatus.DELAYED);
            return true;
        }

        return false;
    }

    /**
     * Parsea fecha con formatos soportados por la API.
     */
    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (DateTimeFormatter formatter : SUPPORTED_DATE_FORMATS) {
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next supported format.
            }
        }

        return null;
    }

    // Método para formatear el número de guía con el prefijo y ceros a la izquierda
    private String formatGuideNumber(Long shipmentId) {
        return String.format(Locale.ROOT, "%s%0" + GUIDE_NUMBER_LENGTH + "d", GUIDE_PREFIX, shipmentId);
    }

    // Método para generar un número de guía temporal único mientras se guarda el envío por primera vez
    private String generateTemporaryGuideNumber() {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 7).toUpperCase(Locale.ROOT);
        return "TMP" + suffix;
    }


    // Método para aplicar el descuento automático basado en la cantidad de productos y calcular el costo total después del descuento
    private void applyAutomaticDiscountAndTotal(TerrestrialShipment terrestrialShipment) {
        BigDecimal quantity = BigDecimal.valueOf(terrestrialShipment.getQuantity());
        BigDecimal shippingCost = terrestrialShipment.getShippingCost();

        int discountSteps = quantity.divideToIntegralValue(PRODUCTS_PER_DISCOUNT_STEP).intValue();
        BigDecimal calculatedDiscountRate = DISCOUNT_STEP_PERCENT.multiply(BigDecimal.valueOf(discountSteps));
        if (calculatedDiscountRate.compareTo(MAX_DISCOUNT_PERCENT) > 0) {
            calculatedDiscountRate = MAX_DISCOUNT_PERCENT;
        }

        BigDecimal discountAmount = shippingCost
                .multiply(calculatedDiscountRate)
                .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
        BigDecimal totalCost = shippingCost.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

        terrestrialShipment.setDiscountRate(calculatedDiscountRate.setScale(2, RoundingMode.HALF_UP));
        terrestrialShipment.setTotalCost(totalCost);
    }


    // Validación para asegurar que solo se proporcione un origen y un destino, y que no sean iguales
    private void validateSingleOriginAndDestination(TerrestrialShipment terrestrialShipment) {
        int originCount = 0;
        if (terrestrialShipment.getOriginBodega() != null) {
            originCount++;
        }
        if (terrestrialShipment.getOriginPort() != null) {
            originCount++;
        }

        int destinationCount = 0;
        if (terrestrialShipment.getDestinationBodega() != null) {
            destinationCount++;
        }
        if (terrestrialShipment.getDestinationPort() != null) {
            destinationCount++;
        }

        if (originCount != 1 || destinationCount != 1) {
            throw new IllegalArgumentException("El envio terrestre debe tener exactamente un origen y un destino");
        }

        // Validación adicional para asegurar que el origen y el destino no sean el mismo, considerando tanto bodegas como puertos
        Long originBodegaId = terrestrialShipment.getOriginBodega() != null ? terrestrialShipment.getOriginBodega().getId() : null;
        Long destinationBodegaId = terrestrialShipment.getDestinationBodega() != null ? terrestrialShipment.getDestinationBodega().getId() : null;
        if (originBodegaId != null && originBodegaId.equals(destinationBodegaId)) {
            throw new IllegalArgumentException("El origen y el destino no pueden ser la misma bodega");
        }

        Long originPortId = terrestrialShipment.getOriginPort() != null ? terrestrialShipment.getOriginPort().getId() : null;
        Long destinationPortId = terrestrialShipment.getDestinationPort() != null ? terrestrialShipment.getDestinationPort().getId() : null;
        if (originPortId != null && originPortId.equals(destinationPortId)) {
            throw new IllegalArgumentException("El origen y el destino no pueden ser el mismo puerto");
        }
    }
}
