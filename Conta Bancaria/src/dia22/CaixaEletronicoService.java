package dia22;

import dia01.Conta;
import dia03.ContaCorrente;
import dia04.Banco;
import dia05.SaldoInsuficienteException;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class CaixaEletronicoService {
    private Banco meuBanco;
    private Scanner teclado;

    protected NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));


    public CaixaEletronicoService(Banco meuBanco, Scanner teclado) {
        this.meuBanco = meuBanco;
        this.teclado = teclado;
    }

    public void realizarSaque() {
        System.out.println("\n--- REALIZAR SAQUE ---");
        System.out.print("Digite o número da conta: ");
        String numSaque = teclado.next();

        Conta contaSaque = meuBanco.buscarContaPorNumero(numSaque);

        if (contaSaque != null) {
            String senhaDigitada = dia16.TecladoUtil.lerSenhaNumerica(teclado, "Digite sua senha de acesso: ");

            if (contaSaque.autenticar(senhaDigitada)) {
                double valorSaque = dia16.TecladoUtil.lerDouble(teclado, "Digite o valor para saque: R$ ");
                try {
                    contaSaque.sacar(valorSaque);
                    dia12.ContaDAO.atualizarSaldo(contaSaque);
                } catch (dia05.SaldoInsuficienteException e) {
                    System.out.println("❌ Erro: " + e.getMessage());
                }
            } else {
                System.out.println("❌ Acesso Negado: Senha incorreta.");
            }
        } else {
            System.out.println("❌ Conta não encontrada.");
        }
    }

    public void consultarSaldo(){
        System.out.print("Digite o número da conta: ");
        String numSaldo = teclado.nextLine();

        dia01.Conta contaSaldo = meuBanco.buscarContaPorNumero(numSaldo);

        if(contaSaldo != null){
            contaSaldo.exibirSaldo();
        } else {
            System.out.println("Conta não encontrada!");
        }
    }

    public void realizarDeposito(){
        System.out.print("Digite o número da conta: ");
        String numDeposito = teclado.nextLine();

        dia01.Conta contaDeposito = meuBanco.buscarContaPorNumero(numDeposito);

        if(contaDeposito != null){
            double valorDeposito = dia16.TecladoUtil.lerDouble(teclado, "Digite o valor para depósito: R$ ");
            contaDeposito.depositar(valorDeposito);
            dia12.ContaDAO.atualizarSaldo(contaDeposito);
        } else {
            System.out.println("Conta não encontrada!");
        }
    }

    public void criarNovaConta(){
        System.out.println("\n--- CADASTRO DE NOVA CONTA ---");
        System.out.println("1 - Conta Corrente");
        System.out.println("2 - Conta Poupança");
        int tipo = dia16.TecladoUtil.lerInteiro(teclado, "Escolha o tipo da conta: ");
        teclado.nextLine(); //Limpar buffer do teclado

        System.out.print("Nome do Titular: ");
        String nome = teclado.nextLine();

        System.out.print("Número da Conta: ");
        String numero = teclado.nextLine();

        String senhaCriada = dia16.TecladoUtil.lerSenhaNumerica(teclado, "Crie uma senha numérica (4 a 6 dígitos): ");
        double saldoInicial = dia16.TecladoUtil.lerDouble(teclado, "Saldo inicial: R$ ");

        if (tipo == 1){
            dia03.ContaCorrente novaCC = new ContaCorrente(nome, numero, saldoInicial, senhaCriada);
            meuBanco.adicionarConta(novaCC);
            dia12.ContaDAO.salvarConta(novaCC);


        } else if (tipo == 2) {
            double taxa = dia16.TecladoUtil.lerDouble(teclado, "Taxa de Rendimento (ex.: 0,05 para5 %");

            dia02.ContaPoupanca novaCP = new dia02.ContaPoupanca(nome, numero, saldoInicial, taxa, senhaCriada);
            meuBanco.adicionarConta(novaCP);
            dia12.ContaDAO.salvarConta(novaCP);

        } else {
            System.out.println("Tipo Inválido! Conta não criada.");
        }
    }

    public void exibirExtrato(){
        System.out.print("Digite o número da conta: ");
        String numExtrato = teclado.nextLine();

        dia01.Conta contaExtrato = meuBanco.buscarContaPorNumero(numExtrato);

        String senhaDigitada = dia16.TecladoUtil.lerSenhaNumerica(teclado, "Digite a senha da conta: ");

        if (contaExtrato.autenticar(senhaDigitada)) {
            contaExtrato.exibirExtrato();

        } else {
            System.out.println("❌ Acesso Negado: Senha incorreta. Operação cancelada!");
        }
    }

    public void realizarTransferencia(){
        System.out.println("\n--- ÁREA DE TRANSFERÊNCIA (PIX/TED) ---");

        System.out.print("Digite o número da SUA conta (Origem): ");
        String numOrigem = teclado.next();

        System.out.print("Digite o número da conta de destino: ");
        String numDestino = teclado.next();

        Conta contaOrigem = meuBanco.buscarContaPorNumero(numOrigem);
        Conta contaDestino = meuBanco.buscarContaPorNumero(numDestino);

        if (contaOrigem != null && contaDestino != null) {

            String senhaDigitada = dia16.TecladoUtil.lerSenhaNumerica(teclado, "Digite a senha da sua conta (Origem): ");

            if (contaOrigem.autenticar(senhaDigitada)) {

                double valorTransferencia = dia16.TecladoUtil.lerDouble(teclado, "Digite o valor da transferência: R$ ");

                try {
                    contaOrigem.sacar(valorTransferencia);
                    contaDestino.depositar(valorTransferencia);

                    dia12.ContaDAO.atualizarSaldo(contaOrigem);
                    dia12.ContaDAO.atualizarSaldo(contaDestino);

                    System.out.println("✅ Transferência concluída com sucesso!");

                } catch (dia05.SaldoInsuficienteException e) {
                    System.out.println("❌ Transferência cancelada: " + e.getMessage());
                }

            } else {
                System.out.println("❌ Acesso Negado: Senha incorreta. Operação cancelada.");
            }

        } else {
            System.out.println("❌ Erro: A conta de origem ou a conta de destino não foi encontrada no banco.");
        }
    }

    public void encerrarConta(){
        System.out.print("Digite o número da conta que deseja ENCERRAR: ");
        String numEncerramento = teclado.next();

        Conta contaParaEncerrar = meuBanco.buscarContaPorNumero(numEncerramento);

        if (contaParaEncerrar != null){ //Tenta achar a conta
            if (contaParaEncerrar.getSaldo() == 00){ //Verifica se o saldo está zerado para ser encerrada
                meuBanco.removerConta(contaParaEncerrar);//remove da RAM
                dia12.ContaDAO.deletarConta(numEncerramento);//Remove do banco de dados
                System.out.println("Conta encerrada definitivamente.");
            } else {
                System.out.println("⚠️ NEGADO: A conta possui saldo de R$ " + contaParaEncerrar.getSaldo() + ".");
                System.out.println("Você precisa sacar ou transferir todo o dinheiro antes de encerrar.");
            }
        } else {
            System.out.println("Conta não encontrada!");
        }
    }

    public void cobrarImpostos(){
        System.out.println("\n--- COBRANÇA DE IMPOSTOS ---");
        meuBanco.getListaDeContas().stream()
                .filter(conta -> conta instanceof dia09.Tributavel)
                .map(conta -> (dia09.Tributavel) conta)
                .forEach(tributavel -> {
                    double imposto = tributavel.calcularImposto();
                    dia01.Conta c = (dia01.Conta) tributavel;
                    try {
                        c.sacar(imposto);
                        System.out.println("Imposto de R$ " + formatador.format(imposto) + " cobrado da conta " + c.getNumeroDaConta());
                    } catch (SaldoInsuficienteException e){
                        System.out.println("Conta " + c.getNumeroDaConta() + "sem saldo para pagar imposto!");
                    }
                });
    }
}
