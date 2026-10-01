package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.PlanoRequestDTO;
import com.victor.saas_pagamentos.model.Plano;
import com.victor.saas_pagamentos.repository.PlanoRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanoController {

    private final PlanoRepository repository;

    //Injetando dependência via construtor
    public PlanoController(PlanoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Plano criarPlano(@Valid @RequestBody PlanoRequestDTO dto) {
        Plano novoPlano = new Plano();
        novoPlano.setNome(dto.nome());
        novoPlano.setValor(dto.valor());

        return repository.save(novoPlano);
    }

    @GetMapping
    public Page<Plano> listarPlanos(@PageableDefault(size = 10, page = 0, sort = "nome") Pageable paginacao){
        //O método findAll() faz um "SELECT * FROM PLANO" e retorna a lista
        return repository.findAll(paginacao);
    }
}
