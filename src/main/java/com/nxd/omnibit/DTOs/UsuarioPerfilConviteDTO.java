package com.nxd.omnibit.DTOs;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioPerfilConviteDTO(

    @NotBlank(message = "O token do convite é obrigatório")
    @Size(min = 8, max = 8, message = "O token deve conter 8 caracteres")
    String tokenConvite,

    @NotNull(message = "O UUID do cargo é obrigatório")
    UUID cargoUuid,

    @NotNull(message = "O UUID da pastoral é obrigatório")
    UUID pastoralUuid

) {}