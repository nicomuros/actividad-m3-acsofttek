package com.softtek.m3.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection implements DBConnection{
    @Override
    public Connection establecerConexion(){
        Connection connection = null;

        String usuario = "muros";
        String pass = "password";
        String db = "softtek";
        String ip = "localhost";
        String puerto = "3306";
        String url = "jdbc:mysql://" + ip + ":" + puerto + "/" + db;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, usuario, pass);
        } catch (ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
        }
        return connection;
    }

    @Override
    public void createTable(){

        Connection conn = establecerConexion();
        if (conn != null) {
            try {
                String query = "CREATE TABLE IF NOT EXISTS todolist(" +
                        "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "titulo VARCHAR(50) NOT NULL," +
                        "descripcion VARCHAR(50) NOT NULL)";

                Statement st = conn.createStatement();
                st.executeUpdate(query);
            } catch (SQLException e){
                System.out.println("e = " + e.getMessage());
            }
        }
    }
}
