package dia04;


import java.util.List;
import java.util.ArrayList;
import dia01.Conta;

public class Banco {
    private List<Conta> listaDeContas;

    public Banco(){
        this.listaDeContas = new ArrayList<>();
    }

    public void adicionarConta(Conta novaConta){
        listaDeContas.add(novaConta);
        System.out.println("Conta adicionada ao banco com sucesso!");
    }

    public void exibirRelatorio() {
        System.out.println("\n=== RELATÓRIO DE CONTAS DO BANCO ===");

        // Lê-se: "Para cada 'Conta' (que vamos chamar de 'contaAtual') dentro da 'listaDeContas'..."
        for (Conta contaAtual : listaDeContas) {
            System.out.println("Titular: " + contaAtual.getTitular() + " | Saldo: R$ " + contaAtual.getSaldo());
        }

        System.out.println("====================================");
    }
}

