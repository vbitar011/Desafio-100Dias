package com.victor.saas_pagamentos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //Vários pagamentos pertencem a uma mesma assinatura
    @JoinColumn(name = "assinatura_id")
    private Assinatura assinatura;

    @NotNull(message = "O valor do pagamento é obrigatório.")
    @Positive(message = "O valor da cobrança não pode ser negativo ou zero.")
    @Column(precision = 12, scale = 2)
    private BigDecimal valor;

    @Version
    private Long version; //Bloqueio contra pagamentos duplicados simultâneos

    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    public Pagamento() {}

    public Pagamento(Assinatura assinatura, @NotNull @Positive BigDecimal valor, LocalDate dataVencimento) {
        this.assinatura = assinatura;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
    }

    //Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Assinatura getAssinatura() { return assinatura; }
    public void setAssinatura(Assinatura assinatura) { this.assinatura = assinatura; }

    public @NotNull(message = "O valor do pagamento é obrigatório.") @Positive(message = "O valor da cobrança não pode ser negativo ou zero.") BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }
}