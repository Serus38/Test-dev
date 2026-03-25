package com.testdev.test_dev.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final TerrestrialShipmentRepository terrestrialShipmentRepository;

    public TerrestrialShipmentServiceImp(TerrestrialShipmentRepository terrestrialShipmentRepository) {
        this.terrestrialShipmentRepository = terrestrialShipmentRepository;
    }

    @Override
    public List<TerrestrialShipment> getAllTerrestrialShipments() {
        return terrestrialShipmentRepository.findAll();
    }


    // Guardamos el envío con un número de guía temporal, luego actualizamos con el número de guía definitivo basado en el ID generado
    @Override
    @Transactional
    public TerrestrialShipment save(TerrestrialShipment terrestrialShipment) {
        validateSingleOriginAndDestination(terrestrialShipment);
        applyAutomaticDiscountAndTotal(terrestrialShipment);
        terrestrialShipment.setGuideNumber(generateTemporaryGuideNumber());
        TerrestrialShipment savedShipment = terrestrialShipmentRepository.save(terrestrialShipment);
        savedShipment.setGuideNumber(formatGuideNumber(savedShipment.getId()));
        return terrestrialShipmentRepository.save(savedShipment);
    }

    @Override
    public void delete(Long id) {
        terrestrialShipmentRepository.deleteById(id);
    }


    // Al actualizar, si el número de guía no se proporciona, mantenemos el existente para evitar sobrescribirlo con un valor nulo o vacío
    @Override
    @Transactional
    public TerrestrialShipment update(TerrestrialShipment terrestrialShipment) {
        validateSingleOriginAndDestination(terrestrialShipment);
        applyAutomaticDiscountAndTotal(terrestrialShipment);
        TerrestrialShipment existingShipment = terrestrialShipmentRepository.findById(terrestrialShipment.getId()).orElse(null);
        if (existingShipment != null && (terrestrialShipment.getGuideNumber() == null || terrestrialShipment.getGuideNumber().isBlank())) {
            terrestrialShipment.setGuideNumber(existingShipment.getGuideNumber());
        }
        return terrestrialShipmentRepository.save(terrestrialShipment);
    }

    @Override
    public TerrestrialShipment getTerrestrialShipmentById(long id) {
        return terrestrialShipmentRepository.findById(id).orElse(null);
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
