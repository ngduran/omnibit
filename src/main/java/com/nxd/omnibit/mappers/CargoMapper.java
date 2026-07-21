package com.nxd.omnibit.mappers;

import org.springframework.stereotype.Component;

import com.nxd.omnibit.DTOs.CargoDTO;
import com.nxd.omnibit.models.Cargo;

@Component
public class CargoMapper {
    
    public Cargo toCargo(CargoDTO cargoRequest) {
        return new Cargo( cargoRequest.nome(), cargoRequest.descricao());
    }
}
