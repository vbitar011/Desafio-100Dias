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

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<String> tratarRegraDeNegocio(RegraDeNegocioException ex) {
        //Intercepta a exceção customizada e devolve Status 400 com a mensagem específica
        return ResponseEntity.status(400).body("Erro de Validação: " + ex.getMessage());
    }
}