package com.victor.saas_pagamentos.repository;

import com.victor.saas_pagamentos.model.Assinatura;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssinaturaRepository extends JpaRepository<Assinatura, Long> {
    @EntityGraph(attributePaths = {"cliente", "plano"})
    Page<Assinatura> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"cliente", "plano"})
    Page<Assinatura> findByClienteId(Long clienteId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Assinatura a WHERE a.id = :id")
    Optional<Assinatura> findByIdParaAtualizar(@Param("id") Long id);
}