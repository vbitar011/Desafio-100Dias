package com.victor.saas_pagamentos.infra;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> tratarErro404() {
        //Intercepta o erro de "Não Encontrado" e devolve o Status 404 customizado
        return ResponseEntity.status(404).body("Erro: O registro solicitado não foi encontrado no banco de dados.");
    }
}