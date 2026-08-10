package dia01;

import dia02.ContaPoupanca;
import dia03.ContaCorrente;
import dia04.Banco;
import dia05.SaldoInsuficienteException;

public class Main{
  public static void main(String[] args){
    Conta minhaConta = new Conta("Victor", " 12345-6", 400.0);
    ContaPoupanca anaConta = new ContaPoupanca("Ana Clara", " 67891-0", 100.0, 0.1);
    ContaCorrente ClaraConta = new ContaCorrente("Clara", " 65432-1", 0.0);
    Banco meuBanco = new Banco();

    meuBanco.adicionarConta(minhaConta);
    meuBanco.adicionarConta(anaConta);
    meuBanco.adicionarConta(ClaraConta);

    meuBanco.exibirRelatorio();

    try {
      minhaConta.sacar(5000.0);
      System.out.println("Por favor, retire seu dinheiro na boca do caixa.");
    } catch (SaldoInsuficienteException e){
       System.out.println("Operação cancelada pelo banco: " + e.getMessage());
    }
  }
}