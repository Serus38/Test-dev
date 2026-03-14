package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.TerrestrialShipment;
import com.testdev.test_dev.repository.TerrestrialShipmentRepository;

@Service
public class TerrestrialShipmentServiceImp implements TerrestrialShipmentService {

    TerrestrialShipmentRepository terrestrialShipmentRepository;

    public TerrestrialShipmentServiceImp(TerrestrialShipmentRepository terrestrialShipmentRepository) {
        this.terrestrialShipmentRepository = terrestrialShipmentRepository;
    }

    @Override
    public List<TerrestrialShipment> getAllTerrestrialShipments() {
        return terrestrialShipmentRepository.findAll();
    }

    @Override
    public void save(TerrestrialShipment terrestrialShipment) {
        terrestrialShipmentRepository.save(terrestrialShipment);
    }

    @Override
    public void delete(Long id) {
        terrestrialShipmentRepository.deleteById(id);
    }

    @Override
    public void update(TerrestrialShipment terrestrialShipment) {
        terrestrialShipmentRepository.save(terrestrialShipment);
    }

    @Override
    public TerrestrialShipment getTerrestrialShipmentById(long id) {
        return terrestrialShipmentRepository.findById(id).orElse(null);
    }
    
}
