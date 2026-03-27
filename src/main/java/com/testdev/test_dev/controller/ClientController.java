package com.testdev.test_dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testdev.test_dev.model.Client;
import com.testdev.test_dev.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/client")
@Tag (name = "Client Controller", description = "Controller for managing clients")

@CrossOrigin(origins = {
    "http://localhost:4200",
    "https://test-dev-front-seven.vercel.app"
}, allowedHeaders = "*")
public class ClientController {

    // Servicio de negocio para operaciones CRUD de clientes.
    @Autowired
    ClientService clientService;

    /**
     * Constructor para inyeccion explicita del servicio.
     */
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Obtiene la lista completa de clientes.
     */
    @GetMapping("/getAll")
    @Operation(summary = "Get all clients", description = "Returns a list of all clients with their information")
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    /**
     * Consulta un cliente puntual por identificador.
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "Get client by ID", description = "Returns a client by their ID")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }
    

    /**
     * Crea un cliente validando previamente el cuerpo de la solicitud.
     */
    @PostMapping("/save")
    @Operation(summary = "Save client", description = "Saves a new client")
    public ResponseEntity<Client> saveClient(@Valid @RequestBody Client client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(client));
    }

    /**
     * Elimina un cliente por id.
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete client", description = "Deletes a client by their ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza un cliente existente usando el id de la ruta.
     */
    @PutMapping("/edit/{id}")
    @Operation(summary = "Edit client", description = "Edits a client by their ID")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody Client client) {
        client.setId(id);
        clientService.update(client);
        return ResponseEntity.noContent().build();
    }    
}
