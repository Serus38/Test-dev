package com.testdev.test_dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testdev.test_dev.exception.ResourceNotFoundException;
import com.testdev.test_dev.model.Client;
import com.testdev.test_dev.repository.ClientRepository;

@Service
public class ClientServiceImp implements ClientService {

    // Repositorio para acceso a datos de clientes.
    ClientRepository clientRepository;

    /**
     * Constructor con inyeccion del repositorio de clientes.
     */
    public ClientServiceImp(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Lista todos los clientes persistidos.
     */
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    /**
     * Elimina un cliente por id.
     */
    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    /**
     * Retorna un cliente por id o lanza excepcion si no existe.
     */
    @Override
    public Client getClientById(long id) {
        Client client = clientRepository.findById(id).orElseThrow(
            () -> {
                throw new ResourceNotFoundException("Client not found with id: " + id);
            }

        );
        return client;
    }

    /**
     * Guarda un cliente nuevo.
     */
    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    /**
     * Actualiza la informacion de un cliente existente.
     */
    @Override
    public Client update(Client client) {
        return clientRepository.save(client);
    }
 
    
    
}
