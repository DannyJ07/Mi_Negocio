package com.example.mi_negocio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mi_negocio.entity.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
 List<Direccion> findByClienteId(Long clienteId);
 boolean existsByClienteIdAndEsMatrizTrue(Long clienteId);
}
