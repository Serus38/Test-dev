package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.Bodega;
import com.testdev.test_dev.repository.BodegaRepository;

@Service
public class BodegaServiceImp implements BodegaService {

    BodegaRepository bodegaRepository;

    public BodegaServiceImp(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    @Override
    public List<Bodega> getAllBodegas() {
        return bodegaRepository.findAll();
    }

    @Override
    public void save(Bodega bodega) {
        bodegaRepository.save(bodega);
    }

    @Override
    public void delete(long id) {
        bodegaRepository.deleteById(id);
    }

    @Override
    public void update(Bodega bodega) {
        bodegaRepository.save(bodega);
    }

    @Override
    public Bodega getBodegaById(long id) {
        return bodegaRepository.findById(id).orElse(null);
    }

}