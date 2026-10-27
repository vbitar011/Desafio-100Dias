package com.victor.saas_pagamentos.repository;

import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.model.Pagamento;
import com.victor.saas_pagamentos.model.StatusPagamento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByAssinaturaAndStatus(Assinatura assinatura, StatusPagamento status);

    @EntityGraph(attributePaths = {"assinatura", "assinatura.cliente", "assinatura.plano"})
    List<Pagamento> findAll();

    @EntityGraph(attributePaths = {"assinatura", "assinatura.cliente", "assinatura.plano"})
    List<Pagamento> findByAssinaturaClienteId(Long clienteId);

    boolean existsByAssinaturaIdAndStatus(Long assinaturaId, StatusPagamento status);
}