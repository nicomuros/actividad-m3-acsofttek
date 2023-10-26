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
     * @param titulo El título de la nueva tarea. No debe estar en blanco.
     * @param descripcion La descripción de la nueva tarea. No debe estar en blanco.
     * @throws DatosInvalidosException Si la descripción proporcionada está en blanco.
     */
    void agregarTarea(String titulo, String descripcion);

    /**
     * Obtiene una tarea por su ID.
     *
     * @param tareaId El ID de la tarea a buscar.
     * @return Un objeto de tipo Tarea que representa la tarea encontrada.
     * @throws RecursoNoEncontradoException Si no se encuentra una tarea con el ID especificado.
     */
    Tarea obtenerTareaPorId(Integer tareaId);

    /**
     * Modifica una tarea existente con la nueva descripción.
     *
     * @param nuevaDescripcion La nueva descripción de la tarea. No debe estar en blanco.
     * @param nuevoTitulo String del título que se va a actualizar. No debe estar en blanco
     * @param tareaId El ID de la tarea a modificar.
     * @throws DatosInvalidosException Si la descripción proporcionada está en blanco.
     * @throws RecursoNoEncontradoException Si no se encuentra una tarea con el ID especificado.
     */
    void modificarTarea(Integer tareaId, String nuevoTitulo, String nuevaDescripcion);

    /**
     * Elimina una tarea por su ID.
     *
     * @param tareaId El ID de la tarea a eliminar.
     * @throws RecursoNoEncontradoException Si no se encuentra una tarea con el ID especificado.
     */
    void eliminarTarea(Integer tareaId);
}
