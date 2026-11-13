package com.victor.saas_pagamentos;

import com.victor.saas_pagamentos.infra.RegraDeNegocioException;
import com.victor.saas_pagamentos.model.*;
import com.victor.saas_pagamentos.repository.AssinaturaRepository;
import com.victor.saas_pagamentos.repository.PagamentoRepository;
import com.victor.saas_pagamentos.infra.security.UsuarioLogado;
import com.victor.saas_pagamentos.service.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock private PagamentoRepository pagamentoRepository;
    @Mock private AssinaturaRepository assinaturaRepository;
    @Mock private UsuarioLogado usuarioLogado;
    @InjectMocks private PagamentoService pagamentoService;

    private Assinatura assinatura;
    private Pagamento pagamento;
    private User admin;

    @BeforeEach
    void setup() {
        Plano plano = new Plano(); plano.setValor(new BigDecimal("99.90"));
        assinatura = new Assinatura(); assinatura.setId(1L); assinatura.setPlano(plano); assinatura.setStatus(StatusAssinatura.ATIVA);
        pagamento = new Pagamento(); pagamento.setId(1L); pagamento.setAssinatura(assinatura); pagamento.setStatus(StatusPagamento.PENDENTE);

        admin = new User(); admin.setRole(UserRole.ADMIN);
    }

    @Test
    void deveRejeitarGeracaoSeAssinaturaCancelada() {
        assinatura.setStatus(StatusAssinatura.CANCELADA);
        when(assinaturaRepository.findByIdParaAtualizar(1L)).thenReturn(Optional.of(assinatura));

        assertThrows(RegraDeNegocioException.class, () -> pagamentoService.gerarPagamento(1L));
    }

    @Test
    void deveRejeitarGeracaoSeJaExistePendente() {
        when(assinaturaRepository.findByIdParaAtualizar(1L)).thenReturn(Optional.of(assinatura));
        when(pagamentoRepository.existsByAssinaturaIdAndStatus(1L, StatusPagamento.PENDENTE)).thenReturn(true);

        assertThrows(RegraDeNegocioException.class, () -> pagamentoService.gerarPagamento(1L));
    }

    @Test
    void devePermitirPagamentoMesmoVencido() {
        pagamento.setDataVencimento(LocalDate.now().minusDays(10)); //Vencida deve funcionar

        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pagamento));
        when(usuarioLogado.get()).thenReturn(admin);

        //Adiciona esta linha para ensinar o Mockito a devolver o pagamento ao gravar!
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        Pagamento pago = pagamentoService.pagar(1L);
        assertEquals(StatusPagamento.PAGO, pago.getStatus());
    }
}