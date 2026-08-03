package com.nxd.omnibit.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nxd.omnibit.DTOs.PastoralDTO;
import com.nxd.omnibit.mappers.PastoralMapper;
import com.nxd.omnibit.models.Pastoral;
import com.nxd.omnibit.repositories.PastoralRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PastoralService {
    
    private final PastoralRepository pastoralRepository;
    private final PastoralMapper pastoralMapper;
    
    public PastoralService(PastoralRepository pastoralRepository, PastoralMapper pastoralMapper) {
        this.pastoralRepository = pastoralRepository;
        this.pastoralMapper = pastoralMapper;
    }

    @Transactional
    public Pastoral criarPastoral(PastoralDTO dto) {
        // Validação de negócio: impede duplicidade de nome
        if (pastoralRepository.existsByNome(dto.nome())) {
            throw new IllegalArgumentException("Esta pastoral já está cadastrado.");
        }

        // Conversão via Mapper e persistência no banco
        Pastoral pastoral = pastoralMapper.toPastoral(dto);
        return pastoralRepository.save(pastoral);
    }

    @Transactional(readOnly = true)
    public List<Pastoral> listarPastorais() {
        return pastoralRepository.findAll();
    }
    
    @Transactional
    public Pastoral atualizarCargo(Long id, PastoralDTO dto) {
        Pastoral pastoral = pastoralRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pastoral não encontrado."));

        // Se o nome foi alterado, valida se já existe outro cargo com o mesmo nome
        if (!pastoral.getNome().equals(dto.nome()) && pastoralRepository.existsByNome(dto.nome())) {
            throw new IllegalArgumentException("Já existe um pastoral cadastrado com este nome.");
        }

        pastoral.setNome(dto.nome());
        pastoral.setDescricao(dto.descricao());
        
        return pastoralRepository.save(pastoral);
    }

    @Transactional
    public void deletarPastoral(Long id) {
        if (!pastoralRepository.existsById(id)) {
            throw new RuntimeException("Pastoral não encontrado.");
        }

        pastoralRepository.deleteById(id);
    }

}
