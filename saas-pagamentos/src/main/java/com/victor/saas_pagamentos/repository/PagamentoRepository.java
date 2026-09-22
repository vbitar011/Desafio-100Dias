package com.victor.saas_pagamentos.repository;

import com.victor.saas_pagamentos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByAssinaturaIdAndStatus(Long assinaturaId, String status);
}