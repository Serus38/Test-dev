package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.MaritimeShipment;

@Service
public interface MaritimeShipmentService {

    /**
     * Lista envios maritimos.
     */
    List<MaritimeShipment> getAllMaritimeShipments();

    /**
     * Crea un envio maritimo.
     */
    MaritimeShipment save(MaritimeShipment maritimeShipment);

    /**
     * Elimina un envio maritimo por id.
     */
    void delete(Long id);

    /**
     * Actualiza un envio maritimo existente.
     */
    MaritimeShipment update(MaritimeShipment maritimeShipment);

    /**
     * Consulta un envio maritimo por id.
     */
    MaritimeShipment getMaritimeShipmentById(long id);
    
}
