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

    //Método auxiliar privado para não repetirmos código
    private PagamentoResponseDTO mapearParaDTO(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getValor(),
                pagamento.getDataVencimento(),
                pagamento.getStatus(),
                pagamento.getAssinatura().getId()
        );
    }

    //Rota 1: Lista todos os pagamentos gerados
    @GetMapping
    public Page<PagamentoResponseDTO> listarPagamentos(@PageableDefault(size = 10, page = 0, sort = "dataVencimento") Pageable paginacao) {
        //busca os dados à base de dados e usa o método auxiliar para mapear cada um deles
        return repository.findAll(paginacao)
                .map(this::mapearParaDTO);
    }

    @PostMapping
    public PagamentoResponseDTO criarPagamento(@Valid @RequestBody PagamentoRequestDTO dto) {
        var pagamento = pagamentoService.gerarPagamento(dto);
        return mapearParaDTO(pagamento);
    }

    //Rota 2: Simula o pagamento da cobrança
    @PatchMapping("/{id}/pagar")
    public PagamentoResponseDTO pagarFatura(@PathVariable Long id) {
        var pagamento = pagamentoService.confirmarPagamento(id);
        return mapearParaDTO(pagamento);
    }
}