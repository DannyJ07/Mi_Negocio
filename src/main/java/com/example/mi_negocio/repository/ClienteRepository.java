package com.example.mi_negocio.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mi_negocio.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.numeroIdentificacion = :numero")
    boolean existsByNumeroIdentificacion(@Param("numero") String numeroIdentificacion);
    
    @Query("SELECT c FROM Cliente c WHERE c.nombres LIKE %:parametro% OR c.numeroIdentificacion LIKE %:parametro%")
    List<Cliente> buscarPorNombreOIdentificacion(@Param("parametro") String parametro);
    
    Optional<Cliente> findByNumeroIdentificacion(String numeroIdentificacion);
}