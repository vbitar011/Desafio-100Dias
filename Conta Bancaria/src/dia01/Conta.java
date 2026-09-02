package dia01;

import dia05.SaldoInsuficienteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class Conta {
    //Definindo atributos
    private String titular;
    private String numeroDaConta;
    protected double saldo;
    protected String senha;
    protected List<String> extrato = new ArrayList<>();
    protected List<String> chavesPix = new ArrayList<>();

    protected NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public Conta(String titular, String numeroDaConta, double saldo, String senha){
        //Iniciando os atributos definidos
        this.titular = titular;
        this.numeroDaConta = numeroDaConta;
        this.saldo = saldo;
        this.senha = senha;
    }

    //Função para saque que irá verificar antes de executar.
    public void sacar(double valor){
        if (saldo < valor) {
            throw new dia05.SaldoInsuficienteException("Erro no saque: Saldo insuficiente. Saldo atual: R$ " + this.saldo);
        } else {
            saldo = saldo - valor;
            System.out.println("Saque de " + formatador.format(valor) + " realizado com sucesso!");
            String mensagem = dataHoraFormatada() + " - Saque: " + formatador.format(valor);
            dia12.ContaDAO.salvarTransacao(this.numeroDaConta, mensagem);
        }
    }

    //Função para depósito, que também verifica antes de executar
    public void depositar(double valor){
        if (valor <= 0) {
            throw new IllegalArgumentException("Erro no depósito: Valor deve ser maior que zero.");
        }
        saldo = saldo + valor;
        System.out.println( formatador.format(valor) + " Depositado com sucesso!");
        String mensagem = dataHoraFormatada() + " - Depósito: " + formatador.format(valor);
        dia12.ContaDAO.salvarTransacao(this.numeroDaConta, mensagem);
    }

    //Método para exibição do extrato
    public void exibirExtrato(){
        System.out.println("\n=== EXTRATO DA CONTA " + numeroDaConta + " ===");

        List<String> historicoDB = dia12.ContaDAO.buscarExtrato(this.numeroDaConta);
        if (historicoDB.isEmpty()){
            System.out.println("Nenhuma movimentação registrada.");
        } else {
            historicoDB.forEach(movimento -> System.out.println(movimento));
        }
        System.out.println("Saldo atual: " + formatador.format(getSaldo()));
    }

    //Exibição do saldo após operações
    public void exibirSaldo(){
        System.out.println("Saldo atual após operação: " + formatador.format(saldo));
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

    public boolean autenticar(String senhaDigitada) {
        return this.senha.equals(senhaDigitada);
    }

    public String getSenha(){ return senha; }

    private String dataHoraFormatada(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
