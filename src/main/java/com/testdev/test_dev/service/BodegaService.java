package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.Bodega;

@Service
public interface BodegaService {

    List<Bodega> getAllBodegas();
    Bodega save(Bodega bodega);
    void delete(long id);
    Bodega update(Bodega bodega);
    Bodega getBodegaById(long id);

}
