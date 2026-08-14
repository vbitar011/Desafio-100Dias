package dia10;

import dia01.Conta;
import dia02.ContaPoupanca;
import dia03.ContaCorrente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeArquivos {

    private static final String NOME_ARQUIVO = "contas.csv";

    //Salvando as contas no arquivo CSV
    public static void salvarContas(List<Conta> contas){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Conta c : contas){
                String tipo = (c instanceof ContaPoupanca) ? "POUPANÇA" : "CORRENTE";
                //Formatando para TIPO;NUMERO;TITULAR;SALDO
                String linha = tipo + ";" + c.getNumeroDaConta() + ";" + c.getTitular() + ";" + c.getSaldo();
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Dados salvos com sucesso em: " + NOME_ARQUIVO + "!");
        } catch (IOException e){
            System.out.println("Erro ao salvar dados no arquivo " + e.getMessage());
        }
    }

    //Carregar o arquivo e reconstruir as contas na memória
    public static List<Conta> carregarContas(){
        List<Conta> contasCarregadas = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);

        if (!arquivo.exists()){
            System.out.println("Nenhum arquivo de dados prévio encontrado. Iniciando banco do zero.");
            return contasCarregadas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))){
            String linha;
            while((linha = reader.readLine()) != null){
                String[] dados = linha.split(";");
                if (dados.length == 4){
                    String tipo = dados[0];
                    String numero = dados[1];
                    String titular = dados[2];
                    double saldo = Double.parseDouble(dados[3]);

                    if (tipo.equals("POUPANÇA")){
                        contasCarregadas.add(new ContaPoupanca(titular, numero, saldo, 0.05));
                    } else {
                        contasCarregadas.add(new ContaCorrente(titular, numero, saldo));
                    }
                }
            }
            System.out.println("Dados carregado com sucesso do arquivo " + NOME_ARQUIVO);
        } catch (IOException e){
            System.out.println("Erro ao carregar arquivo de dados: " + e.getMessage());
        }

        return contasCarregadas;
    }
}
