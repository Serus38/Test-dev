package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.TerrestrialShipment;

@Service
public interface TerrestrialShipmentService {

    List<TerrestrialShipment> getAllTerrestrialShipments();
    void save(TerrestrialShipment terrestrialShipment);
    void delete(Long id);
    void update(TerrestrialShipment terrestrialShipment);
    TerrestrialShipment getTerrestrialShipmentById(long id);
    
}
