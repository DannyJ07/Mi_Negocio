package com.example.mi_negocio.controller;

import com.example.mi_negocio.entity.Cliente;
import com.example.mi_negocio.entity.Direccion;
import com.example.mi_negocio.exception.BusinessException;
import com.example.mi_negocio.exception.NotFoundException;
import com.example.mi_negocio.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Crear cliente
    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody Cliente cliente) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(clienteService.crearCliente(cliente));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

 // Listar/buscar clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes(
            @RequestParam(name = "search", required = false) String search) {
        return ResponseEntity.ok(clienteService.buscarClientes(search));
    }

    // Obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCliente(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(
            @PathVariable(name = "id") Long id,
            @RequestBody Cliente cliente) {
        try {
            return ResponseEntity.ok(clienteService.actualizarCliente(id, cliente));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable(name = "id") Long id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Agregar dirección a cliente
    @PostMapping("/{clienteId}/direcciones")
    public ResponseEntity<?> agregarDireccion(
    		@PathVariable("clienteId") Long clienteId, @RequestBody Direccion direccion) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(clienteService.agregarDireccion(clienteId, direccion));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar direcciones de cliente
    @GetMapping("/{clienteId}/direcciones")
    public ResponseEntity<?> listarDirecciones(@PathVariable("clienteId") Long clienteId) {
        try {
            return ResponseEntity.ok(clienteService.obtenerDireccionesDeCliente(clienteId));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
