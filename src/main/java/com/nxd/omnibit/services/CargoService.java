package com.nxd.omnibit.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxd.omnibit.DTOs.CargoDTO;
import com.nxd.omnibit.mappers.CargoMapper;
import com.nxd.omnibit.models.Cargo;
import com.nxd.omnibit.repositories.CargoRepository;

@Service
public class CargoService {
    
    private final CargoRepository cargoRepository;
    private final CargoMapper cargoMapper;

    public CargoService(CargoRepository cargoRepository, CargoMapper cargoMapper) {
        this.cargoRepository = cargoRepository;
        this.cargoMapper = cargoMapper;
    }

    @Transactional
    public Cargo criarCargo(CargoDTO dto) {
        // Validação de negócio: impede duplicidade de nome
        if (cargoRepository.existsByNome(dto.nome())) {
            throw new IllegalArgumentException("Este cargo já está cadastrado.");
        }

        // Conversão via Mapper e persistência no banco
        Cargo cargo = cargoMapper.toCargo(dto);
        return cargoRepository.save(cargo);
    }

    @Transactional(readOnly = true)
    public List<Cargo> listarCargos() {
        return cargoRepository.findAll();
    }

    @Transactional
    public Cargo atualizarCargo(Long id, CargoDTO dto) {
        Cargo cargo = cargoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cargo não encontrado."));

        // Se o nome foi alterado, valida se já existe outro cargo com o mesmo nome
        if (!cargo.getNome().equals(dto.nome()) && cargoRepository.existsByNome(dto.nome())) {
            throw new IllegalArgumentException("Já existe um cargo cadastrado com este nome.");
        }

        cargo.setNome(dto.nome());
        cargo.setDescricao(dto.descricao());
        
        return cargoRepository.save(cargo);
    }

    @Transactional
    public void deletarCargo(Long id) {
        if (!cargoRepository.existsById(id)) {
            throw new RuntimeException("Cargo não encontrado.");
        }
        cargoRepository.deleteById(id);
    }
}
