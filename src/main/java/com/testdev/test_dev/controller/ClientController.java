package com.testdev.test_dev.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.testdev.test_dev.model.Client;
import com.testdev.test_dev.service.ClientService;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/client")
@Tag (name = "Client Controller", description = "Controller for managing clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping ("/getAll")
    @Operation(summary = "Get all clients", description = "Returns a list of all clients")
    public String getAllClients(Model model) {
        List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        return "List of clients: " + clients.toString();
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get client by ID", description = "Returns a client by their ID")
    public String getClientById(@PathVariable Long id, Model model) {
        Client client = clientService.getClientById(id);
        model.addAttribute("client", client);
        return "Client details: " + client.toString();
    }

    @GetMapping("/new")
    @Operation(summary = "New client", description = "Creates a new client")
    public String newClient(Model model) {
        model.addAttribute("client", new Client());
        return "clients/newclient";
    }
    

    @PostMapping("/save")
    @Operation(summary = "Save client", description = "Saves a new client")
    public String saveClient(@ModelAttribute Client client,
                            BindingResult result, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()){
            return "clients/newclient";
        }
        clientService.save(client);
        redirectAttrs.addFlashAttribute("mensaje", "Register saved");
        return "redirect:/client/getAll";
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "Delete client", description = "Deletes a client by their ID")
    public String deleteClient(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        clientService.delete(id);
        redirectAttrs.addFlashAttribute("mensaje", "Register deleted");
        return "redirect:/client/getAll";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Edit client", description = "Edits a client by their ID")
    public String editClient(@PathVariable Long id, Model model) {
        Client client = clientService.getClientById(id);
        model.addAttribute("client", client);
        return "clients/editclient";
    }    
}
