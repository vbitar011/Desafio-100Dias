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
        String numSaque = dia16.TecladoUtil.lerNumeroConta(teclado, "Digite o número da conta: ");

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
        String numSaldo = dia16.TecladoUtil.lerNumeroConta(teclado, "Digite o número da conta: ");

        dia01.Conta contaSaldo = meuBanco.buscarContaPorNumero(numSaldo);

        if(contaSaldo == null) {
            System.out.println("❌ Erro: Conta não encontrada!");
            return;
        }

        String senhaDigitada = dia16.TecladoUtil.lerSenhaNumerica(teclado, "Digite a senha da conta: ");

        if (!contaSaldo.autenticar(senhaDigitada)){
            System.out.println("❌ Acesso Negado: Senha incorreta.");
            return;
        }

        contaSaldo.exibirSaldo();
    }

    public void realizarDeposito(){
        String numDeposito = dia16.TecladoUtil.lerNumeroConta(teclado, "Digite o número da conta: ");

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

        if (tipo != 1 && tipo != 2) {
            System.out.println("❌ Tipo inválido! Operação cancelada.");
            return;
        }

        String nome = dia16.TecladoUtil.lerNomeTitular(teclado, "Nome do Titular: ");
        String numero = dia16.TecladoUtil.lerNumeroConta(teclado, "Número da Conta: ");
        String senhaCriada = dia16.TecladoUtil.lerSenhaNumerica(teclado, "Crie uma senha numérica (4 a 6 dígitos): ");
        double saldoInicial = dia16.TecladoUtil.lerDouble(teclado, "Saldo inicial: R$ ");

        if (tipo == 1) {
            dia03.ContaCorrente novaCC = new dia03.ContaCorrente(nome, numero, saldoInicial, senhaCriada);
            meuBanco.adicionarConta(novaCC);
            dia12.ContaDAO.salvarConta(novaCC);

        } else {
            double taxa = dia16.TecladoUtil.lerDouble(teclado, "Taxa de Rendimento (ex.: 0.05 para 5%): ");
            dia02.ContaPoupanca novaCP = new dia02.ContaPoupanca(nome, numero, saldoInicial, taxa, senhaCriada);
            meuBanco.adicionarConta(novaCP);
            dia12.ContaDAO.salvarConta(novaCP);
        }
    }

    public void exibirExtrato(){
        String numExtrato = dia16.TecladoUtil.lerNumeroConta(teclado, "Digite o número da conta: ");

        dia01.Conta contaExtrato = meuBanco.buscarContaPorNumero(numExtrato);

        if (contaExtrato == null){
            System.out.println("❌ Erro: Conta não encontrada!");
            return;
        }

        String senhaDigitada = dia16.TecladoUtil.lerSenhaNumerica(teclado, "Digite a senha da conta: ");

        if (!contaExtrato.autenticar(senhaDigitada)) {
            System.out.println("❌ Acesso Negado: Senha incorreta.");
            return;
        }

        contaExtrato.exibirExtrato();
    }

    public void realizarTransferencia() {
        System.out.println("\n--- ÁREA DE TRANSFERÊNCIA (PIX/TED) ---");

        //1. Valida origem e senha primeiro
        String numOrigem = dia16.TecladoUtil.lerNumeroConta(teclado, "Digite o número da SUA conta (Origem): ");
        Conta contaOrigem = meuBanco.buscarContaPorNumero(numOrigem);

        if (contaOrigem == null) {
            System.out.println("❌ Erro: Conta de origem não encontrada no banco.");
            return; //Aborta se a origem não existir
        }

        String senhaDigitada = dia16.TecladoUtil.lerSenhaNumerica(teclado, "Digite a senha da sua conta (Origem): ");

        if (!contaOrigem.autenticar(senhaDigitada)) {
            System.out.println("❌ Acesso Negado: Senha incorreta. Operação cancelada.");
            return; //Aborta se a senha estiver errada
        }

        //Verifica o tipo de transferência
        System.out.println("1 - Por Número da Conta");
        System.out.println("2 - Por Chave PIX");
        int tipo = dia16.TecladoUtil.lerInteiro(teclado, "Escolha qual tipo de transferência irá realizar: ");

        //2. Declara a variável alvo FORA dos blocos de decisão
        Conta contaDestino = null;

        //3. Descobrir o destino
        if (tipo == 1) {
            String numeroDigitado = dia16.TecladoUtil.lerNumeroConta(teclado, "Digite o número da conta de destino (XXXXX-X): ");
            contaDestino = meuBanco.buscarContaPorNumero(numeroDigitado);

        } else if (tipo == 2) {
            System.out.print("Digite a Chave PIX: ");
            String chaveDigitada = teclado.nextLine().trim();
            String numeroContaPix = dia12.ContaDAO.buscarNumeroContaPorChavePix(chaveDigitada);

            if (numeroContaPix == null) {
                System.out.println("❌ Erro: Chave PIX não encontrada.");
                return; //Aborta se a chave PIX não existir no banco
            }
            contaDestino = meuBanco.buscarContaPorNumero(numeroContaPix);

        } else {
            System.out.println("❌ Opção inválida.");
            return; //Aborta se o usuário digitar 3, 4, etc.
        }

        //4. Barreira de segurança unificada
        if (contaDestino == null) {
            System.out.println("❌ Erro: Conta de destino não encontrada.");
            return; //Última barreira antes do caminho feliz
        }

        //5. Execução única da transferência
        double valorTransferencia = dia16.TecladoUtil.lerDouble(teclado, "Digite o valor da transferência: R$ ");

        try {
            //Realiza as operações financeiras
            contaOrigem.sacar(valorTransferencia);
            contaDestino.depositar(valorTransferencia);

            //Salva no banco de dados (DAO)
            dia12.ContaDAO.atualizarSaldo(contaOrigem);
            dia12.ContaDAO.atualizarSaldo(contaDestino);

            System.out.println("✅ Transferência concluída com sucesso!");

        } catch (dia05.SaldoInsuficienteException e) {
            System.out.println("❌ Transferência cancelada: " + e.getMessage());
        }
    }

    public void encerrarConta(){
        String numEncerramento = dia16.TecladoUtil.lerNumeroConta(teclado, "Digite o número da conta que deseja ENCERRAR: ");
        Conta contaParaEncerrar = meuBanco.buscarContaPorNumero(numEncerramento);

        if (contaParaEncerrar == null) {
            System.out.println("❌ Erro: Conta não encontrada!");
            return;
        }

        if (contaParaEncerrar.getSaldo() > 0) {
            System.out.println("⚠️ NEGADO: A conta possui saldo de R$ " + contaParaEncerrar.getSaldo() + ".");
            return;
        }

        String senhaDigitada = dia16.TecladoUtil.lerSenhaNumerica(teclado, "Digite a senha da conta: ");
        if (!contaParaEncerrar.autenticar(senhaDigitada)) {
            System.out.println("❌ Acesso Negado: Senha incorreta.");
            return;
        }

        meuBanco.removerConta(contaParaEncerrar);
        dia12.ContaDAO.deletarConta(numEncerramento);
        System.out.println("✅ Conta encerrada definitivamente.");
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
                        dia12.ContaDAO.atualizarSaldo(c);
                        System.out.println("Imposto de R$ " + formatador.format(imposto) + " cobrado da conta " + c.getNumeroDaConta());
                    } catch (SaldoInsuficienteException e){
                        System.out.println("Conta " + c.getNumeroDaConta() + "sem saldo para pagar imposto!");
                    }
                });
    }

    public void cadastrarChavePix() {
        System.out.println("\n--- CADASTRO DE CHAVE PIX ---");

        //A Segurança (Cláusulas de Guarda)
        String numeroDaConta = dia16.TecladoUtil.lerNumeroConta(teclado, "Digite o número da sua conta: ");
        Conta conta = meuBanco.buscarContaPorNumero(numeroDaConta);

        if (conta == null) {
            System.out.println("❌ Erro: Conta não encontrada.");
            return; //Aborta se a conta não existir
        }

        String senhaDigitada = dia16.TecladoUtil.lerSenhaNumerica(teclado, "Digite a sua senha: ");
        if (!conta.autenticar(senhaDigitada)) {
            System.out.println("❌ Acesso Negado: Senha incorreta. Operação cancelada.");
            return; //Aborta se a senha estiver errada
        }

        //Escolhe o tipo da chave
        System.out.println("\nEscolha o tipo de chave que deseja cadastrar:");
        System.out.println("1 - CPF");
        System.out.println("2 - E-mail");
        System.out.println("3 - Telefone");
        int opcaoTipo = dia16.TecladoUtil.lerInteiro(teclado, "Opção: ");

        String tipoEscolhido = "";
        if (opcaoTipo == 1) {
            tipoEscolhido = "CPF";
        } else if (opcaoTipo == 2) {
            tipoEscolhido = "E-mail";
        } else if (opcaoTipo == 3) {
            tipoEscolhido = "Telefone";
        } else {
            System.out.println("❌ Opção de tipo de chave inválida.");
            return; //Cláusula de guarda extra para garantir que o tipo seja válido
        }

        //O Input
        System.out.print("Digite a chave " + tipoEscolhido + " que deseja cadastrar: ");
        String chaveDigitada = teclado.nextLine().trim();

        //Consolidação do cadastro
        dia12.ContaDAO.salvarChavePix(chaveDigitada, tipoEscolhido, numeroDaConta);
    }
}
