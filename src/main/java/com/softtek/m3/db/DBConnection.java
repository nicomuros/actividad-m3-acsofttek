package com.softtek.m3.db;

import java.sql.Connection;

public interface DBConnection {
    public Connection establecerConexion();
    public void createTable();
}
