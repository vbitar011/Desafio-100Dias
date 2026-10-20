package com.victor.saas_pagamentos.repository;

import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.model.StatusAssinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssinaturaRepository extends JpaRepository<Assinatura, Long> {

    boolean existsByClienteIdAndPlanoIdAndStatus(Long clienteId, Long planoId, StatusAssinatura status);
}