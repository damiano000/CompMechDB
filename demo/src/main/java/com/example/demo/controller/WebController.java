package com.example.demo.controller;

import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ComponenteMeccanico;
import com.example.demo.service.ComponenteMeccanicoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ComponenteMeccanicoService service;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/addComponent")
    public String addComponentForm(Model model) {
        model.addAttribute("componenteMeccanico", new ComponenteMeccanico());
        return "addComponent";
    }

    @PostMapping("/addComponent")
    public String addComponentSubmit(@ModelAttribute ComponenteMeccanico componenteMeccanico, Model model) {
        try {
            if (service.findByCodiceProdotto(componenteMeccanico.getCodiceProdotto()) != null) {
                throw new DuplicateResourceException("Codice Prodotto già esistente");
            }
            service.save(componenteMeccanico);
            model.addAttribute("message", "Componente aggiunto con successo!");
        } catch (DuplicateResourceException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("error", "Errore interno del server: " + ex.getMessage());
        }
        return "addComponent";
    }

    @GetMapping("/updateComponent")
    public String updateComponentForm(Model model) {
        model.addAttribute("componenteMeccanico", new ComponenteMeccanico());
        return "updateComponent";
    }

    @PostMapping("/updateComponent")
    public String updateComponentSubmit(@ModelAttribute ComponenteMeccanico componenteMeccanico, Model model) {
        try {
            if (service.findById(componenteMeccanico.getId()).isEmpty()) {
                throw new ResourceNotFoundException("Componente non trovato con ID: " + componenteMeccanico.getId());
            }
            service.update(componenteMeccanico);
            model.addAttribute("message", "Componente aggiornato con successo!");
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("error", "Errore interno del server: " + ex.getMessage());
        }
        return "updateComponent";
    }

    @GetMapping("/deleteComponent")
    public String deleteComponentForm() {
        return "deleteComponent";
    }

    @PostMapping("/deleteComponent")
    public String deleteComponentSubmit(@RequestParam Long id, Model model) {
        try {
            if (service.findById(id).isEmpty()) {
                throw new ResourceNotFoundException("Componente non trovato con ID: " + id);
            }
            service.deleteById(id);
            model.addAttribute("message", "Componente cancellato con successo!");
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("error", "Errore interno del server: " + ex.getMessage());
        }
        return "deleteComponent";
    }

    @GetMapping("/searchComponent")
    public String searchComponentForm() {
        return "searchComponent";
    }

    @PostMapping("/searchComponent")
    public String searchComponentSubmit(@RequestParam String codiceProdotto, Model model) {
        try {
            ComponenteMeccanico componente = service.findByCodiceProdotto(codiceProdotto);
            if (componente == null) {
                throw new ResourceNotFoundException("Componente non trovato con Codice Prodotto: " + codiceProdotto);
            }
            model.addAttribute("componenteMeccanico", componente);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("error", "Errore interno del server: " + ex.getMessage());
        }
        return "searchComponent";
    }

    @GetMapping("/exportCSV")
    @ResponseBody
    public void exportCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"componenti_meccanici.csv\"");

        List<ComponenteMeccanico> componenti = service.findAll();
        service.exportComponentiToCSV(componenti, response.getWriter());
    }
}