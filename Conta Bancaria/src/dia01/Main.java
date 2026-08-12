package dia01;

import dia02.ContaPoupanca;
import dia03.ContaCorrente;
import dia04.Banco;
import dia05.SaldoInsuficienteException;

import java.util.List;
import java.util.Scanner;

public class Main{
  public static void main(String[] args){

    Scanner teclado = new Scanner(System.in);
    Banco meuBanco = new Banco();
    ContaCorrente minhaConta = new ContaCorrente("Victor", " 12345-6", 400.0);
    meuBanco.adicionarConta(minhaConta);

    int opcao = -1;
    while (opcao != 0) {
      System.out.println("\n=== CAIXA ELETRÔNICO ===");
      System.out.println("1 - Consultar Saldo");
      System.out.println("2 - Depositar");
      System.out.println("3 - Sacar");
      System.out.println("4 - Relatório Geral do Banco");
      System.out.println("5 - Ver Patrimônio Total do Banco (Stream)");
      System.out.println("6 - Filtrar Contas com Saldo Máximo (Stream)");
      System.out.println("0 - Sair");
      System.out.print("Escolha uma opção: ");

      opcao = teclado.nextInt();

      switch (opcao) {
        case 1:
          minhaConta.exibirSaldo();
          break;

        case 2:
          System.out.print("Digite o valor para depósito: R$ ");
          double valorDeposito = teclado.nextDouble();
          minhaConta.depositar(valorDeposito);
          break;

        case 3:
          System.out.print("Digite o valor para saque: R$ ");
          double valorSaque = teclado.nextDouble();
          try {
            minhaConta.sacar(valorSaque);
            System.out.println("Por favor, retire seu dinheiro na boca do caixa.");
          } catch (SaldoInsuficienteException e){
            System.out.println("Operação cancelada pelo banco: " + e.getMessage());
          }
          break;

        case 4:
          meuBanco.exibirRelatorio();
          break;

        case 5:
          double total = meuBanco.calcularPatrimonioTotal();
          System.out.println("\nPatrimônio Total do Banco: R$ " + total);
          break;

        case 6:
          System.out.print("Digite o saldo máximo para filtro: R$ ");
          double limite = teclado.nextDouble();
          List<dia01.Conta> filtradas = meuBanco.buscarContasComSaldoAte(limite);

          System.out.println("\n=== CONTAS ENCONTRADAS (Até R$ " + limite + ") ===");
          filtradas.forEach(c -> System.out.println("Titular: " + c.getTitular() + "| Saldo: R$ " + c.getSaldo()));
          break;

        case 0:
          System.out.println("Desligando o Caixa Eletrônico. Volte sempre!");
          break;

        default:
          //Se o usuário digitar um número fora das opções
          System.out.println("Opção inválida! Tente novamente.");
      }
    }

    teclado.close();
  }
}

