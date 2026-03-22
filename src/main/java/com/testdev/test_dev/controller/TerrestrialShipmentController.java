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

import com.testdev.test_dev.model.TerrestrialShipment;
import com.testdev.test_dev.service.TerrestrialShipmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/terrestrial-shipment")
@Tag (name = "Terrestrial Shipment Controller", description = "Controller for managing terrestrial shipments")
public class TerrestrialShipmentController {

    private final TerrestrialShipmentService terrestrialShipmentService;

    public TerrestrialShipmentController(TerrestrialShipmentService terrestrialShipmentService) {
        this.terrestrialShipmentService = terrestrialShipmentService;
    }

    @GetMapping ("/getAll")
    @Operation(summary = "Get all terrestrial shipments", description = "Returns a list of all terrestrial shipments")
    public ResponseEntity<List<TerrestrialShipment>> getAllTerrestrialShipments() {
        return ResponseEntity.ok(terrestrialShipmentService.getAllTerrestrialShipments());
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get terrestrial shipment by ID", description = "Returns a terrestrial shipment by ID")
    public ResponseEntity<TerrestrialShipment> getTerrestrialShipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(terrestrialShipmentService.getTerrestrialShipmentById(id));
    }
    

    @PostMapping("/save")
    @Operation(summary = "Save terrestrial shipment", description = "Saves a new terrestrial shipment")
    public ResponseEntity<TerrestrialShipment> saveTerrestrialShipment(@Valid @RequestBody TerrestrialShipment terrestrialShipment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(terrestrialShipmentService.save(terrestrialShipment));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete terrestrial shipment", description = "Deletes a terrestrial shipment by their ID")
    public ResponseEntity<Void> deleteTerrestrialShipment(@PathVariable Long id) {
        terrestrialShipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{id}")
    @Operation(summary = "Edit terrestrial shipment", description = "Edits a terrestrial shipment by their ID")
    public ResponseEntity<TerrestrialShipment> editTerrestrialShipment(@PathVariable Long id,
                                                                       @Valid @RequestBody TerrestrialShipment terrestrialShipment) {
        terrestrialShipment.setId(id);
        return ResponseEntity.ok(terrestrialShipmentService.update(terrestrialShipment));
    }    
}

