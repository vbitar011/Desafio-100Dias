package com.victor.saas_pagamentos.service;

import com.victor.saas_pagamentos.infra.RecursoNaoEncontradoException;
import com.victor.saas_pagamentos.infra.RegraDeNegocioException;
import com.victor.saas_pagamentos.infra.exception.AcessoNegadoException;
import com.victor.saas_pagamentos.infra.security.UsuarioLogado;
import com.victor.saas_pagamentos.model.*;
import com.victor.saas_pagamentos.repository.AssinaturaRepository;
import com.victor.saas_pagamentos.repository.PagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PagamentoService {
    private final PagamentoRepository pagamentoRepository;
    private final AssinaturaRepository assinaturaRepository;
    private final UsuarioLogado usuarioLogado;

    public PagamentoService(PagamentoRepository pagamentoRepository, AssinaturaRepository assinaturaRepository, UsuarioLogado usuarioLogado) {
        this.pagamentoRepository = pagamentoRepository;
        this.assinaturaRepository = assinaturaRepository;
        this.usuarioLogado = usuarioLogado;
    }

    @Transactional
    public Pagamento gerarPagamento(Long assinaturaId) {
        //Bloqueio Pessimista na leitura da assinatura
        Assinatura assinatura = assinaturaRepository.findByIdParaAtualizar(assinaturaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Assinatura não encontrada."));

        if (assinatura.getStatus() == StatusAssinatura.CANCELADA) {
            throw new RegraDeNegocioException("Não é possível gerar cobrança para uma assinatura cancelada.");
        }

        if (pagamentoRepository.existsByAssinaturaIdAndStatus(assinaturaId, StatusPagamento.PENDENTE)) {
            throw new RegraDeNegocioException("Já existe um pagamento pendente para esta assinatura.");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setAssinatura(assinatura);
        pagamento.setValor(assinatura.getPlano().getValor());
        pagamento.setDataVencimento(LocalDate.now().plusDays(5));
        pagamento.setStatus(StatusPagamento.PENDENTE);

        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public Pagamento pagar(Long pagamentoId) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado."));

        User user = usuarioLogado.get();
        if (user.getRole() == UserRole.USER) {
            if (user.getCliente() == null || !pagamento.getAssinatura().getCliente().getId().equals(user.getCliente().getId())) {
                throw new AcessoNegadoException("Você não tem permissão para pagar esta cobrança.");
            }
        }

        if (pagamento.getStatus() != StatusPagamento.PENDENTE) {
            throw new RegraDeNegocioException("Apenas pagamentos pendentes podem ser pagos.");
        }

        pagamento.setStatus(StatusPagamento.PAGO);
        return pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> listar() {
        User user = usuarioLogado.get();
        if (user.getRole() == UserRole.ADMIN) {
            return pagamentoRepository.findAll();
        } else {
            if (user.getCliente() == null) return List.of();
            return pagamentoRepository.findByAssinaturaClienteId(user.getCliente().getId());
        }
    }

    @Transactional
    public void cancelarPagamentosPendentes(Assinatura assinatura) {
        List<Pagamento> pendentes = pagamentoRepository.findByAssinaturaAndStatus(assinatura, StatusPagamento.PENDENTE);
        for(Pagamento p : pendentes) {
            p.setStatus(StatusPagamento.CANCELADO);
            pagamentoRepository.save(p);
        }
    }
}