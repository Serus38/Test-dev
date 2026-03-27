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

import com.testdev.test_dev.model.MaritimeShipment;
import com.testdev.test_dev.service.MaritimeShipmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/maritime-shipment")
@Tag (name = "Maritime Shipment Controller", description = "Controller for managing maritime shipments")
public class MaritimeShipmentController {

    private final MaritimeShipmentService maritimeShipmentService;

    /**
     * Constructor del controlador para operaciones de envios maritimos.
     */
    public MaritimeShipmentController(MaritimeShipmentService maritimeShipmentService) {
        this.maritimeShipmentService = maritimeShipmentService;
    }

    /**
     * Retorna todos los envios maritimos.
     */
    @GetMapping ("/getAll")
    @Operation(summary = "Get all maritime shipments", description = "Returns a list of all maritime shipments")
    public ResponseEntity<List<MaritimeShipment>> getAllMaritimeShipments() {
        return ResponseEntity.ok(maritimeShipmentService.getAllMaritimeShipments());
    }

    /**
     * Busca un envio maritimo por id.
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "Get maritime shipment by ID", description = "Returns a maritime shipment by ID")
    public ResponseEntity<MaritimeShipment> getMaritimeShipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(maritimeShipmentService.getMaritimeShipmentById(id));
    }
    

    /**
     * Registra un envio maritimo nuevo.
     */
    @PostMapping("/save")
    @Operation(summary = "Save maritime shipment", description = "Saves a new maritime shipment")
    public ResponseEntity<MaritimeShipment> saveMaritimeShipment(@Valid @RequestBody MaritimeShipment maritimeShipment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maritimeShipmentService.save(maritimeShipment));
    }

    /**
     * Elimina un envio maritimo por id.
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete maritime shipment", description = "Deletes a maritime shipment by their ID")
    public ResponseEntity<Void> deleteMaritimeShipment(@PathVariable Long id) {
        maritimeShipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza un envio maritimo existente por id.
     */
    @PutMapping("/edit/{id}")
    @Operation(summary = "Edit maritime shipment", description = "Edits a maritime shipment by their ID")
    public ResponseEntity<MaritimeShipment> editMaritimeShipment(@PathVariable Long id,
                                                                 @Valid @RequestBody MaritimeShipment maritimeShipment) {
        maritimeShipment.setId(id);
        return ResponseEntity.ok(maritimeShipmentService.update(maritimeShipment));
    }    
}

