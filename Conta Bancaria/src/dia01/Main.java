package dia01;

import dia02.ContaPoupanca;
import dia03.ContaCorrente;
import dia04.Banco;
import dia05.SaldoInsuficienteException;
import dia11.ConexaoDB;
import dia17.CaixaEletronicoUI;

import java.util.List;
import java.util.Scanner;

public class Main{
  public static void main(String[] args){
    dia11.ConexaoDB.conectar();
    dia12.ContaDAO.criarTabelaSeNaoExistir();

    Scanner teclado = new Scanner(System.in);
    Banco meuBanco = new Banco();

    //Carrega automaticamente as contas salvas anteriormente
    List<Conta> contasSalvas = dia12.ContaDAO.carregarContas();
    contasSalvas.forEach(meuBanco :: adicionarConta);

    CaixaEletronicoUI interfaceUI = new CaixaEletronicoUI(meuBanco, teclado);
    interfaceUI.iniciarMenu();

    teclado.close();
  }
}