package com.victor.saas_pagamentos.controller;

import com.victor.saas_pagamentos.dto.AssinaturaResponseDTO;
import com.victor.saas_pagamentos.model.Assinatura;
import com.victor.saas_pagamentos.model.Cliente;
import com.victor.saas_pagamentos.model.Pagamento;
import com.victor.saas_pagamentos.model.Plano;
import com.victor.saas_pagamentos.repository.AssinaturaRepository;
import com.victor.saas_pagamentos.repository.ClienteRepository;
import com.victor.saas_pagamentos.repository.PagamentoRepository;
import com.victor.saas_pagamentos.repository.PlanoRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/assinaturas")
public class AssinaturaController {

    private final AssinaturaRepository repository;
    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;
    private final PagamentoRepository pagamentoRepository;

    //O Spring Boot vai injetar os 4 repositórios automaticamente aqui
    public AssinaturaController(AssinaturaRepository repository,
                                ClienteRepository clienteRepository,
                                PlanoRepository planoRepository,
                                PagamentoRepository pagamentoRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.planoRepository = planoRepository;
        this.pagamentoRepository = pagamentoRepository;
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

        //--- INÍCIO DA AUTOMAÇÃO DE PAGAMENTO ---
        //Cria um pagamento pendente para daqui a 5 dias, com o valor exato do plano
        Pagamento primeiroPagamento = new Pagamento(
                salva,
                planoReal.getValor(),
                LocalDate.now().plusDays(5)
        );
        pagamentoRepository.save(primeiroPagamento);

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

    @PatchMapping("/{id}/cancelar")
    public AssinaturaResponseDTO cancelarAssinatura(@PathVariable Long id) {
        //1. Busca a assinatura no banco de dados pelo ID passado na URL e cancela a assinatura principal
        Assinatura assinatura = repository.findById(id).orElseThrow();
        assinatura.setStatus("CANCELADA");
        Assinatura salva = repository.save(assinatura);

        //2. Busca todas as cobranças que ainda estão pendentes para essa assinatura
        List<Pagamento> pagamentosPendentes = pagamentoRepository.findByAssinaturaIdAndStatus(id, "PENDENTE");

        //3. Altera o status de cada uma(Soft Delete)
        for (Pagamento pagamento : pagamentosPendentes) {
            pagamento.setStatus("CANCELADO");
        }

        //4. Guarda as alterações de todos os pagamentos de uma só vez
        pagamentoRepository.saveAll(pagamentosPendentes);

        //5. Devolve o DTO atualizado
        return new AssinaturaResponseDTO(
                salva.getId(),
                salva.getCliente().getNome(),
                salva.getPlano().getNome(),
                salva.getDataInicio(),
                salva.getStatus()
        );
    }
}