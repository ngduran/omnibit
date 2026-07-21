package com.nxd.omnibit.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nxd.omnibit.DTOs.CargoDTO;
import com.nxd.omnibit.mappers.CargoMapper;
import com.nxd.omnibit.models.Cargo;
import com.nxd.omnibit.repositories.CargoRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class CargoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CargoRepository cargoRepository;

    @MockitoBean
    private CargoMapper cargoMapper;

    private CargoDTO cargoDTOValido;
    private Cargo cargoInstancia;
    private final UUID uuidFicticio = UUID.randomUUID();

    @BeforeEach
    void setUp() {
            // 1. Instancia o DTO padrão para o corpo da requisição
            cargoDTOValido = new CargoDTO(
                "Coordenador", "Coordena as pessoas nas atividades da pastoral"                    
            );

            // 2. Adequação ao Construtor Real (Eliminando o Mockito.mock)
            cargoInstancia = new Cargo(
                    "Coordenador", 
                    "Coordena as pessoas nas atividades da pastoral"
                    
            );
    }

    @Nested
    @DisplayName("Cenários de Sucesso")
    class Sucesso {

        @Test
        @DisplayName("Deve criar o cargo com sucesso e retornar o uuid gerado")
        void deveCriarCargoComSucesso() throws Exception {
            when(cargoRepository.existsByNome(cargoDTOValido.nome())).thenReturn(false);
            when(cargoMapper.toCargo(any(CargoDTO.class))).thenReturn(cargoInstancia);
            when(cargoRepository.save(any(Cargo.class))).thenReturn(cargoInstancia);

            mockMvc.perform(post("/cargos/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cargoDTOValido)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.uuid").exists());
        }
    }
    
}
