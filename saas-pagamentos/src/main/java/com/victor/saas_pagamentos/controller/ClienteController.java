package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.ClienteResponseDTO;
import com.victor.saas_pagamentos.model.Cliente;
import com.victor.saas_pagamentos.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Cliente criarCliente(@Valid @RequestBody Cliente novoCliente) {
        return repository.save(novoCliente);
    }

    @GetMapping
    public Page<ClienteResponseDTO> listarClientes(@PageableDefault(size = 10, page = 0, sort = "nome") Pageable paginacao) {

        //O método map() funciona bem com o objeto Page
        return repository.findAll(paginacao)
                .map(cliente -> new ClienteResponseDTO(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getEmail()
                ));
    }
}