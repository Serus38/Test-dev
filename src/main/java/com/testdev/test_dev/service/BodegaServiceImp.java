package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.Bodega;
import com.testdev.test_dev.repository.BodegaRepository;

@Service
public class BodegaServiceImp implements BodegaService {

    // Repositorio para operaciones CRUD de bodegas.
    BodegaRepository bodegaRepository;

    /**
     * Constructor con inyeccion del repositorio de bodegas.
     */
    public BodegaServiceImp(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    /**
     * Obtiene todas las bodegas.
     */
    @Override
    public List<Bodega> getAllBodegas() {
        return bodegaRepository.findAll();
    }

    /**
     * Guarda una bodega nueva.
     */
    @Override
    public Bodega save(Bodega bodega) {
        return bodegaRepository.save(bodega);
    }

    /**
     * Elimina una bodega por id.
     */
    @Override
    public void delete(long id) {
        bodegaRepository.deleteById(id);
    }

    /**
     * Actualiza una bodega existente.
     */
    @Override
    public Bodega update(Bodega bodega) {
        return bodegaRepository.save(bodega);
    }

    /**
     * Busca una bodega por id.
     */
    @Override
    public Bodega getBodegaById(long id) {
        return bodegaRepository.findById(id).orElse(null);
    }

}