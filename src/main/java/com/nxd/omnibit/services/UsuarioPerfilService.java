package com.nxd.omnibit.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nxd.omnibit.DTOs.UsuarioPerfilDTO;
import com.nxd.omnibit.mappers.UsuarioPerfilMapper;
import com.nxd.omnibit.models.Cargo;
import com.nxd.omnibit.models.UsuarioPerfil;
import com.nxd.omnibit.repositories.CargoRepository;
import com.nxd.omnibit.repositories.UsuarioPerfilRepository;

@Service
public class UsuarioPerfilService {

    private final UsuarioPerfilRepository usuarioPerfilRepository;
    private final CargoRepository cargoRepository;
    private final UsuarioPerfilMapper usuarioPerfilMapper;

    public UsuarioPerfilService(
            UsuarioPerfilRepository usuarioPerfilRepository, 
            CargoRepository cargoRepository,
            UsuarioPerfilMapper usuarioPerfilMapper) {
        this.usuarioPerfilRepository = usuarioPerfilRepository;
        this.cargoRepository = cargoRepository;
        this.usuarioPerfilMapper = usuarioPerfilMapper;
    }

    @Transactional
    public UsuarioPerfil salvarOuAtualizar(UsuarioPerfilDTO dto) {
        // 1. Busca o cargo utilizando o UUID fornecido no DTO[cite: 6]
        Cargo cargo = cargoRepository.findByUuid(dto.cargoUuid())
            .orElseThrow(() -> new RuntimeException("Cargo não encontrado através do UUID fornecido."));

        // 2. Executa o fluxo de Upsert[cite: 6]
        UsuarioPerfil perfil = usuarioPerfilRepository.findByAuthUuid(dto.authUuid())
            .map(perfilExistente -> {
                perfilExistente.setCargo(cargo);
                return perfilExistente;
            })
            .orElseGet(() -> usuarioPerfilMapper.toEntity(dto, cargo));

        // 3. Persiste as alterações no banco de dados e retorna a entidade salva[cite: 6]
        return usuarioPerfilRepository.save(perfil);
    }

    @Transactional(readOnly = true)
    public List<UsuarioPerfil> listarUsuariosPerfils() {
        // Utiliza o JOIN FETCH para garantir que o Cargo seja carregado sem erros de Lazy Loading
        return usuarioPerfilRepository.findAllWithCargo();
    }
}