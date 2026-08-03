package com.nxd.omnibit.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxd.omnibit.DTOs.PastoralDTO;
import com.nxd.omnibit.models.Pastoral;
import com.nxd.omnibit.services.PastoralService;


@RestController
@CrossOrigin("*")
@RequestMapping("/pastoral")
public class PastoralController {
    
    private final PastoralService pastoralService;

    public PastoralController(PastoralService pastoralService) {
        this.pastoralService = pastoralService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PastoralDTO pastoralRequest) {
        try {
            Pastoral pastoralNew = pastoralService.criarPastoral(pastoralRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("uuid", pastoralNew.getUuid()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Pastoral>> listar() {
        List<Pastoral> pastorais = pastoralService.listarPastorais(); // Certifique-se de que o método no service possui este nome ou ajuste conforme sua regra de negócio
        return ResponseEntity.ok(pastorais);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PastoralDTO pastoralRequest) {
        try {
            Pastoral pastoralUpdated = pastoralService.atualizarCargo(id, pastoralRequest);
            return ResponseEntity.ok(pastoralUpdated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            pastoralService.deletarPastoral(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
