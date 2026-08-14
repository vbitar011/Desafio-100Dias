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

    //Carrega automaticamente as contas salvas anteriormente
    List<Conta> contasSalvas = dia10.GerenciadorDeArquivos.carregarContas();
    contasSalvas.forEach(meuBanco :: adicionarConta);

    int opcao = -1;
    while (opcao != 0) {
      System.out.println("\n=== CAIXA ELETRÔNICO ===");
      System.out.println("1 - Consultar Saldo");
      System.out.println("2 - Depositar");
      System.out.println("3 - Sacar");
      System.out.println("4 - Relatório Geral do Banco");
      System.out.println("5 - Criar Nova Conta");
      System.out.println("6 - Ver Patrimônio Total do Banco (Stream)");
      System.out.println("7 - Filtrar Contas com Saldo Máximo (Stream)");
      System.out.println("8 - Tirar Extrato");
      System.out.println("9 - Cobrar Impostos");
      System.out.println("0 - Sair");
      System.out.print("Escolha uma opção: ");

      opcao = teclado.nextInt();

      switch (opcao) {
        case 1:
          teclado.nextLine();
          System.out.print("Digite o número da conta: ");
          String numSaldo = teclado.nextLine();

          dia01.Conta contaSaldo = meuBanco.buscarContaPorNumero(numSaldo);

          if(contaSaldo != null){
            contaSaldo.exibirSaldo();
          } else {
            System.out.println("Conta não encontrada!");
          }
          break;

        case 2:
          teclado.nextLine();
          System.out.print("Digite o número da conta: ");
          String numDeposito = teclado.nextLine();

          dia01.Conta contaDeposito = meuBanco.buscarContaPorNumero(numDeposito);

          if(contaDeposito != null){
            System.out.print("Digite o valor para depósito: R$ ");
            double valorDeposito = teclado.nextDouble();
            contaDeposito.depositar(valorDeposito);
          } else {
            System.out.println("Conta não encontrada!");
          }
          break;

        case 3:
          teclado.nextLine();
          System.out.print("Digite o numero da conta: ");
          String numSaque = teclado.nextLine();

          dia01.Conta contaSaque = meuBanco.buscarContaPorNumero(numSaque);

          if (contaSaque != null){
            System.out.print("Digite o valor para saque: R$ ");
            double valorSaque = teclado.nextDouble();
            try {
              contaSaque.sacar(valorSaque);
              System.out.println("Por favor, retire seu dinheiro na boca do caixa.");
            } catch (SaldoInsuficienteException e){
              System.out.println("Operação cancelada pelo banco: " + e.getMessage());
            }
          } else {
            System.out.println("Conta não Encontrada!");
          }
          break;

        case 4:
          meuBanco.exibirRelatorio();
          break;

        case 5:
          System.out.println("\n--- CADASTRO DE NOVA CONTA ---");
          System.out.println("1 - Conta Corrente");
          System.out.println("2 - Conta Poupança");
          System.out.print("Escolha o tipo: ");
          int tipo = teclado.nextInt();
          teclado.nextLine(); //Limpar buffer do teclado

          System.out.print("Nome do Titular: ");
          String nome = teclado.nextLine();

          System.out.print("Número da Conta: ");
          String numero = teclado.nextLine();

          System.out.print("Saldo inicial: R$ ");
          double saldoInicial = teclado.nextDouble();

          if (tipo == 1){
            meuBanco.adicionarConta(new dia03.ContaCorrente(nome, numero, saldoInicial));
          } else if (tipo == 2) {
            System.out.print("Taxa de Rendimento (ex.: 0.05 para 5%): ");
            double taxa = teclado.nextDouble();
            meuBanco.adicionarConta(new dia02.ContaPoupanca(nome, numero, saldoInicial, taxa));
          } else {
            System.out.println("Tipo Inválido! Conta não criada.");
          }
          break;

        case 6:
          double total = meuBanco.calcularPatrimonioTotal();
          System.out.println("\nPatrimônio Total do Banco: R$ " + total);
          break;

        case 7:
          System.out.print("Digite o saldo máximo para filtro: R$ ");
          double limite = teclado.nextDouble();
          List<dia01.Conta> filtradas = meuBanco.buscarContasComSaldoAte(limite);

          System.out.println("\n=== CONTAS ENCONTRADAS (Até R$ " + limite + ") ===");
          filtradas.forEach(c -> System.out.println("Titular: " + c.getTitular() + "| Saldo: R$ " + c.getSaldo()));
          break;

        case 8:
          teclado.nextLine();
          System.out.print("Digite o número da conta: ");
          String numExtrato = teclado.nextLine();

          dia01.Conta contaExtrato = meuBanco.buscarContaPorNumero(numExtrato);

          if(contaExtrato != null){
            contaExtrato.exibirExtrato();
          } else {
            System.out.println("Conta não encontrada!");
          }
          break;

        case 9:
          System.out.println("\n--- COBRANÇA DE IMPOSTOS ---");
          meuBanco.getListaDeContas().stream()
                  .filter(conta -> conta instanceof dia09.Tributavel)
                  .map(conta -> (dia09.Tributavel) conta)
                  .forEach(tributavel -> {
                    double imposto = tributavel.calcularImposto();
                    dia01.Conta c = (dia01.Conta) tributavel;
                    try {
                      c.sacar(imposto);
                      System.out.println("Imposto de R$ " + imposto + " cobrado da conta " + c.getNumeroDaConta());
                    } catch (SaldoInsuficienteException e){
                      System.out.println("Conta " + c.getNumeroDaConta() + "sem saldo para pagar imposto!");
                    }
                  });
          break;

        case 0:
          System.out.println("Desligando o Caixa Eletrônico. Volte sempre!");
          dia10.GerenciadorDeArquivos.salvarContas(meuBanco.getListaDeContas());
          break;

        default:
          //Se o usuário digitar um número fora das opções
          System.out.println("Opção inválida! Tente novamente.");
      }
    }

    teclado.close();
  }
}