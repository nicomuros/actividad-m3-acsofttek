package com.softtek.m3;

import com.softtek.m3.db.DBConnection;
import com.softtek.m3.db.MySQLConnection;
import com.softtek.m3.repositorio.TareaRepositorio;
import com.softtek.m3.repositorio.TareaRepositorioImpl;
import com.softtek.m3.servicio.TareaServicio;
import com.softtek.m3.servicio.TareaServicioImpl;

public class Main {
    public static void main(String[] args) {
        DBConnection databaseConnection = new MySQLConnection();
        TareaRepositorio tareaRepositorio = new TareaRepositorioImpl(databaseConnection);
        TareaServicio tareaServicio = new TareaServicioImpl(tareaRepositorio);

        tareaServicio.obtenerTareas().forEach(System.out::println);
        tareaServicio.agregarTarea("Hola", "Prueba");
    }
}