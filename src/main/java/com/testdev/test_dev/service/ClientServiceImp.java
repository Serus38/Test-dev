package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.exception.ResourceNotFoundException;
import com.testdev.test_dev.model.Client;
import com.testdev.test_dev.repository.ClientRepository;

@Service
public class ClientServiceImp implements ClientService {

    ClientRepository clientRepository;

    public ClientServiceImp(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Client getClientById(long id) {
        Client client = clientRepository.findById(id).orElseThrow(
            () -> {
                throw new ResourceNotFoundException("Client not found with id: " + id);
            }

        );
        return client;
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void update(Client client) {
        clientRepository.save(client);
    }
 
    
    
}
