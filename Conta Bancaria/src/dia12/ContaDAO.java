package dia12;

import dia01.Conta;
import dia02.ContaPoupanca;
import dia11.ConexaoDB;

import java.awt.desktop.SystemSleepEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
                pstmt.setString(4, "POUPANÇA");
            } else{
                pstmt.setString(4, "CORRENTE");
            }

            pstmt.executeUpdate();
            System.out.println("✅ Conta " + conta.getNumeroDaConta() + "salva no Banco de Dados!");
        } catch (SQLException e){
            System.out.println("Erro ao salvar a conta: " + e.getMessage());
        }
    }
}
