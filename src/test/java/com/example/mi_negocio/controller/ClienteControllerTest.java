package com.example.mi_negocio.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.mi_negocio.entity.Cliente;
import com.example.mi_negocio.exception.BusinessException;
import com.example.mi_negocio.exception.NotFoundException;
import com.example.mi_negocio.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    // ------------ PRUEBAS EXITOSAS ------------
    
    @Test
    @DisplayName("POST /clientes - Debe retornar 201 cuando creación exitosa")
    void crearCliente_DatosValidos_DeberiaRetornar201() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNumeroIdentificacion("1751234567");
        
        given(clienteService.crearCliente(any())).willReturn(cliente);
        
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /clientes/{id} - Debe retornar cliente existente")
    void obtenerCliente_Existente_DeberiaRetornar200() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        
        given(clienteService.obtenerClientePorId(1L)).willReturn(cliente);
        
        mockMvc.perform(get("/api/clientes/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L));
    }

    // ------------ PRUEBAS FALLIDAS ------------
    
    @Test
    @DisplayName("POST /clientes - Debe retornar 400 cuando identificación existe")
    void crearCliente_IdentificacionDuplicada_DeberiaRetornar400() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNumeroIdentificacion("1751234567");
        
        given(clienteService.crearCliente(any()))
            .willThrow(new BusinessException("Identificación duplicada"));
        
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Identificación duplicada"));
    }

    @Test
    @DisplayName("GET /clientes/{id} - Debe retornar 404 cuando cliente no existe")
    void obtenerCliente_Inexistente_DeberiaRetornar404() throws Exception {
        given(clienteService.obtenerClientePorId(anyLong()))
            .willThrow(new NotFoundException("Cliente no encontrado"));
        
        mockMvc.perform(get("/api/clientes/99"))
               .andExpect(status().isNotFound());
    }
}