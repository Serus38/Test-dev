package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.MaritimeShipment;
import com.testdev.test_dev.repository.MaritimeShipmentRepository;

@Service
public class MaritimeShipmentServiceImp implements MaritimeShipmentService {

    MaritimeShipmentRepository maritimeShipmentRepository;

    public MaritimeShipmentServiceImp(MaritimeShipmentRepository maritimeShipmentRepository) {
        this.maritimeShipmentRepository = maritimeShipmentRepository;
    }

    @Override
    public List<MaritimeShipment> getAllMaritimeShipments() {
        return maritimeShipmentRepository.findAll();
    }

    @Override
    public void save(MaritimeShipment maritimeShipment) {
        maritimeShipmentRepository.save(maritimeShipment);
    }

    @Override
    public void delete(Long id) {
        maritimeShipmentRepository.deleteById(id);
    }

    @Override
    public void update(MaritimeShipment maritimeShipment) {
        maritimeShipmentRepository.save(maritimeShipment);
    }

    @Override
    public MaritimeShipment getMaritimeShipmentById(long id) {
        return maritimeShipmentRepository.findById(id).orElse(null);
    }
}
