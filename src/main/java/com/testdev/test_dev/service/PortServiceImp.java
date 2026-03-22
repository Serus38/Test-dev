package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.Port;
import com.testdev.test_dev.repository.PortRepository;

@Service
public class PortServiceImp implements PortService {

    PortRepository portRepository;

    public PortServiceImp(PortRepository portRepository) {
        this.portRepository = portRepository;
    }

    @Override
    public List<Port> getAllPorts() {
        return portRepository.findAll();
    }

    @Override
    public Port save(Port port) {
        return portRepository.save(port);
    }

    @Override
    public void delete(Long id) {
        portRepository.deleteById(id);
    }

    @Override
    public Port update(Port port) {
        return portRepository.save(port);
    }

    @Override
    public Port getPortById(long id) {
        return portRepository.findById(id).orElse(null);
    }

    
}
