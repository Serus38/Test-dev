package com.testdev.test_dev.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.testdev.test_dev.model.Port;

@Service
public interface PortService {

    List<Port> getAllPorts();
    Port save(Port port);
    void delete(Long id);
    Port update(Port port);
    Port getPortById(long id);

}
