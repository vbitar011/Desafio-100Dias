package dia04;


import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
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
        listaDeContas.forEach(conta -> System.out.println("Titular: " + conta.getTitular() + " | Saldo: R$ " + conta.getSaldo()));

        System.out.println("====================================");
    }

    public double calcularPatrimonioTotal(){
        return listaDeContas.stream()
                .mapToDouble(Conta::getSaldo) //Pega todos os saldos das contas
                .sum(); //Soma todos
    }

    public List<Conta> buscarContasComSaldoAte(double valorMaximo){
        return listaDeContas.stream()
                .filter(conta -> conta.getSaldo() <= valorMaximo) //Filtra as contas que atendem ao critério pedido
                .collect(Collectors.toList()); //Devolve o Fluxo filtrado em lista
    }

    public Conta buscarContaPorNumero(String numero){
        return listaDeContas.stream()
                .filter(conta -> conta.getNumeroDaConta().equalsIgnoreCase(numero))
                .findFirst()
                .orElse(null); //Retornará 'null' se nenhuma conta for encontrada
    }

    public List<Conta> getListaDeContas(){
        return listaDeContas;
    }

    public void removerConta(Conta conta){
        this.listaDeContas.remove(conta);
    }

}

