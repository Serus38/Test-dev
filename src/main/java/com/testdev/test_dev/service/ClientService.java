package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.Client;

@Service
public interface ClientService {

    /**
     * Lista todos los clientes.
     */
    List<Client> getAllClients();

    /**
     * Crea un cliente nuevo.
     */
    Client save(Client client);

    /**
     * Elimina un cliente por id.
     */
    void delete(Long id);

    /**
     * Actualiza la informacion de un cliente.
     */
    Client   update(Client client);

    /**
     * Busca un cliente por id.
     */
    Client getClientById(long id);

}
