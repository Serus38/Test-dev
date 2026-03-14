package com.testdev.test_dev.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.testdev.test_dev.model.MaritimeShipment;
import com.testdev.test_dev.service.MaritimeShipmentService;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/maritime-shipment")
@Tag (name = "Maritime Shipment Controller", description = "Controller for managing maritime shipments")
public class MaritimeShipmentController {

    private final MaritimeShipmentService maritimeShipmentService;

    public MaritimeShipmentController(MaritimeShipmentService maritimeShipmentService) {
        this.maritimeShipmentService = maritimeShipmentService;
    }

    @GetMapping ("/getAll")
    @Operation(summary = "Get all maritime shipments", description = "Returns a list of all maritime shipments")
    public String getAllMaritimeShipments(Model model) {
        List<MaritimeShipment> maritimeShipments = maritimeShipmentService.getAllMaritimeShipments();
        model.addAttribute("maritimeShipments", maritimeShipments);
        return "List of maritime shipments: " + maritimeShipments.toString();
    }

    @GetMapping("/new")
    @Operation(summary = "New maritime shipment", description = "Creates a new maritime shipment")
    public String newMaritimeShipment(Model model) {
        model.addAttribute("maritimeShipment", new MaritimeShipment());
        return "maritime-shipments/newmaritimeshipment";
    }
    

    @PostMapping("/save")
    @Operation(summary = "Save maritime shipment", description = "Saves a new maritime shipment")
    public String saveMaritimeShipment(@ModelAttribute MaritimeShipment maritimeShipment,
                                      BindingResult result, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()){
            return "maritime-shipments/newmaritimeshipment";
        }
        maritimeShipmentService.save(maritimeShipment);
        redirectAttrs.addFlashAttribute("mensaje", "Register saved");
        return "redirect:/maritime-shipment/getAll";
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "Delete maritime shipment", description = "Deletes a maritime shipment by their ID")
    public String deleteMaritimeShipment(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        maritimeShipmentService.delete(id);
        redirectAttrs.addFlashAttribute("mensaje", "Register deleted");
        return "redirect:/maritime-shipment/getAll";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Edit maritime shipment", description = "Edits a maritime shipment by their ID")
    public String editMaritimeShipment(@PathVariable Long id, Model model) {
        MaritimeShipment maritimeShipment = maritimeShipmentService.getMaritimeShipmentById(id);
        model.addAttribute("maritimeShipment", maritimeShipment);
        return "maritime-shipments/editmaritimeshipment";
    }    
}

