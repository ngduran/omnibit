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

import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios-perfis")
public class UsuarioPerfilController {

    private final UsuarioPerfilService usuarioPerfilService;

    public UsuarioPerfilController(UsuarioPerfilService usuarioPerfilService) {
        this.usuarioPerfilService = usuarioPerfilService;
    }

    @PostMapping("/create")
    public ResponseEntity<UsuarioPerfil> salvarOuAtualizar(@RequestBody @Valid UsuarioPerfilDTO dto) {
        UsuarioPerfil perfilSalvo = usuarioPerfilService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilSalvo);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioPerfil>> listar() {
        List<UsuarioPerfil> usuarioPerfils = usuarioPerfilService.listarUsuariosPerfils(); 
        return ResponseEntity.ok(usuarioPerfils);
    }
}
