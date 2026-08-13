package dia01;

import dia05.SaldoInsuficienteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Conta {
    //Definindo atributos
    private String titular;
    private String numeroDaConta;
    protected double saldo;
    protected List<String> extrato = new ArrayList<>();

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
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            extrato.add(dataHora + " - Saque: R$ " + valor);
        }
    }

    //Função para depósito, que também verifica antes de executar
    public void depositar(double valor){
        if (valor <= 0) {
           System.out.println("Valor Insuficiente! Tente novamente");
        } else{
            saldo = saldo + valor;
            System.out.println( valor + " Depositado com sucesso!");
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            extrato.add(dataHora + " - Depósito: R$ " + valor);
        }
    }

    //Método para exibição do extrato
    public void exibirExtrato(){
        System.out.println("\n=== EXTRATO DA CONTA " + numeroDaConta + " ===");
        if (extrato.isEmpty()){
            System.out.println("Nenhuma movimentação registrada.");
        } else {
            extrato.forEach(movimento -> System.out.println(movimento));
        }
        System.out.println("Saldo atual: R$ " +getSaldo());
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
