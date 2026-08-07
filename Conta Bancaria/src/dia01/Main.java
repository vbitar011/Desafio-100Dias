package dia01;

import dia02.ContaPoupanca;
import dia03.ContaCorrente;

public class Main{
  public static void main(String[] args){
    Conta minhaConta = new Conta("Victor", " 12345-6", 400.0);
    ContaPoupanca anaConta = new ContaPoupanca("Ana Clara", " 67891-0", 100.0, 0.1);
    ContaCorrente ClaraConta = new ContaCorrente("Clara", " 65432-1", 0.0);

    minhaConta.depositar(10.0);
    minhaConta.exibirSaldo();

    anaConta.aplicarRendimento();
    anaConta.exibirSaldo();

    ClaraConta.depositar(250.0);
    ClaraConta.sacar(50.0);
  }
}