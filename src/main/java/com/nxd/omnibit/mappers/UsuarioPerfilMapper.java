package com.nxd.omnibit.mappers;

import org.springframework.stereotype.Component;

import com.nxd.omnibit.DTOs.UsuarioPerfilDTO;
import com.nxd.omnibit.models.Cargo;
import com.nxd.omnibit.models.UsuarioPerfil;

@Component
public class UsuarioPerfilMapper {
    
    public UsuarioPerfil toEntity(UsuarioPerfilDTO dto, Cargo cargo) {
        return new UsuarioPerfil(dto.authUuid(), cargo);
    }
}
