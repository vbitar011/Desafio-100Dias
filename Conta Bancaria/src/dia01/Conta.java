package dia01;

import dia05.SaldoInsuficienteException;

public class Conta {
    //Definindo atributos
    private String titular;
    private String numeroDaConta;
    protected double saldo;

    public Conta(String titular, String numeroDaConta, double saldo){
        //Iniciando os atributos definidos
        this.titular = titular;
        this.numeroDaConta = numeroDaConta;
        this.saldo = saldo;
    }

    //Função para saque que irá verificar antes de executar.
    public void sacar(double valor){
        if (saldo < valor) {
            throw new dia05.SaldoInsuficienteException("Erro no saque: Saldo insuficiente. Saldo atual: R$ " + this.saldo);
        } else {
            saldo = saldo - valor;
            System.out.println("Saque de R$" + valor + " realizado com sucesso!");
        }
    }

    //Função para depósito, que também verifica antes de executar
    public void depositar(double valor){
        if (valor <= 0) {
           System.out.println("Valor Insuficiente! Tente novamente");
        } else{
            saldo = saldo + valor;
            System.out.println( valor + " Depositado com sucesso!");
        }
    }

    //Exibição do saldo após operações
    public void exibirSaldo(){
        System.out.println("Saldo atual após operação: " + saldo);
    }

    //Getter para acessar o titular fora do arquivo
    public String getTitular() {
        return titular;
    }

    //Getter para acessar o saldo fora do arquivo e manipula-lo quando permitido e necessário
    public double getSaldo() {
        return saldo;
    }

    //Getter para acessar o número da conta e permitir a busca pela conta no menu
    public String getNumeroDaConta(){ return numeroDaConta; }
}
