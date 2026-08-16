package dia11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    //Método para abrir e devolver a conexão com o banco
    public static Connection conectar(){
        //Definindo o caminho
        String url = "jdbc:sqlite:banco_caixa_eletronico.db";
        Connection conexao = null;

        try {
            conexao = DriverManager.getConnection(url);
            System.out.println("✅ Conexão com o Banco de Dados estabelecida com sucesso!");
        } catch (SQLException e){
            System.out.println("❌ Erro ao conectar como Banco de Dados " + e.getMessage());
        }

        return conexao;
    }
}
