package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.Bodega;

@Service
public interface BodegaService {

    /**
     * Lista todas las bodegas.
     */
    List<Bodega> getAllBodegas();

    /**
     * Crea una bodega nueva.
     */
    Bodega save(Bodega bodega);

    /**
     * Elimina una bodega por id.
     */
    void delete(long id);

    /**
     * Actualiza una bodega existente.
     */
    Bodega update(Bodega bodega);

    /**
     * Obtiene una bodega por id.
     */
    Bodega getBodegaById(long id);

}
