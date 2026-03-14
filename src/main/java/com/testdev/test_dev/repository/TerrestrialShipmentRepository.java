package com.testdev.test_dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.testdev.test_dev.model.TerrestrialShipment;

@Repository
public interface TerrestrialShipmentRepository extends JpaRepository<TerrestrialShipment, Long> {
    
}
