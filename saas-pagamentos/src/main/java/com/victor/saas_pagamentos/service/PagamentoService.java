package com.victor.saas_pagamentos.service;

import com.victor.saas_pagamentos.dto.PagamentoRequestDTO;
import com.victor.saas_pagamentos.infra.RecursoNaoEncontradoException;
import com.victor.saas_pagamentos.infra.RegraDeNegocioException;
import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.model.Pagamento;
import com.victor.saas_pagamentos.model.StatusAssinatura;
import com.victor.saas_pagamentos.model.StatusPagamento;
import com.victor.saas_pagamentos.repository.AssinaturaRepository;
import com.victor.saas_pagamentos.repository.PagamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AssinaturaRepository assinaturaRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, AssinaturaRepository assinaturaRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.assinaturaRepository = assinaturaRepository;
    }

    public Pagamento gerarPagamento(PagamentoRequestDTO dto) {
        //1. Procura a assinatura
        Assinatura assinatura = assinaturaRepository.findById(dto.assinaturaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assinatura não encontrada."));

        //2. Opcional de negócio: Não gera cobrança se a assinatura estiver cancelada
        if ("CANCELADA".equals(assinatura.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível gerar cobranças para uma assinatura cancelada.");
        }

        //3. Monta o pagamento automaticamente
        Pagamento pagamento = new Pagamento();
        pagamento.setAssinatura(assinatura);
        pagamento.setValor(assinatura.getPlano().getValor()); //Copia o valor do plano atual
        pagamento.setDataVencimento(LocalDate.now().plusDays(5));
        pagamento.setStatus(StatusPagamento.PENDENTE);
        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public Pagamento confirmarPagamento(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado"));

        if (pagamento.getStatus() == StatusPagamento.PAGO) {
            throw new RegraDeNegocioException("Pagamento já liquidado");
        }
        if (pagamento.getStatus() == StatusPagamento.CANCELADO) {
            throw new RegraDeNegocioException("Cobrança cancelada não pode ser paga");
        }
        if (pagamento.getAssinatura() != null && pagamento.getAssinatura().getStatus() == StatusAssinatura.CANCELADA) {
            throw new RegraDeNegocioException("Assinatura cancelada, pagamento rejeitado");
        }

        pagamento.setStatus(StatusPagamento.PAGO);
        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public void cancelarPagamentosPendentesPorAssinatura(Assinatura assinatura) {
        List<Pagamento> pendentes = pagamentoRepository.findByAssinaturaAndStatus(assinatura, StatusPagamento.PENDENTE);
        for (Pagamento p : pendentes) {
            p.setStatus(StatusPagamento.CANCELADO);
            pagamentoRepository.save(p);
        }
    }
}