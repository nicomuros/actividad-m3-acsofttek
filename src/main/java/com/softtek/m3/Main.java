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

        try {

            // Instanciación de las dependencias, siguiendo el patrón de Inyección de Dependencias
            DBConnection databaseConnection = new MySQLConnection();

            // Se comprueba si la base de datos está disponible y la conexión es exitosa
            if (databaseConnection.probarConexion()) {
                // Crear tabla si no existe
                databaseConnection.createTable();

                // Se crean el resto de las dependencias
                TareaRepositorio tareaRepositorio = new TareaRepositorioImpl(databaseConnection);
                TareaServicio tareaServicio = new TareaServicioImpl(tareaRepositorio);

                // Se instancia el formulario, inyectandose la dependencia del servicio
                TareaForm tareaFormOld = new TareaForm(tareaServicio);

            // Si la prueba de conexión falla, se devuelve un mensaje.
            } else {
                System.out.println("La base de datos 'softtek' no está disponible. " +
                        "El error puede deberse a que:" +
                        "\n* MySQL no se encuentra en ejecución." +
                        "\n* Las credenciales son incorrectas." +
                        "\n* MySQL no se encuentra escuchando el puerto 3306." +
                        "\n* No existe una base de datos con el nombre 'softtek'");
            }

        // Manejo de errores generales de la aplicación
        } catch (Exception e){
            System.out.println("e = " + e);
        }
    }
}