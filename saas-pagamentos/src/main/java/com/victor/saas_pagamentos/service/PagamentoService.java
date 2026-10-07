package com.victor.saas_pagamentos.service;

import com.victor.saas_pagamentos.dto.PagamentoRequestDTO;
import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.model.Pagamento;
import com.victor.saas_pagamentos.repository.AssinaturaRepository;
import com.victor.saas_pagamentos.repository.PagamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;

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
        pagamento.setDataVencimento(LocalDate.now().plusDays(5)); //Vence daqui a 5 dias
        pagamento.setStatus("PENDENTE");

        return pagamentoRepository.save(pagamento);
    }

    public Pagamento confirmarPagamento(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagamento não encontrado."));

        if ("PAGO".equals(pagamento.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este pagamento já se encontra liquidado.");
        }

        pagamento.setStatus("PAGO");
        return pagamentoRepository.save(pagamento);
    }
}