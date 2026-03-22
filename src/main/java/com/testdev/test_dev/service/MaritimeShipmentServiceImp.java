package com.testdev.test_dev.service;

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
}
