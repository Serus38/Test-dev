package com.testdev.test_dev.service;

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
    
}
