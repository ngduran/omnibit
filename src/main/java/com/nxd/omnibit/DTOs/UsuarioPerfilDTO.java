package com.nxd.omnibit.DTOs;

import java.util.UUID;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioPerfilDTO(
    
        @NotBlank(message = "O authUuid não pode estar em branco")
        String authUuid,

        @NotNull(message = "O UUID do cargo é obrigatório")
        UUID cargoUuid
    )
{}
