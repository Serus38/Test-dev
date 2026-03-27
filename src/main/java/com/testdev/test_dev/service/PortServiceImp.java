package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.Port;
import com.testdev.test_dev.repository.PortRepository;

@Service
public class PortServiceImp implements PortService {

    // Repositorio para persistencia de puertos.
    PortRepository portRepository;

    /**
     * Constructor para inyeccion del repositorio de puertos.
     */
    public PortServiceImp(PortRepository portRepository) {
        this.portRepository = portRepository;
    }

    /**
     * Retorna todos los puertos.
     */
    @Override
    public List<Port> getAllPorts() {
        return portRepository.findAll();
    }

    /**
     * Guarda un puerto nuevo.
     */
    @Override
    public Port save(Port port) {
        return portRepository.save(port);
    }

    /**
     * Elimina un puerto por id.
     */
    @Override
    public void delete(Long id) {
        portRepository.deleteById(id);
    }

    /**
     * Actualiza un puerto existente.
     */
    @Override
    public Port update(Port port) {
        return portRepository.save(port);
    }

    /**
     * Busca un puerto por id.
     */
    @Override
    public Port getPortById(long id) {
        return portRepository.findById(id).orElse(null);
    }

    
}
