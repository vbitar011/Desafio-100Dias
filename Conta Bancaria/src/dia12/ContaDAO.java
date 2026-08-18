package dia12;

import dia01.Conta;
import dia02.ContaPoupanca;
import dia03.ContaCorrente;
import dia11.ConexaoDB;

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
                + "tipo TEXT NOT NULL"
                + ");";

        try (Connection conexao = ConexaoDB.conectar();
             Statement stmt = conexao.createStatement()){

            stmt.execute(sql);
            System.out.println("✅ Tabela de contas pronta para uso!");
        } catch (SQLException e){
            System.out.println("❌ Erro  ao criar a tabela " + e.getMessage());
        }
    }

    public static void salvarConta(Conta conta){
        String sql = "INSERT INTO contas (numero, titular, saldo, tipo) VALUES (?, ?, ?, ?)";

        try (Connection conexao = ConexaoDB.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)){

            pstmt.setString(1, conta.getNumeroDaConta());
            pstmt.setString(2, conta.getTitular());
            pstmt.setDouble(3, conta.getSaldo());

            if (conta instanceof ContaPoupanca){
                pstmt.setString(4, "POUPANCA");
            } else{
                pstmt.setString(4, "CORRENTE");
            }

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

                if (tipo.equals("POUPANCA")){
                    contasCarregadas.add(new ContaPoupanca(titular, titular, saldo, 0.05));
                } else {
                    contasCarregadas.add(new ContaCorrente(titular, numero, saldo));
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
}
