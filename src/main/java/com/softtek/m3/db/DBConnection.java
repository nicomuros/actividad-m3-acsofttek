package com.softtek.m3.db;

import java.sql.Connection;

public interface DBConnection {
    Connection establecerConexion();
    /**
     * Método para crear la tabla "todolist" en caso de que no exista en la base de datos
     */
    void createTable();
}
