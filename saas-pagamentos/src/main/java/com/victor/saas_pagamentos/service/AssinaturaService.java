package com.victor.saas_pagamentos.service;

import com.victor.saas_pagamentos.dto.AssinaturaRequestDTO;
import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.model.Cliente;
import com.victor.saas_pagamentos.model.Plano;
import com.victor.saas_pagamentos.repository.AssinaturaRepository;
import com.victor.saas_pagamentos.repository.ClienteRepository;
import com.victor.saas_pagamentos.repository.PlanoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class AssinaturaService {

    private final AssinaturaRepository assinaturaRepository;
    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;

    public AssinaturaService(AssinaturaRepository assinaturaRepository, ClienteRepository clienteRepository, PlanoRepository planoRepository) {
        this.assinaturaRepository = assinaturaRepository;
        this.clienteRepository = clienteRepository;
        this.planoRepository = planoRepository;
    }

    public Assinatura criarAssinatura(AssinaturaRequestDTO dto) {
        //1. Valida se o Cliente existe
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado com o ID fornecido."));

        //2. Valida se o Plano existe
        Plano plano = planoRepository.findById(dto.planoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado com o ID fornecido."));

        //3. Monta a Assinatura com as regras de negócio
        Assinatura novaAssinatura = new Assinatura();
        novaAssinatura.setCliente(cliente);
        novaAssinatura.setPlano(plano);
        novaAssinatura.setDataInicio(LocalDate.now()); // Data automática do servidor
        novaAssinatura.setStatus("ATIVA"); //Status inicial padrão

        //4. Guarda na base de dados
        return assinaturaRepository.save(novaAssinatura);
    }
}