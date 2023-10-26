package com.softtek.m3;

import com.softtek.m3.db.DBConnection;
import com.softtek.m3.db.MySQLConnection;
import com.softtek.m3.repositorio.TareaRepositorio;
import com.softtek.m3.repositorio.TareaRepositorioImpl;
import com.softtek.m3.servicio.TareaServicio;
import com.softtek.m3.servicio.TareaServicioImpl;
import com.softtek.m3.vista.TareaForm;

public class Main {
    public static void main(String[] args) {
        // Instanciación de las dependencias, siguiendo el patrón de Inyección de Dependencias
        DBConnection databaseConnection = new MySQLConnection();
        TareaRepositorio tareaRepositorio = new TareaRepositorioImpl(databaseConnection);
        TareaServicio tareaServicio = new TareaServicioImpl(tareaRepositorio);

        // Se crea la tabla 'todolist' en caso de que no exista
        databaseConnection.createTable();

        // Se instancia el formulario, inyectandose la dependencia del servicio
        TareaForm tareaForm = new TareaForm(tareaServicio);
    }
}