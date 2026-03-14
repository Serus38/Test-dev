package com.testdev.test_dev.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.testdev.test_dev.model.Port;

@Service
public interface PortService {

    List<Port> getAllPorts();
    void save(Port port);
    void delete(Long id);
    void update(Port port);
    Port getPortById(long id);

}
