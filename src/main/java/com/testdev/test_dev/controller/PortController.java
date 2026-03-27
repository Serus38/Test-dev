package com.testdev.test_dev.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testdev.test_dev.model.Port;
import com.testdev.test_dev.service.PortService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/port")
@Tag (name = "Port Controller", description = "Controller for managing ports")
public class PortController {

    private final PortService portService;

    /**
     * Constructor con inyeccion del servicio de puertos.
     */
    public PortController(PortService portService) {
        this.portService = portService;
    }

    /**
     * Lista todos los puertos.
     */
    @GetMapping ("/getAll")
    @Operation(summary = "Get all ports", description = "Returns a list of all ports")
    public ResponseEntity<List<Port>> getAllPorts() {
        return ResponseEntity.ok(portService.getAllPorts());
    }

    /**
     * Obtiene un puerto por id.
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "Get port by ID", description = "Returns a port by ID")
    public ResponseEntity<Port> getPortById(@PathVariable Long id) {
        return ResponseEntity.ok(portService.getPortById(id));
    }
    

    /**
     * Crea un puerto nuevo.
     */
    @PostMapping("/save")
    @Operation(summary = "Save port", description = "Saves a new port")
    public ResponseEntity<Port> savePort(@Valid @RequestBody Port port) {
        return ResponseEntity.status(HttpStatus.CREATED).body(portService.save(port));
    }

    /**
     * Elimina un puerto por id.
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete port", description = "Deletes a port by their ID")
    public ResponseEntity<Void> deletePort(@PathVariable Long id) {
        portService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza un puerto existente por id.
     */
    @PutMapping("/edit/{id}")
    @Operation(summary = "Edit port", description = "Edits a port by their ID")
    public ResponseEntity<Port> editPort(@PathVariable Long id, @Valid @RequestBody Port port) {
        port.setId(id);
        return ResponseEntity.ok(portService.update(port));
    }    
}
