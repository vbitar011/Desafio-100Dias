package dia02;

import dia01.Conta;

public class ContaPoupanca extends Conta {
    private double taxaRendimento;

    public ContaPoupanca(String titular, String numeroDaConta, double saldo, double taxaRendimento, String senha){
        super(titular, numeroDaConta, saldo, senha);
        this.taxaRendimento = taxaRendimento;
    }

    public void aplicarRendimento(){
        if (taxaRendimento > 0){
            double valorRendido = saldo * taxaRendimento;
            saldo = saldo + valorRendido;
        }
    }
}
