package com.victor.saas_pagamentos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping("/status")
    public String checarStatus() {
        return "✅ API de Pagamentos ONLINE e operando no Spring Boot!";
    }

    @GetMapping("/plano-teste")
    public PlanoTeste checarPlano() {
        return new PlanoTeste("Pro", 49.90);
    }

    record PlanoTeste(String nome, double valor) {}
}