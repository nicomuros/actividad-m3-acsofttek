package com.softtek.m3.db;

import java.sql.Connection;

public interface DBConnection {
    Connection establecerConexion();
    void createTable();
}
