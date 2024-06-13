package com.example.demo.service;

import com.example.demo.model.ComponenteMeccanico;
import com.example.demo.repository.ComponenteMeccanicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

@Service
public class ComponenteMeccanicoService {

    @Autowired
    private ComponenteMeccanicoRepository repository;

    public ComponenteMeccanico save(ComponenteMeccanico componente) {
        return repository.save(componente);
    }

    public List<ComponenteMeccanico> findAll() {
        return repository.findAll();
    }

    public Optional<ComponenteMeccanico> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public ComponenteMeccanico update(ComponenteMeccanico componente) {
        return repository.save(componente);
    }

    public ComponenteMeccanico findByCodiceProdotto(String codiceProdotto) {
        return repository.findByCodiceProdotto(codiceProdotto);
    }




//Implementare la funzionalità di esportazione per cercare un componente meccanico per codice prodotto e scaricare i risultati su un file CSV
    public void exportComponentiToCSV(List<ComponenteMeccanico> componenti, Writer writer) throws IOException {
        PrintWriter printWriter = new PrintWriter(writer);
        String[] header = {"ID", "Codice Prodotto", "Nome", "Descrizione", "Quantità", "Prezzo", "Data Inserimento"};
        printWriter.println(String.join(",", header));

        for (ComponenteMeccanico componente : componenti) {
            String[] data = {
                    String.valueOf(componente.getId()),
                    componente.getCodiceProdotto(),
                    componente.getNome(),
                    componente.getDescrizione(),
                    String.valueOf(componente.getQuantità()),
                    String.valueOf(componente.getPrezzo()),
                    componente.getDataInserimento().toString()
            };
            printWriter.println(String.join(",", data));
        }

        printWriter.flush();
    }
}