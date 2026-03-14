package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.Bodega;

@Service
public interface BodegaService {

    List<Bodega> getAllBodegas();
    void save(Bodega bodega);
    void delete(long id);
    void update(Bodega bodega);
    Bodega getBodegaById(long id);

}
