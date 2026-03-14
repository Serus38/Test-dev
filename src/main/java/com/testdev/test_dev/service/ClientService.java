package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.model.Client;

@Service
public interface ClientService {

    List<Client> getAllClients();
    Client save(Client client);
    void delete(Long id);
    void update(Client client);
    Client getClientById(long id);

}
