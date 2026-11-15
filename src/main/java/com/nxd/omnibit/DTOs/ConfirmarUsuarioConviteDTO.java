package com.nxd.omnibit.DTOs;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConfirmarUsuarioConviteDTO(

    @NotNull(message = "O authUuid é obrigatório")
    UUID authUuid,

    @NotBlank(message = "O token do convite é obrigatório")
    @Size(min = 8, max = 8, message = "O token deve conter 8 caracteres")
    String tokenConvite

) {}