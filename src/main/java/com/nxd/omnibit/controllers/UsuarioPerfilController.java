package com.nxd.omnibit.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nxd.omnibit.DTOs.UsuarioPerfilDTO;
import com.nxd.omnibit.models.UsuarioPerfil;
import com.nxd.omnibit.services.UsuarioPerfilService;
import com.nxd.omnibit.DTOs.UsuarioPerfilConviteDTO;
import com.nxd.omnibit.DTOs.ConfirmarUsuarioConviteDTO;

import jakarta.validation.Valid;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios-perfis")
public class UsuarioPerfilController {

    private final UsuarioPerfilService usuarioPerfilService;

    private final String integrationApiKey;

    public UsuarioPerfilController(
        UsuarioPerfilService usuarioPerfilService,
        @Value("${OMNIBIT_INTEGRATION_API_KEY}") String integrationApiKey) {

        this.usuarioPerfilService = usuarioPerfilService;

        if (integrationApiKey == null || integrationApiKey.isBlank()) {
            throw new IllegalStateException(
                "A chave de integração do Omnibit não foi configurada."
            );
        }

        this.integrationApiKey = integrationApiKey;
    }

    @PostMapping("/create")
    public ResponseEntity<UsuarioPerfil> salvarOuAtualizar(@RequestBody @Valid UsuarioPerfilDTO dto) {
        UsuarioPerfil perfilSalvo = usuarioPerfilService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilSalvo);
    }

    @PostMapping("/convite")
    public ResponseEntity<UsuarioPerfil> salvarConvite(
            @RequestBody @Valid UsuarioPerfilConviteDTO dto) {

        UsuarioPerfil perfilSalvo = usuarioPerfilService.salvarConvite(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(perfilSalvo);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioPerfil>> listar() {
        List<UsuarioPerfil> usuarioPerfils = usuarioPerfilService.listarUsuariosPerfils(); 
        return ResponseEntity.ok(usuarioPerfils);
    }

    @PostMapping("/convite/confirmar")
    public ResponseEntity<UsuarioPerfil> confirmarUsuarioConvite(

            @RequestHeader(
                value = "X-Omnibit-Integration-Key",
                required = false
            ) String apiKey,

            @RequestBody @Valid ConfirmarUsuarioConviteDTO dto) {

        if (!chaveIntegracaoValida(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UsuarioPerfil perfilAtualizado =
            usuarioPerfilService.confirmarUsuarioConvite(dto);

        return ResponseEntity.ok(perfilAtualizado);
    }

    private boolean chaveIntegracaoValida(String chaveRecebida) {

        if (chaveRecebida == null) {
            return false;
        }

        return MessageDigest.isEqual(
            integrationApiKey.getBytes(StandardCharsets.UTF_8),
            chaveRecebida.getBytes(StandardCharsets.UTF_8)
        );
    }
}
