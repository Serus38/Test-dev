package com.testdev.test_dev.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testdev.test_dev.model.MaritimeShipment;
import com.testdev.test_dev.repository.MaritimeShipmentRepository;

@Service
public class MaritimeShipmentServiceImp implements MaritimeShipmentService {

    private static final String GUIDE_PREFIX = "MAR";
    private static final int GUIDE_NUMBER_LENGTH = 7;
    private static final BigDecimal DISCOUNT_STEP_PERCENT = new BigDecimal("5");
    private static final BigDecimal PRODUCTS_PER_DISCOUNT_STEP = new BigDecimal("10");
    private static final BigDecimal MAX_DISCOUNT_PERCENT = new BigDecimal("50");
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final MaritimeShipmentRepository maritimeShipmentRepository;

    public MaritimeShipmentServiceImp(MaritimeShipmentRepository maritimeShipmentRepository) {
        this.maritimeShipmentRepository = maritimeShipmentRepository;
    }

    @Override
    public List<MaritimeShipment> getAllMaritimeShipments() {
        return maritimeShipmentRepository.findAll();
    }

    @Override
    @Transactional
    public MaritimeShipment save(MaritimeShipment maritimeShipment) {
        validateOriginAndDestination(maritimeShipment);
        applyAutomaticDiscountAndTotal(maritimeShipment);
        maritimeShipment.setGuideNumber(generateTemporaryGuideNumber());
        MaritimeShipment savedShipment = maritimeShipmentRepository.save(maritimeShipment);
        savedShipment.setGuideNumber(formatGuideNumber(savedShipment.getId()));
        return maritimeShipmentRepository.save(savedShipment);
    }

    @Override
    public void delete(Long id) {
        maritimeShipmentRepository.deleteById(id);
    }

    // Al actualizar, si el número de guía no se proporciona, mantenemos el existente para evitar sobrescribirlo con un valor nulo o vacío
    @Override
    @Transactional
    public MaritimeShipment update(MaritimeShipment maritimeShipment) {
        validateOriginAndDestination(maritimeShipment);
        applyAutomaticDiscountAndTotal(maritimeShipment);
        MaritimeShipment existingShipment = maritimeShipmentRepository.findById(maritimeShipment.getId()).orElse(null);
        if (existingShipment != null && (maritimeShipment.getGuideNumber() == null || maritimeShipment.getGuideNumber().isBlank())) {
            maritimeShipment.setGuideNumber(existingShipment.getGuideNumber());
        }
        return maritimeShipmentRepository.save(maritimeShipment);
    }

    @Override
    public MaritimeShipment getMaritimeShipmentById(long id) {
        return maritimeShipmentRepository.findById(id).orElse(null);
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

    // Método para aplicar el descuento automático y calcular el costo total basado en la cantidad y el costo de envío
    private void applyAutomaticDiscountAndTotal(MaritimeShipment maritimeShipment) {
        BigDecimal quantity = maritimeShipment.getQuantity();
        BigDecimal shippingCost = maritimeShipment.getShippingCost();

        int discountSteps = quantity.divideToIntegralValue(PRODUCTS_PER_DISCOUNT_STEP).intValue();
        BigDecimal calculatedDiscountRate = DISCOUNT_STEP_PERCENT.multiply(BigDecimal.valueOf(discountSteps));
        if (calculatedDiscountRate.compareTo(MAX_DISCOUNT_PERCENT) > 0) {
            calculatedDiscountRate = MAX_DISCOUNT_PERCENT;
        }

        BigDecimal discountAmount = shippingCost
                .multiply(calculatedDiscountRate)
                .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
        BigDecimal totalCost = shippingCost.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

        maritimeShipment.setDiscountRate(calculatedDiscountRate.setScale(2, RoundingMode.HALF_UP));
        maritimeShipment.setTotalCost(totalCost);
    }

    // Validación para asegurar que el envío marítimo tenga un origen y un destino, y que no sean el mismo puerto
    private void validateOriginAndDestination(MaritimeShipment maritimeShipment) {
        if (maritimeShipment.getOriginPort() == null || maritimeShipment.getDestinationPort() == null) {
            throw new IllegalArgumentException("El envio maritimo debe tener un origen y un destino");
        }

        Long originId = maritimeShipment.getOriginPort().getId();
        Long destinationId = maritimeShipment.getDestinationPort().getId();

        if (originId != null && originId.equals(destinationId)) {
            throw new IllegalArgumentException("El origen y el destino no pueden ser el mismo puerto");
        }
    }
}
