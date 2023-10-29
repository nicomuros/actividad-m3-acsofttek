package com.softtek.m3.servicio;

import com.softtek.m3.modelo.Tarea;
import com.softtek.m3.excepciones.DatosInvalidosException;
import com.softtek.m3.excepciones.RecursoNoEncontradoException;

import java.util.List;

/**
 * TareaServicio define operaciones para gestionar tareas.
 * Esta capa se encarga de realizar las validaciones de los datos ingresados por el usuario, y de
 * lanzar errores cuando sea necesario. Si procede exitosamente, se comunica con la capa de repositorio
 */
public interface TareaServicio {

    /**
     * Obtiene una lista de todas las tareas disponibles.
     *
     * @return Una lista de objetos de tipo Tarea que representan las tareas.
     *         Si no hay tareas disponibles, la lista estará vacía.
     */
    List<Tarea> obtenerTareas();

    /**
     * Agrega una nueva tarea con la descripción especificada.
     *
     * @param tarea Objeto Tarea que se solicita se añada al repositorio
     * @throws DatosInvalidosException Si la descripción y/o el título proporcionado está en blanco.
     */
    void agregarTarea(Tarea tarea);


    /**
     * Obtiene una tarea por su ID.
     *
     * @param tareaId El ID de la tarea a obtener.
     * @return La tarea correspondiente al ID.
     * @throws RecursoNoEncontradoException Si no se encuentra una tarea con el ID especificado.
     */
    Tarea obtenerTareaPorId(Integer tareaId);

    /**
     * Modifica una tarea existente con la nueva descripción.
     *
     * @param tareaModificada Tarea con ID, titulo y descripción, que representa el nuevo estado

     * @throws DatosInvalidosException Si la descripción proporcionada está en blanco.
     * @throws RecursoNoEncontradoException Si no se encuentra una tarea con el ID especificado.
     */

    void modificarTarea(Tarea tareaModificada);

    /**
     * Elimina una tarea por su ID.
     *
     * @param tareaId El ID de la tarea a eliminar.
     * @throws RecursoNoEncontradoException Si no se encuentra una tarea con el ID especificado.
     */
    void eliminarTarea(Integer tareaId);
}
