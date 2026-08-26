package dia16;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TecladoUtil {

    //Método para ler seguramente números inteiros(opções do menu e tipo de conta)
    public static int lerInteiro(Scanner teclado, String mensagem){
        while (true){ //Repete até o usuário acertar
            try {
                System.out.print(mensagem);
                int valor = teclado.nextInt();
                teclado.nextLine(); //Lipa o "Enter" fantasma do buffer
                return valor; //Retorna o número e encerra o laço

            } catch (InputMismatchException e){
                System.out.println("❌ Entrada inválida! Por favor digite apenas números inteiros.");
                teclado.nextLine(); //Limpa o texto errado que ficou preso no buffer
            }
        }
    }

    //Método para ler seguramente valores decimais(dinheiro e taxas)
    public static double  lerDouble(Scanner teclado, String mensagem){
        while (true){
            try {
                System.out.print(mensagem);
                double valor = teclado.nextDouble();
                teclado.nextLine();
                return valor;

            } catch (InputMismatchException e){
                System.out.println("❌ Entrada inválida! Por favor, digite um valor numérico válido (ex: 50,00 ou 50.00).");
                teclado.nextLine(); // Limpa o texto errado que ficou preso no buffer
            }
        }
    }

    public static String lerSenhaNumerica(Scanner teclado, String mensagem) {
        String senha;
        while (true) {
            System.out.print(mensagem);
            senha = teclado.next();

            //O '\\d{4,6}' é truque do Java que diz: "Apenas dígitos numéricos, de 4 até 6 vezes"
            if (senha.matches("\\d{4,6}")) {
                return senha;
            }
            System.out.println("❌ Erro: A senha deve conter APENAS NÚMEROS e ter entre 4 e 6 dígitos. Tente novamente.");
        }
    }
}
