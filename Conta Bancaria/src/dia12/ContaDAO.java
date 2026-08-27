package dia12;

import dia01.Conta;
import dia02.ContaPoupanca;
import dia03.ContaCorrente;
import dia11.ConexaoDB;
import dia20.TipoConta;

import java.awt.desktop.SystemSleepEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class ContaDAO {

    //Método para criar a estrutura do banco
    public static void criarTabelaSeNaoExistir(){
        //Comandos SQL para criar a tabela
        String sql = "CREATE TABLE IF NOT EXISTS contas ("
                + "numero TEXT PRIMARY KEY, "
                + "titular TEXT NOT NULL, "
                + "saldo REAL, "
                + "tipo TEXT NOT NULL, "
                + "senha TEXT NOT NULL"
                + ");";

        String sqlTransacoes = "CREATE TABLE IF NOT EXISTS transacoes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "numero_conta TEXT NOT NULL, "
                + "descricao TEXT NOT NULL, "
                + "FOREIGN KEY (numero_conta) REFERENCES contas(numero)"
                + ");";

        try (Connection conexao = ConexaoDB.conectar();
             Statement stmt = conexao.createStatement()){

            stmt.execute(sql);
            stmt.execute(sqlTransacoes);
            System.out.println("✅ Tabela de contas pronta para uso!");
        } catch (SQLException e){
            System.out.println("❌ Erro  ao criar a tabela " + e.getMessage());
        }
    }

    public static void salvarConta(Conta conta){
        String sql = "INSERT OR REPLACE INTO contas (numero, titular, saldo, tipo, senha) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoDB.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)){

            pstmt.setString(1, conta.getNumeroDaConta());
            pstmt.setString(2, conta.getTitular());
            pstmt.setDouble(3, conta.getSaldo());

            if (conta instanceof ContaPoupanca){
                pstmt.setString(4, TipoConta.POUPANCA.name());
            } else{
                pstmt.setString(4, TipoConta.CORRENTE.name());
            }

            pstmt.setString(5, conta.getSenha());
            pstmt.executeUpdate();

            System.out.println("✅ Conta " + conta.getNumeroDaConta() + " salva no Banco de Dados!");

        } catch (SQLException e){
            System.out.println("Erro ao salvar a conta: " + e.getMessage());
        }
    }

    public static List<Conta> carregarContas(){
        List<Conta> contasCarregadas = new ArrayList<>();
        String sql = "SELECT * FROM contas";

        try (Connection conexao = ConexaoDB.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()){
                String numero = rs.getString("numero");
                String titular = rs.getString("titular");
                double saldo = rs.getDouble("saldo");
                String tipo = rs.getString("tipo");
                String senhaSalva = rs.getString("senha");

                if (TipoConta.valueOf(tipo) == TipoConta.POUPANCA){
                    contasCarregadas.add(new ContaPoupanca(titular, numero, saldo, 0.05, senhaSalva));
                } else {
                    contasCarregadas.add(new ContaCorrente(titular, numero, saldo, senhaSalva));
                }
            }
            System.out.println("✅ Dados carregados do Banco SQLite com sucesso!");
        } catch (SQLException e){
            System.out.println("❌ Erro ao buscar contas no banco: " + e.getMessage());
        }

        return contasCarregadas;
    }

    public static void atualizarSaldo(Conta conta){
        String sql = "UPDATE contas SET saldo = ? WHERE numero = ?";

        try (Connection conexao = ConexaoDB.conectar();
            PreparedStatement pstmt = conexao.prepareStatement(sql)){

            pstmt.setDouble(1, conta.getSaldo());
            pstmt.setString(2, conta.getNumeroDaConta());
            pstmt.executeUpdate();

            System.out.println("✅ Saldo sincronizado com o Banco de Dados SQLite!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar o saldo no banco: " + e.getMessage());
        }
    }

    public static void deletarConta(String numeroDaConta){
        String sql = "DELETE FROM contas WHERE numero= ?";

        try (Connection conexao = ConexaoDB.conectar();
            PreparedStatement pstmt = conexao.prepareStatement(sql)){

            pstmt.setString(1, numeroDaConta);
            pstmt.executeUpdate(); //Executa o DELETE

            System.out.println("✅ Conta removida do Banco de Dados com sucesso!");
        } catch (SQLException e){
            System.out.println("❌ Erro ao excluir a conta: " + e.getMessage());
        }
    }

    public static void salvarTransacao(String numeroConta, String descricao){
        String sql = "INSERT INTO transacoes (numero_conta, descricao) VALUES (?, ?)";

        try (Connection conexao = ConexaoDB.conectar();
        PreparedStatement pstmt = conexao.prepareStatement(sql)){

            pstmt.setString(1, numeroConta);
            pstmt.setString(2, descricao);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("❌ Erro ao salvar transação: " + e.getMessage());
        }
    }

    public static List<String> buscarExtrato(String numeroConta){
        List<String> historico = new ArrayList<>();
        String sql = "SELECT descricao FROM transacoes WHERE numero_conta = ?";

        try (Connection conexao = dia11.ConexaoDB.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)){

            pstmt.setString(1, numeroConta);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                historico.add(rs.getString("descricao"));
            }
        } catch (SQLException e){
            System.out.println("❌ Erro ao buscar extrato: " + e.getMessage());
        }

        return historico;
    }
}
