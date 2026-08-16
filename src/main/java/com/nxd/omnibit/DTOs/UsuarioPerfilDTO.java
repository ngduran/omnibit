package com.nxd.omnibit.DTOs;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record UsuarioPerfilDTO(  

        @NotNull(message = "O authUuid é obrigatório")
        UUID authUuid,

        @NotNull(message = "O UUID do cargo é obrigatório")
        UUID cargoUuid,

        @NotNull(message = "O UUID da pastoral é obrigatório")
        UUID pastoralUuid

    )
{}
