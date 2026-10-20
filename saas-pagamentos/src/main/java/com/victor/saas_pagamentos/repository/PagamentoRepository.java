package com.victor.saas_pagamentos.repository;

import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.model.Pagamento;
import com.victor.saas_pagamentos.model.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByAssinaturaIdAndStatus(Long assinaturaId, String status);
    List<Pagamento> findByAssinaturaAndStatus(Assinatura assinatura, StatusPagamento status);
}