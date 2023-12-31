package com.softtek.m3;

import com.softtek.m3.db.DBConnection;
import com.softtek.m3.db.MySQLConnection;
import com.softtek.m3.repositorio.TareaRepositorio;
import com.softtek.m3.repositorio.TareaRepositorioImpl;
import com.softtek.m3.servicio.TareaServicio;
import com.softtek.m3.servicio.TareaServicioImpl;
import com.softtek.m3.vista.TareaVista;
import com.softtek.m3.vista.TareaVistaImpl;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        try {
            // Instanciación de las dependencias, siguiendo el patrón de Inyección de Dependencias
            DBConnection databaseConnection = new MySQLConnection();

            // Se comprueba si la base de datos está disponible y la conexión es exitosa
            databaseConnection.probarConexion();

            // Crear tabla si no existe
            databaseConnection.createTable();

            // Se crean el resto de las dependencias
            TareaRepositorio tareaRepositorio = new TareaRepositorioImpl(databaseConnection);
            TareaServicio tareaServicio = new TareaServicioImpl(tareaRepositorio);

            // Se instancia el formulario, inyectandose la dependencia del servicio

            TareaVista vista = new TareaVistaImpl(tareaServicio);
            vista.mostrarMenu();

        // Manejo de errores generales de la aplicación
        } catch (Exception e){
            System.out.println("e = " + e);
        }
    }

}