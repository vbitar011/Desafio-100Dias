package com.victor.saas_pagamentos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Entity
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne //Vários pagamentos pertencem a uma mesma assinatura
    @JoinColumn(name = "assinatura_id")
    private Assinatura assinatura;

    @NotNull(message = "O valor do pagamento é obrigatório.")
    @Positive(message = "O valor da cobrança não pode ser negativo ou zero.")
    private Double valor;

    @NotNull(message = "A data de vencimento é obrigatória.")
    @FutureOrPresent(message = "A data de vencimento não pode estar no passado.")

    private LocalDate dataVencimento;
    private String status = "PENDENTE"; //Pode ser PENDENTE, PAGO ou CANCELADO

    public Pagamento() {}

    public Pagamento(Assinatura assinatura, Double valor, LocalDate dataVencimento) {
        this.assinatura = assinatura;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
    }

    //Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Assinatura getAssinatura() { return assinatura; }
    public void setAssinatura(Assinatura assinatura) { this.assinatura = assinatura; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}