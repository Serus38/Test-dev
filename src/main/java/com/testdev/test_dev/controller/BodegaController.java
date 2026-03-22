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

import com.testdev.test_dev.model.Bodega;
import com.testdev.test_dev.service.BodegaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/bodega")
@Tag (name = "Bodega Controller", description = "Controller for managing bodegas")
public class BodegaController {

    private final BodegaService bodegaService;

    public BodegaController(BodegaService bodegaService) {
        this.bodegaService = bodegaService;
    }
    

    @GetMapping("/getAll")
    @Operation(summary = "Get all bodegas", description = "Returns a list of all bodegas")
    public ResponseEntity<List<Bodega>> getAllBodegas() {
        return ResponseEntity.ok(bodegaService.getAllBodegas());
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get bodega by ID", description = "Returns a bodega by ID")
    public ResponseEntity<Bodega> getBodegaById(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.getBodegaById(id));
    }

    @PostMapping("/save")
    @Operation(summary = "Save bodega", description = "Saves a new bodega")
    public ResponseEntity<Bodega> saveBodega(@Valid @RequestBody Bodega bodega) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bodegaService.save(bodega));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete bodega", description = "Deletes a bodega by ID")
    public ResponseEntity<Void> deleteBodega(@PathVariable Long id) {
        bodegaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{id}")
    @Operation(summary = "Edit bodega", description = "Edits a bodega by ID")
    public ResponseEntity<Bodega> editBodega(@PathVariable Long id, @Valid @RequestBody Bodega bodega) {
        bodega.setId(id);
        return ResponseEntity.ok(bodegaService.update(bodega));
    }
}
