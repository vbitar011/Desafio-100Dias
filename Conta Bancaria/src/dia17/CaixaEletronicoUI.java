package dia17;

import dia01.Conta;
import dia03.ContaCorrente;
import dia04.Banco;
import dia05.SaldoInsuficienteException;
import dia22.CaixaEletronicoService;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;
import java.util.List;

public class CaixaEletronicoUI {

    private Banco meuBanco;
    private Scanner teclado;
    private CaixaEletronicoService servico;
    
    protected NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));


    public CaixaEletronicoUI(Banco banco, Scanner teclado){
        this.meuBanco = banco;
        this.teclado = teclado;
        this.servico = new CaixaEletronicoService(meuBanco, teclado);
    }

    public void iniciarMenu(){

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== CAIXA ELETRÔNICO ===");
            System.out.println("1 - Consultar Saldo");
            System.out.println("2 - Depositar");
            System.out.println("3 - Sacar");
            System.out.println("4 - Relatório Geral do Banco");
            System.out.println("5 - Criar Nova Conta");
            System.out.println("6 - Tirar Extrato");
            System.out.println("7 - Cobrar Impostos");
            System.out.println("8 - Encerrar Conta");
            System.out.println("9 - Transferência");
            System.out.println("0 - Sair");
            opcao = dia16.TecladoUtil.lerInteiro(teclado, "Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    servico.consultarSaldo();
                    break;

                case 2:
                    servico.realizarDeposito();
                    break;

                case 3:
                    servico.realizarSaque();
                    break;

                case 4:
                    meuBanco.exibirRelatorio();
                    break;

                case 5:
                    servico.criarNovaConta();
                    break;

                case 6:
                    servico.exibirExtrato();
                    break;


                case 7:
                    servico.cobrarImpostos();
                    break;

                case 8:
                    servico.encerrarConta();
                    break;

                case 9:
                    servico.realizarTransferencia();
                    break;


                    case 0:
                        System.out.println("Desligando o Caixa Eletrônico. Volte sempre!");
                    break;

                default:
                    //Se o usuário digitar um número fora das opções
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }
}
