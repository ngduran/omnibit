package com.nxd.omnibit.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nxd.omnibit.DTOs.UsuarioPerfilDTO;
import com.nxd.omnibit.mappers.UsuarioPerfilMapper;
import com.nxd.omnibit.models.Cargo;
import com.nxd.omnibit.models.Pastoral;
import com.nxd.omnibit.models.UsuarioPerfil;
import com.nxd.omnibit.repositories.CargoRepository;
import com.nxd.omnibit.repositories.PastoralRepository;
import com.nxd.omnibit.repositories.UsuarioPerfilRepository;
import com.nxd.omnibit.DTOs.UsuarioPerfilConviteDTO;
import com.nxd.omnibit.DTOs.ConfirmarUsuarioConviteDTO;

@Service
public class UsuarioPerfilService {

    private final UsuarioPerfilRepository usuarioPerfilRepository;
    
    private final CargoRepository cargoRepository;

    private final PastoralRepository pastoralRepository;

    private final UsuarioPerfilMapper usuarioPerfilMapper;
   

    public UsuarioPerfilService(UsuarioPerfilRepository usuarioPerfilRepository, CargoRepository cargoRepository,
            PastoralRepository pastoralRepository, UsuarioPerfilMapper usuarioPerfilMapper) {
        this.usuarioPerfilRepository = usuarioPerfilRepository;
        this.cargoRepository = cargoRepository;
        this.pastoralRepository = pastoralRepository;
        this.usuarioPerfilMapper = usuarioPerfilMapper;
    }

    @Transactional
    public UsuarioPerfil salvarOuAtualizar(UsuarioPerfilDTO dto) {
        // 1. Busca o cargo utilizando o UUID fornecido no DTO
        Cargo cargo = cargoRepository.findByUuid(dto.cargoUuid())
            .orElseThrow(() -> new RuntimeException("Cargo não encontrado através do UUID fornecido."));

        // 2. Busca a pastoral utilizando o UUID fornecido no DTO
        Pastoral pastoral = pastoralRepository.findByUuid(dto.pastoralUuid())
            .orElseThrow(() -> new RuntimeException("Pastoral não encontrada através do UUID fornecido."));    

        // 2. Executa o fluxo de Upsert
        UsuarioPerfil perfil = usuarioPerfilRepository.findByAuthUuid(dto.authUuid())
            .map(perfilExistente -> {
                perfilExistente.setCargo(cargo);
                perfilExistente.setPastoral(pastoral);
                return perfilExistente;
            })
            .orElseGet(() -> usuarioPerfilMapper.toEntity(dto, cargo, pastoral));

        // 3. Persiste as alterações no banco de dados e retorna a entidade salva
        return usuarioPerfilRepository.save(perfil);
    }

    @Transactional(readOnly = true)
    public List<UsuarioPerfil> listarUsuariosPerfils() {
        // Utiliza o JOIN FETCH para garantir que o Cargo seja carregado sem erros de Lazy Loading
        return usuarioPerfilRepository.findAllWithCargo();
    }

    @Transactional
    public UsuarioPerfil salvarConvite(UsuarioPerfilConviteDTO dto) {

        // Verifica se o token já possui um vínculo registrado
        if (usuarioPerfilRepository.findByTokenConvite(dto.tokenConvite()).isPresent()) {
            throw new IllegalStateException("Este convite já possui um vínculo cadastrado.");
        }

        // Busca o cargo no banco do Omnibit
        Cargo cargo = cargoRepository.findByUuid(dto.cargoUuid())
            .orElseThrow(() -> new RuntimeException("Cargo não encontrado."));

        // Busca a pastoral no banco do Omnibit
        Pastoral pastoral = pastoralRepository.findByUuid(dto.pastoralUuid())
            .orElseThrow(() -> new RuntimeException("Pastoral não encontrada."));

        // Cria o vínculo provisório, ainda sem usuário do Auctoritas
        UsuarioPerfil perfil = new UsuarioPerfil(null, cargo, pastoral);
        perfil.setTokenConvite(dto.tokenConvite());

        return usuarioPerfilRepository.save(perfil);
    }

    @Transactional
    public UsuarioPerfil confirmarUsuarioConvite(ConfirmarUsuarioConviteDTO dto) {

        // 1. Localiza o vínculo provisório pelo token
        UsuarioPerfil perfil = usuarioPerfilRepository
            .findByTokenConvite(dto.tokenConvite())
            .orElseThrow(() ->
                new IllegalArgumentException("Convite não encontrado no Omnibit.")
            );

        // 2. Verifica se o convite já foi associado a outro usuário
        if (perfil.getAuthUuid() != null) {

            // Permite repetir a mesma operação sem duplicar o vínculo
            if (perfil.getAuthUuid().equals(dto.authUuid())) {
                return perfil;
            }

            throw new IllegalStateException(
                "Este convite já foi associado a outro usuário."
            );
        }

        // 3. Verifica se o usuário já possui outro perfil
        if (usuarioPerfilRepository.findByAuthUuid(dto.authUuid()).isPresent()) {
            throw new IllegalStateException(
                "Este usuário já possui um perfil cadastrado."
            );
        }

        // 4. Completa o vínculo existente
        perfil.setAuthUuid(dto.authUuid());

        return usuarioPerfilRepository.save(perfil);
    }

}