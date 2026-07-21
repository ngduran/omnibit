package com.nxd.omnibit.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxd.omnibit.DTOs.CargoDTO;
import com.nxd.omnibit.mappers.CargoMapper;
import com.nxd.omnibit.models.Cargo;
import com.nxd.omnibit.repositories.CargoRepository;


@RestController
@CrossOrigin("*")
@RequestMapping("/cargos")
public class CargoController {

    @Autowired private CargoRepository cargoRepository;

    @Autowired private CargoMapper cargoMapper;
  
    public CargoController(CargoRepository cargoRepository, CargoMapper cargoMapper) {
        this.cargoRepository = cargoRepository;
        this.cargoMapper = cargoMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CargoDTO cargoRequest) {

        if (cargoRepository.existsByNome(cargoRequest.nome())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este cargo já está cadastrado.");
        }

        Cargo cargo = cargoMapper.toCargo(cargoRequest);

        Cargo cargoNew = cargoRepository.save(cargo);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("uuid", cargoNew.getUuid()));
      
    }
    
}
