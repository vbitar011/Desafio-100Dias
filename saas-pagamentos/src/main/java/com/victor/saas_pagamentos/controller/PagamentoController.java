package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.PagamentoResponseDTO;
import com.victor.saas_pagamentos.model.Pagamento;
import com.victor.saas_pagamentos.repository.PagamentoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoRepository repository;

    public PagamentoController(PagamentoRepository repository) {
        this.repository = repository;
    }

    //Rota 1: Lista todos os pagamentos gerados
    @GetMapping
    public List<PagamentoResponseDTO> listarPagamentos() {
        return repository.findAll().stream()
                .map(pagamento -> new PagamentoResponseDTO(
                        pagamento.getId(),
                        pagamento.getAssinatura().getId(),
                        pagamento.getValor(),
                        pagamento.getDataVencimento(),
                        pagamento.getStatus()
                ))
                .toList();
    }

    //Rota 2: Simula o pagamento da cobrança
    @PatchMapping("/{id}/pagar")
    public PagamentoResponseDTO darBaixaNoPagamento(@PathVariable Long id) {
        //Busca o pagamento no banco (se não achar, o TratadorDeErros devolve 404)
        Pagamento pagamento = repository.findById(id).orElseThrow();

        //Muda o status simulando que o cartão de crédito aprovou a compra
        pagamento.setStatus("PAGO");

        //Salva a alteração
        Pagamento salvo = repository.save(pagamento);

        //Devolve o DTO atualizado
        return new PagamentoResponseDTO(
                salvo.getId(),
                salvo.getAssinatura().getId(),
                salvo.getValor(),
                salvo.getDataVencimento(),
                salvo.getStatus()
        );
    }
}