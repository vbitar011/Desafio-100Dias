package dia01;

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
            System.out.println("Saldo insuficiente!");
        } else {
            saldo = saldo - valor;
            System.out.println("Saque de R$" + valor + " realizado com sucesso!");
        }
    }

    //Função para depósito, que também verifica antes de executar
    public void depositar(double valor){
        if (valor <= 0){
            System.out.println("Valor insuficiente para deposito. Tente Novamente!");
        } else {
            saldo = saldo + valor;
            System.out.println( valor + " Depositado com sucesso!");
        }
    }

    //Exibição do saldo após operações
    public void exibirSaldo(){
        System.out.println("Saldo atual após operação: " + saldo);
    }
}
