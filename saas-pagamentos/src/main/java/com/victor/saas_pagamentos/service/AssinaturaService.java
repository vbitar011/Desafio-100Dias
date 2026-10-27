package com.victor.saas_pagamentos.service;

import com.victor.saas_pagamentos.dto.AssinaturaRequestDTO;
import com.victor.saas_pagamentos.infra.RecursoNaoEncontradoException;
import com.victor.saas_pagamentos.infra.exception.AcessoNegadoException;
import com.victor.saas_pagamentos.infra.security.UsuarioLogado;
import com.victor.saas_pagamentos.model.*;
import com.victor.saas_pagamentos.repository.AssinaturaRepository;
import com.victor.saas_pagamentos.repository.ClienteRepository;
import com.victor.saas_pagamentos.repository.PlanoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssinaturaService {

    private final AssinaturaRepository assinaturaRepository;
    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;
    private final PagamentoService pagamentoService;
    private final UsuarioLogado usuarioLogado;

    public AssinaturaService(AssinaturaRepository assinaturaRepository, ClienteRepository clienteRepository, PlanoRepository planoRepository, PagamentoService pagamentoService, UsuarioLogado usuarioLogado) {
        this.assinaturaRepository = assinaturaRepository;
        this.clienteRepository = clienteRepository;
        this.planoRepository = planoRepository;
        this.pagamentoService = pagamentoService;
        this.usuarioLogado = usuarioLogado;
    }

    @Transactional
    public Assinatura criar(AssinaturaRequestDTO dto) {
        User user = usuarioLogado.get();
        Long clienteId = dto.clienteId();

        if (user.getRole() == UserRole.USER) {
            if (user.getCliente() == null) {
                throw new AcessoNegadoException("Usuário não possui cliente vinculado.");
            }
            clienteId = user.getCliente().getId(); //Ignora payload e restringe ao dono
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
        Plano plano = planoRepository.findById(dto.planoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado."));

        Assinatura assinatura = new Assinatura();
        assinatura.setCliente(cliente);
        assinatura.setPlano(plano);
        assinatura.setStatus(StatusAssinatura.ATIVA);
        assinatura.setDataInicio(LocalDate.now());

        return assinaturaRepository.save(assinatura);
    }

    @Transactional
    public Assinatura cancelar(Long id) {
        Assinatura assinatura = assinaturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Assinatura não encontrada."));

        User user = usuarioLogado.get();
        if (user.getRole() == UserRole.USER) {
            if (user.getCliente() == null || !assinatura.getCliente().getId().equals(user.getCliente().getId())) {
                throw new AcessoNegadoException("Você não tem permissão para cancelar esta assinatura.");
            }
        }

        assinatura.setStatus(StatusAssinatura.CANCELADA);
        pagamentoService.cancelarPagamentosPendentes(assinatura);

        return assinaturaRepository.save(assinatura);
    }

    public List<Assinatura> listar() {
        User user = usuarioLogado.get();
        if (user.getRole() == UserRole.ADMIN) {
            return assinaturaRepository.findAll();
        } else {
            if (user.getCliente() == null) return List.of();
            return assinaturaRepository.findByClienteId(user.getCliente().getId());
        }
    }
}