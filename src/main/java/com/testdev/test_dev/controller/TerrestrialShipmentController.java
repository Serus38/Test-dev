package com.testdev.test_dev.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.testdev.test_dev.model.TerrestrialShipment;
import com.testdev.test_dev.service.TerrestrialShipmentService;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    public String getAllTerrestrialShipments(Model model) {
        List<TerrestrialShipment> terrestrialShipments = terrestrialShipmentService.getAllTerrestrialShipments();
        model.addAttribute("terrestrialShipments", terrestrialShipments);
        return "List of terrestrial shipments: " + terrestrialShipments.toString();
    }

    @GetMapping("/new")
    @Operation(summary = "New terrestrial shipment", description = "Creates a new terrestrial shipment")
    public String newTerrestrialShipment(Model model) {
        model.addAttribute("terrestrialShipment", new TerrestrialShipment());
        return "terrestrial-shipments/newterrestrialshipment";
    }
    

    @PostMapping("/save")
    @Operation(summary = "Save terrestrial shipment", description = "Saves a new terrestrial shipment")
    public String saveTerrestrialShipment(@ModelAttribute TerrestrialShipment terrestrialShipment,
                            BindingResult result, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()){
            return "terrestrial-shipments/newterrestrialshipment";
        }
        terrestrialShipmentService.save(terrestrialShipment);
        redirectAttrs.addFlashAttribute("mensaje", "Register saved");
        return "redirect:/terrestrial-shipment/getAll";
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "Delete terrestrial shipment", description = "Deletes a terrestrial shipment by their ID")
    public String deleteTerrestrialShipment(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        terrestrialShipmentService.delete(id);
        redirectAttrs.addFlashAttribute("mensaje", "Register deleted");
        return "redirect:/terrestrial-shipment/getAll";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Edit terrestrial shipment", description = "Edits a terrestrial shipment by their ID")
    public String editTerrestrialShipment(@PathVariable Long id, Model model) {
        TerrestrialShipment terrestrialShipment = terrestrialShipmentService.getTerrestrialShipmentById(id);
        model.addAttribute("terrestrialShipment", terrestrialShipment);
        return "terrestrial-shipments/editterrestrialshipment";
    }    
}

