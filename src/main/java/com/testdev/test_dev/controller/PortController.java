package com.testdev.test_dev.controller;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.testdev.test_dev.model.Port;
import com.testdev.test_dev.service.PortService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/port")
@Tag (name = "Port Controller", description = "Controller for managing ports")
public class PortController {

    private final PortService portService;

    public PortController(PortService portService) {
        this.portService = portService;
    }

    @GetMapping ("/getAll")
    @Operation(summary = "Get all ports", description = "Returns a list of all ports")
    public String getAllPorts(Model model) {
        List<Port> ports = portService.getAllPorts();
        model.addAttribute("ports", ports);
        return "List of ports: " + ports.toString();
    }

    @GetMapping("/new")
    @Operation(summary = "New port", description = "Creates a new port")
    public String newPort(Model model) {
        model.addAttribute("port", new Port());
        return "ports/newport";
    }
    

    @PostMapping("/save")
    @Operation(summary = "Save port", description = "Saves a new port")
    public String savePort(@ModelAttribute Port port,
                            BindingResult result, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()){
            return "ports/newport";
        }
        portService.save(port);
        redirectAttrs.addFlashAttribute("mensaje", "Register saved");
        return "redirect:/port/getAll";
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "Delete port", description = "Deletes a port by their ID")
    public String deletePort(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        portService.delete(id);
        redirectAttrs.addFlashAttribute("mensaje", "Register deleted");
        return "redirect:/port/getAll";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Edit port", description = "Edits a port by their ID")
    public String editPort(@PathVariable Long id, Model model) {
        Port port = portService.getPortById(id);
        model.addAttribute("port", port);
        return "ports/editport";
    }    
}
