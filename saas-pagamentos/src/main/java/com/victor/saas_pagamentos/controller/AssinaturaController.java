package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.AssinaturaRequestDTO;
import com.victor.saas_pagamentos.dto.AssinaturaResponseDTO;
import com.victor.saas_pagamentos.infra.RecursoNaoEncontradoException;
import com.victor.saas_pagamentos.model.*;
import com.victor.saas_pagamentos.repository.AssinaturaRepository;
import com.victor.saas_pagamentos.repository.ClienteRepository;
import com.victor.saas_pagamentos.repository.PagamentoRepository;
import com.victor.saas_pagamentos.repository.PlanoRepository;
import com.victor.saas_pagamentos.service.AssinaturaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/assinaturas")
public class AssinaturaController {

    private final AssinaturaRepository repository;
    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final AssinaturaService assinaturaService;

    //O Spring Boot vai injetar os 4 repositórios automaticamente aqui
    public AssinaturaController(AssinaturaRepository repository,
                                ClienteRepository clienteRepository,
                                PlanoRepository planoRepository,
                                PagamentoRepository pagamentoRepository,
                                AssinaturaService assinaturaService) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.planoRepository = planoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.assinaturaService = assinaturaService;
    }

    @PostMapping
    public AssinaturaResponseDTO criarAssinatura(@Valid @RequestBody AssinaturaRequestDTO dto) {

        //1. Busca os objetos reais na base de dados usando os IDs do DTO
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));

        Plano plano = planoRepository.findById(dto.planoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado"));

        //2. Instancia a Entidade e popula com os objetos
        Assinatura novaAssinatura = new Assinatura();
        novaAssinatura.setCliente(cliente);
        novaAssinatura.setPlano(plano);

        //3. O Serviço faz o trabalho pesado (validações e transação)
        var assinaturaSalva = assinaturaService.criarAssinatura(novaAssinatura);

        // 4. Mapeia a Entidade salva de volta para o ResponseDTO
        return new AssinaturaResponseDTO(
                assinaturaSalva.getId(),
                assinaturaSalva.getCliente().getId(),
                assinaturaSalva.getPlano().getId(),
                assinaturaSalva.getDataInicio(),
                assinaturaSalva.getStatus()
        );
    }

    @GetMapping
    public Page<AssinaturaResponseDTO> listarAssinaturas(@PageableDefault(size = 10, page = 0, sort = "dataInicio") Pageable paginacao){
        return repository.findAll(paginacao)
                .map(assinatura -> new AssinaturaResponseDTO(
                        assinatura.getId(),
                        assinatura.getCliente().getId(),
                        assinatura.getPlano().getId(),
                        assinatura.getDataInicio(),
                        assinatura.getStatus()
                ));
    }

    @PatchMapping("/{id}/cancelar")
    public AssinaturaResponseDTO cancelarAssinatura(@PathVariable Long id) {
        //1. Busca a assinatura no banco de dados pelo ID passado na URL e cancela a assinatura principal
        Assinatura assinatura = repository.findById(id).orElseThrow();
        assinatura.setStatus(StatusAssinatura.valueOf("CANCELADA"));
        Assinatura salva = repository.save(assinatura);

        //2. Busca todas as cobranças que ainda estão pendentes para essa assinatura
        List<Pagamento> pagamentosPendentes = pagamentoRepository.findByAssinaturaIdAndStatus(id, "PENDENTE");

        //3. Altera o status de cada uma(Soft Delete)
        for (Pagamento pagamento : pagamentosPendentes) {
            pagamento.setStatus(StatusPagamento.valueOf("CANCELADO"));
        }

        //4. Guarda as alterações de todos os pagamentos de uma só vez
        pagamentoRepository.saveAll(pagamentosPendentes);

        //5. Devolve o DTO atualizado
        return new AssinaturaResponseDTO(
                salva.getId(),
                salva.getCliente().getId(),
                salva.getPlano().getId(),
                salva.getDataInicio(),
                salva.getStatus()
        );
    }
}