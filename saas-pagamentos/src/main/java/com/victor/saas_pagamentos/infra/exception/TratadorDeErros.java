package com.victor.saas_pagamentos.infra.exception;

import com.victor.saas_pagamentos.dto.ErroValidacaoDTO;
import com.victor.saas_pagamentos.infra.RecursoNaoEncontradoException;
import com.victor.saas_pagamentos.infra.RegraDeNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> tratarErro404(RecursoNaoEncontradoException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResponseDTO(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResponseDTO> tratarErroDeNegocio(RegraDeNegocioException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> tratarErroValidacao(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ErroValidacaoDTO> erros = ex.getBindingResult().getFieldErrors().stream().map(e -> new ErroValidacaoDTO(e.getField(), e.getDefaultMessage())).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Erro de Validação", "Um ou mais campos estão inválidos", req.getRequestURI(), erros));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponseDTO> tratarConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        List<ErroValidacaoDTO> erros = ex.getConstraintViolations().stream().map(v -> new ErroValidacaoDTO(v.getPropertyPath().toString(), v.getMessage())).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Erro de Validação", "Falha de validação de dados.", req.getRequestURI(), erros));
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErroResponseDTO> tratarAcessoNegado(AcessoNegadoException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErroResponseDTO(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), "Forbidden", ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ErroResponseDTO> tratarErroAutenticacao(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErroResponseDTO(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "Credenciais inválidas", req.getRequestURI()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponseDTO> tratarErroIntegridade(DataIntegrityViolationException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroResponseDTO(LocalDateTime.now(), HttpStatus.CONFLICT.value(), "Conflict", "Registro duplicado ou violação de integridade", req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> tratarErroGenerico(Exception ex, HttpServletRequest req) {
        ex.printStackTrace(); // Log interno
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErroResponseDTO(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Ocorreu um erro interno inesperado.", req.getRequestURI()));
    }
}