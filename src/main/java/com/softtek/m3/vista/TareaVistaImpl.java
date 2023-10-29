package com.softtek.m3.vista;


import com.softtek.m3.excepciones.DatosInvalidosException;
import com.softtek.m3.modelo.Tarea;
import com.softtek.m3.servicio.TareaServicio;

import java.util.Scanner;

public class TareaVistaImpl implements TareaVista{

    // Dependencias
    Scanner entrada = new Scanner(System.in);
    TareaServicio tareaServicio;

    public TareaVistaImpl(TareaServicio tareaServicio){
        this.tareaServicio = tareaServicio;
    }

    public void mostrarMenu(){

        System.out.println("\n\n~~ TODO List v2 ~~");

        boolean continuar = true; // Variable de control de flujo
        do {
            mostrarTareas();
            System.out.print("""
                    \nMenú:
                    1: Agregar una tarea.
                    2: Modificar una tarea.
                    3: Eliminar una tarea.
                    0: Salir.
                    Elija una opción:""");

            // Se captura el input del usuario
            String userInputOpcion = entrada.nextLine();

            // Se decide que camino seguir acorde a la elección del usuario
            switch (userInputOpcion) {
                case "1":
                    mostrarTareas();
                    break;
                case "2":
                    agregarTarea();
                    break;
                case "3":
                    modificarTarea();
                    break;
                case "4":
                    eliminarTarea();
                    break;

                    // Se cierra el programa
                case "0":
                    System.out.println("Muchas gracias por usar la aplicación! \nAutor: Nicolás Muros");
                    continuar = false;
                    break;

                    // Cuando se ingresa un dato no válido como opción, se muestra un mensaje de error
                default:
                    System.out.println("\nERROR: Ingrese una opción correcta.");
            }
        } while (continuar);
    }

    private void mostrarTareas(){
        System.out.println("\nTareas activas:");
        // Solicitud al servicio de la lista de tareas. Se recorre y se muestran en consola
        try {
            tareaServicio.obtenerTareas()
                    .forEach((t) -> System.out.println(
                            "ID: " + t.getId() + ", Titulo: " + t.getTitulo() + ", Descripción: " + t.getDescripcion() + "."));
        } catch (Exception e){
            mostrarError(e);
        }
    }

    private void agregarTarea(){

        try {
            System.out.print("Ingrese el título de la nueva tarea: ");
            String titulo = entrada.nextLine();

            System.out.print("\nIngrese la descripción de la nueva tarea: ");
            // Se captura la descripción
            String descripcion = entrada.nextLine();

            Tarea tarea = new Tarea(titulo, descripcion);
            // Se envía la descripción al servicio para que cargue la tarea en el repositorio
            tareaServicio.agregarTarea(tarea);

            mostrarExito("Tarea agregada exitosamente");
        } catch (Exception e){
            mostrarError(e);
        }
    }

    private void modificarTarea() {
        System.out.println("\nIngrese el ID de la tarea que quiere modificar");
        try {
            // Captura del ID
            Integer tareaId = Integer.parseInt(entrada.nextLine());

            // Se recupera la tarea original
            Tarea tarea = tareaServicio.obtenerTareaPorId(tareaId);

            // Se muestra al usuario la tarea original
            System.out.println("Tarea a modificar: ID: " + tarea.getId() +
                    ", Título: " + tarea.getTitulo() +
                    ", Descripción: " + tarea.getDescripcion() + ".");

            // Captura del nuevo título
            System.out.print("Ingrese el nuevo título: ");
            String titulo = entrada.nextLine();

            // Captura de la nueva descripción
            System.out.print("Ingrese la nueva descripción: ");
            String descripcion = entrada.nextLine();

            // Se crea la tarea
            Tarea nuevaTarea = new Tarea(tareaId, titulo, descripcion);

            // Se solicita al servicio modificar la tarea
            tareaServicio.modificarTarea(nuevaTarea);

            //Se muestra mensaje de éxito
            mostrarExito("Tarea agregada exitosamente.");

        } catch (NumberFormatException e) {
            mostrarError(new DatosInvalidosException("Por favor, ingrese un caracter válido."));
        }catch (Exception e){
            mostrarError(e);
        }
    }

    private void eliminarTarea(){
        System.out.println("\nIngrese el ID de la tarea que quiere eliminar: ");
        try {

            // Se captura el id de la tarea que se desea eliminar
            Integer tareaId = Integer.parseInt(entrada.nextLine());

            // Solicitud al servicio por los datos de la tarea
            Tarea tarea = tareaServicio.obtenerTareaPorId(tareaId);
            System.out.println("Tarea a eliminar: ID: " + tarea.getId() +
                    ", Título: " + tarea.getTitulo() +
                    ", Descripción: " + tarea.getDescripcion() + ".");

            // Solicitud al servicio de eliminar la tarea
            tareaServicio.eliminarTarea(tareaId);

            // Mostrar mensaje de éxito
            mostrarExito("Tarea eliminada exitosamente");
        } catch (NumberFormatException e) {
            mostrarError(new DatosInvalidosException("Por favor, ingrese un caracter válido."));
        }catch (Exception e){
            mostrarError(e);
        }
    }

    private void mostrarError(Exception error){
        System.out.println("\nERROR: " + error.getMessage());
    }

    private void mostrarExito(String message){
        System.out.println(message);
    }
}
