package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.MaritimeShipment;

@Service
public interface MaritimeShipmentService {

    List<MaritimeShipment> getAllMaritimeShipments();
    void save(MaritimeShipment maritimeShipment);
    void delete(Long id);
    void update(MaritimeShipment maritimeShipment);
    MaritimeShipment getMaritimeShipmentById(long id);
    
}
