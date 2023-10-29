package com.softtek.m3.db;

import com.softtek.m3.excepciones.RecursoNoEncontradoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection implements DBConnection{
    @Override
    public Connection establecerConexion(){

        // Se inicia la conexión como null
        Connection connection = null;

        // Configuración de JDBC
        String usuario = "muros";
        String pass = "password";
        String db = "softtek";
        String ip = "localhost";
        String puerto = "3306";
        String url = "jdbc:mysql://" + ip + ":" + puerto + "/" + db;

        try {
            // Se carga el driver de mysql en la memoria
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Se establece la conexión
            connection = DriverManager.getConnection(url, usuario, pass);

            // Si no se encuentra la clase, o ocurre una excepción, se lanza un error
        } catch (ClassNotFoundException e){
            System.out.println("ClassNotFoundException: " + e.getMessage());
        } catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }

        // Se retorna la conexión
        return connection;
    }

    public void probarConexion(){
        try (Connection conn = establecerConexion()) {
            System.out.println("Conexión a base de datos exitosa");
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }

    @Override
    public void createTable(){

        // Se establece la conexión
        Connection conn = establecerConexion();
        if (conn != null) {
            try {

                // Consulta SQL
                String query = "CREATE TABLE IF NOT EXISTS todolist(" +
                        "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "titulo VARCHAR(50) NOT NULL," +
                        "descripcion VARCHAR(50) NOT NULL)";
                Statement st = conn.createStatement();

                // Se ejecuta la consulta
                st.executeUpdate(query);
            } catch (SQLException e){
                System.out.println("e = " + e.getMessage());
            }
        }
    }
}
