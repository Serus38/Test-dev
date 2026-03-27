package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.TerrestrialShipment;

@Service
public interface TerrestrialShipmentService {

    /**
     * Lista envios terrestres.
     */
    List<TerrestrialShipment> getAllTerrestrialShipments();

    /**
     * Crea un envio terrestre.
     */
    TerrestrialShipment save(TerrestrialShipment terrestrialShipment);

    /**
     * Elimina un envio terrestre por id.
     */
    void delete(Long id);

    /**
     * Actualiza un envio terrestre existente.
     */
    TerrestrialShipment update(TerrestrialShipment terrestrialShipment);

    /**
     * Consulta un envio terrestre por id.
     */
    TerrestrialShipment getTerrestrialShipmentById(long id);
    
}
