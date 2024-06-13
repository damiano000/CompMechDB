package com.example.demo.repository;

import com.example.demo.model.ComponenteMeccanico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponenteMeccanicoRepository extends JpaRepository<ComponenteMeccanico, Long> {
    ComponenteMeccanico findByCodiceProdotto(String codiceProdotto);
}


