package com.testdev.test_dev.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.testdev.test_dev.model.Port;

@Service
public interface PortService {

    /**
     * Lista todos los puertos.
     */
    List<Port> getAllPorts();

    /**
     * Crea un puerto nuevo.
     */
    Port save(Port port);

    /**
     * Elimina un puerto por id.
     */
    void delete(Long id);

    /**
     * Actualiza un puerto existente.
     */
    Port update(Port port);

    /**
     * Obtiene un puerto por id.
     */
    Port getPortById(long id);

}
