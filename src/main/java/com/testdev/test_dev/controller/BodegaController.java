package com.testdev.test_dev.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.testdev.test_dev.model.Bodega;
import com.testdev.test_dev.service.BodegaService;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    public String getAllBodegas(Model model) {
        List<Bodega> bodegas = bodegaService.getAllBodegas();
        model.addAttribute("bodegas", bodegas);
        return "List of bodegas: " + bodegas.toString();
    }

    @GetMapping("/new")
    @Operation(summary = "New bodega", description = "Creates a new bodega")
    public String newBodega(Model model) {
        model.addAttribute("bodega", new Bodega());
        return "bodegas/newbodega";
    }

    @PostMapping("/save")
    @Operation(summary = "Save bodega", description = "Saves a new bodega")
    public String saveBodega(@ModelAttribute Bodega bodega, BindingResult result, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()){
            return "bodegas/newbodega";
        }
        bodegaService.save(bodega);
        redirectAttrs.addFlashAttribute("mensaje", "Register saved");
        return "redirect:/bodega/getAll";
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "Delete bodega", description = "Deletes a bodega by ID")
    public String deleteBodega(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        bodegaService.delete(id);
        redirectAttrs.addFlashAttribute("mensaje", "Register deleted");
        return "redirect:/bodega/getAll";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Edit bodega", description = "Edits a bodega by ID")
    public String editBodega(@PathVariable Long id, Model model) {
        Bodega bodega = bodegaService.getBodegaById(id);
        model.addAttribute("bodega", bodega);
        return "bodegas/editbodega";
    }
}
