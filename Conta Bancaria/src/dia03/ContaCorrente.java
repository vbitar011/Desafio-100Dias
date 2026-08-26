package dia03;

import dia01.Conta;
import dia09.Tributavel;

public class ContaCorrente extends Conta implements Tributavel {

    public ContaCorrente(String titular, String numeroDaConta, double saldo, String senha){
        super(titular, numeroDaConta, saldo, senha);
    }

    @Override
    public void sacar(double valor){
        double taxaSaque = 2.0;
        double valorTotal = valor + taxaSaque;

        super.sacar(valorTotal);
    }

    @Override
    public double calcularImposto(){
        return getSaldo() * 0.05;
    }
}
