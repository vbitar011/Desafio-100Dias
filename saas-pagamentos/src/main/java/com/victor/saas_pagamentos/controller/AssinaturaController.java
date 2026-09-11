package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.AssinaturaResponseDTO;
import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.model.Cliente;
import com.victor.saas_pagamentos.model.Plano;
import com.victor.saas_pagamentos.repository.AssinaturaRepository;
import com.victor.saas_pagamentos.repository.ClienteRepository;
import com.victor.saas_pagamentos.repository.PlanoRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assinaturas")
public class AssinaturaController {

    private final AssinaturaRepository repository;
    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;

    // O Spring Boot vai injetar os 3 repositórios automaticamente aqui
    public AssinaturaController(AssinaturaRepository repository,
                                ClienteRepository clienteRepository,
                                PlanoRepository planoRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.planoRepository = planoRepository;
    }

    @PostMapping
    public AssinaturaResponseDTO criarAssinatura(@Valid @RequestBody Assinatura novaAssinatura) {

        //1. Busca as entidades REAIS no banco de dados usando os IDs da "casca"
        Cliente clienteReal = clienteRepository.findById(novaAssinatura.getCliente().getId()).orElseThrow();
        Plano planoReal = planoRepository.findById(novaAssinatura.getPlano().getId()).orElseThrow();

        //2. Troca as "cascas" vazias pelos objetos completos
        novaAssinatura.setCliente(clienteReal);
        novaAssinatura.setPlano(planoReal);

        //3. Agora salva. O objeto 'salva' terá todos os dados preenchidos.
        Assinatura salva = repository.save(novaAssinatura);

        //4. Mapea para o DTO
        return new AssinaturaResponseDTO(
                salva.getId(),
                salva.getCliente().getNome(),
                salva.getPlano().getNome(),
                salva.getDataInicio(),
                salva.getStatus()
        );
    }

    @GetMapping
    public List<AssinaturaResponseDTO> listarAssinaturas() {
        return repository.findAll().stream()
                .map(assinatura -> new AssinaturaResponseDTO(
                        assinatura.getId(),
                        assinatura.getCliente().getNome(),
                        assinatura.getPlano().getNome(),
                        assinatura.getDataInicio(),
                        assinatura.getStatus()
                ))
                .toList();
    }
}