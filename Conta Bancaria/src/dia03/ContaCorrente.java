package dia03;

import dia01.Conta;

public class ContaCorrente extends Conta {

    public ContaCorrente(String titular, String numeroDaConta, double saldo){
        super(titular, numeroDaConta, saldo);
    }

    @Override
    public void sacar(double valor){
        double taxaSaque = 2.0;
        double valorTotal = valor + taxaSaque;

        super.sacar(valorTotal);
    }
}
