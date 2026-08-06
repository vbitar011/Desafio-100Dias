package dia01;

import dia02.ContaPoupanca;

public class Main{
  public static void main(String[] args){
    Conta minhaConta = new Conta("Victor", " 12345-6", 400.0);
    ContaPoupanca anaConta = new ContaPoupanca("Ana Clara", " 67891-0", 100.0, 0.1);

    minhaConta.depositar(10.0);
    minhaConta.exibirSaldo();

    anaConta.aplicarRendimento();
    anaConta.exibirSaldo();
  }
}