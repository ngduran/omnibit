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

import com.nxd.omnibit.DTOs.CargoDTO;
import com.nxd.omnibit.models.Cargo;
import com.nxd.omnibit.services.CargoService;

@RestController
@CrossOrigin("*")
@RequestMapping("/cargos")
public class CargoController {

    private final CargoService cargoService;

    // Injeção de dependência moderna via construtor
    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CargoDTO cargoRequest) {
        try {
            Cargo cargoNew = cargoService.criarCargo(cargoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("uuid", cargoNew.getUuid()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Cargo>> listar() {
        List<Cargo> cargos = cargoService.listarCargos(); // Certifique-se de que o método no service possui este nome ou ajuste conforme sua regra de negócio
        return ResponseEntity.ok(cargos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CargoDTO cargoRequest) {
        try {
            Cargo cargoUpdated = cargoService.atualizarCargo(id, cargoRequest);
            return ResponseEntity.ok(cargoUpdated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            cargoService.deletarCargo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}