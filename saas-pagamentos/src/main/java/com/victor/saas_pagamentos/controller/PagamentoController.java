package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.PagamentoResponseDTO;
import com.victor.saas_pagamentos.infra.RegraDeNegocioException;
import com.victor.saas_pagamentos.model.Pagamento;
import com.victor.saas_pagamentos.repository.PagamentoRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import com.victor.saas_pagamentos.service.PagamentoService;
import com.victor.saas_pagamentos.dto.PagamentoRequestDTO;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoRepository repository;
    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoRepository repository, PagamentoService pagamentoService) {
        this.repository = repository;
        this.pagamentoService = pagamentoService;
    }

    //Rota 1: Lista todos os pagamentos gerados
    @GetMapping
    public Page<PagamentoResponseDTO> listarPagamentos(@PageableDefault(size = 10, page = 0, sort = "dataVencimento") Pageable paginacao) {

        return repository.findAll(paginacao)
                .map(pagamento -> new PagamentoResponseDTO(
                        pagamento.getId(),
                        pagamento.getAssinatura().getId(),
                        pagamento.getValor(),
                        pagamento.getDataVencimento(),
                        pagamento.getStatus()
                ));
    }

    @PostMapping
    public Pagamento criarPagamento(@Valid @RequestBody PagamentoRequestDTO dto) {
        return pagamentoService.gerarPagamento(dto);
    }

    //Rota 2: Simula o pagamento da cobrança
    @PatchMapping("/{id}/pagar")
    public Pagamento pagarFatura(@PathVariable Long id) {
        return pagamentoService.confirmarPagamento(id);
    }
}