package com.victor.saas_pagamentos.service;

import com.victor.saas_pagamentos.dto.AssinaturaRequestDTO;
import com.victor.saas_pagamentos.infra.RecursoNaoEncontradoException;
import com.victor.saas_pagamentos.infra.RegraDeNegocioException;
import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.model.StatusAssinatura;
import com.victor.saas_pagamentos.repository.AssinaturaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssinaturaService {

    private final AssinaturaRepository assinaturaRepository;
    private final PagamentoService pagamentoService;

    //Injeção por construtor
    public AssinaturaService(AssinaturaRepository assinaturaRepository, PagamentoService pagamentoService) {
        this.assinaturaRepository = assinaturaRepository;
        this.pagamentoService = pagamentoService;
    }

    @Transactional
    public Assinatura criarAssinatura(Assinatura novaAssinatura) {
        //Regra: Impedir assinatura ativa duplicada para o mesmo cliente e plano
        boolean existeAtiva = assinaturaRepository.existsByClienteIdAndPlanoIdAndStatus(
                novaAssinatura.getCliente().getId(),
                novaAssinatura.getPlano().getId(),
                StatusAssinatura.ATIVA);

        if (existeAtiva) {
            throw new RegraDeNegocioException("O cliente já possui uma assinatura ativa para este plano.");
        }

        novaAssinatura.setStatus(StatusAssinatura.ATIVA);
        return assinaturaRepository.save(novaAssinatura);
    }

    @Transactional
    public Assinatura cancelar(Long id) {
        Assinatura assinatura = assinaturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Assinatura não encontrada"));

        if (assinatura.getStatus() == StatusAssinatura.CANCELADA) {
            throw new RegraDeNegocioException("A assinatura já se encontra cancelada.");
        }

        assinatura.setStatus(StatusAssinatura.valueOf(String.valueOf(StatusAssinatura.CANCELADA)));
        pagamentoService.cancelarPagamentosPendentesPorAssinatura(assinatura);

        return assinaturaRepository.save(assinatura);
    }
}