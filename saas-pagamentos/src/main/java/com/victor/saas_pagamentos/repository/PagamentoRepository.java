package com.victor.saas_pagamentos.repository;

import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.model.Pagamento;
import com.victor.saas_pagamentos.model.StatusPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByAssinaturaAndStatus(Assinatura assinatura, StatusPagamento status);

    @EntityGraph(attributePaths = {"assinatura", "assinatura.cliente", "assinatura.plano"})
    Page<Pagamento> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"assinatura", "assinatura.cliente", "assinatura.plano"})
    Page<Pagamento> findByAssinaturaClienteId(Long clienteId, Pageable pageable);

    boolean existsByAssinaturaIdAndStatus(Long assinaturaId, StatusPagamento status);
}