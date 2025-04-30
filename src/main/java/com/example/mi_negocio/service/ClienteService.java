package com.example.mi_negocio.service;

import com.example.mi_negocio.entity.Cliente;
import com.example.mi_negocio.entity.Direccion;
import com.example.mi_negocio.exception.BusinessException;
import com.example.mi_negocio.exception.NotFoundException;
import com.example.mi_negocio.repository.ClienteRepository;
import com.example.mi_negocio.repository.DireccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final DireccionRepository direccionRepository;

    public ClienteService(ClienteRepository clienteRepository, DireccionRepository direccionRepository) {
        this.clienteRepository = clienteRepository;
        this.direccionRepository = direccionRepository;
    }

    // Crear cliente con dirección matriz
    public Cliente crearCliente(Cliente cliente) {
        if (clienteRepository.existsByNumeroIdentificacion(cliente.getNumeroIdentificacion())) {
            throw new BusinessException("Ya existe un cliente con este número de identificación");
        }
        
        // Validar dirección matriz
        validarDireccionMatriz(cliente);
        
        return clienteRepository.save(cliente);
    }

    // Buscar clientes por parámetro
    public List<Cliente> buscarClientes(String parametro) {
        if (parametro == null || parametro.isEmpty()) {
            return clienteRepository.findAll();
        }
        return clienteRepository.buscarPorNombreOIdentificacion(parametro);
    }

    // Obtener cliente por ID
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
    }

    // Actualizar cliente
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = obtenerClientePorId(id);
        
        if (!clienteExistente.getNumeroIdentificacion().equals(clienteActualizado.getNumeroIdentificacion())) {
            if (clienteRepository.existsByNumeroIdentificacion(clienteActualizado.getNumeroIdentificacion())) {
                throw new BusinessException("El número de identificación ya está en uso");
            }
        }

        clienteExistente.setTipoIdentificacion(clienteActualizado.getTipoIdentificacion());
        clienteExistente.setNumeroIdentificacion(clienteActualizado.getNumeroIdentificacion());
        clienteExistente.setNombres(clienteActualizado.getNombres());
        clienteExistente.setCorreo(clienteActualizado.getCorreo());
        clienteExistente.setNumeroCelular(clienteActualizado.getNumeroCelular());

        return clienteRepository.save(clienteExistente);
    }

    // Eliminar cliente
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new NotFoundException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }

    // Agregar dirección adicional
    public Direccion agregarDireccion(Long clienteId, Direccion direccion) {
        Cliente cliente = obtenerClientePorId(clienteId);
        
        if (direccion.isEsMatriz()) {
            throw new BusinessException("No se puede agregar otra dirección matriz");
        }
        
        direccion.setCliente(cliente);
        return direccionRepository.save(direccion);
    }

    // Listar direcciones de un cliente
    public List<Direccion> obtenerDireccionesDeCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new NotFoundException("Cliente no encontrado");
        }
        return direccionRepository.findByClienteId(clienteId);
    }

    // Validación de dirección matriz
    private void validarDireccionMatriz(Cliente cliente) {
        long countMatriz = cliente.getDirecciones().stream()
                .filter(Direccion::isEsMatriz)
                .count();
        
        if (countMatriz != 1) {
            throw new BusinessException("Debe tener exactamente una dirección matriz");
        }
    }
}