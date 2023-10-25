package com.softtek.m3.repositorio;

import com.softtek.m3.modelo.Tarea;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * La interfaz TareaRepositorio define métodos para la transacción de datos con el repositorio establecido.
 */
public interface TareaRepositorio {

    /**
     * Agrega una tarea al repositorio.
     *
     * @param tarea La tarea que se agregará al repositorio.
     */
    void agregarTarea(Tarea tarea);

    /**
     * Obtiene una lista de todas las tareas en el repositorio.
     *
     * @return Lista de tareas en el repositorio.
     */
    List<Tarea> seleccionarTodasLasTareas();

    /**
     * Verifica si existe una tarea con el ID especificado en el repositorio.
     *
     * @param tareaId El ID de la tarea a verificar.
     * @return `true` si la tarea existe, de lo contrario, `false`.
     */
    Boolean existeTareaPorId(Integer tareaId);

    /**
     * Obtiene una tarea por su ID.
     *
     * @param tareaId El ID de la tarea a obtener.
     * @return Una instancia Optional de Tarea, que puede contener la tarea correspondiente al ID.
     */
    Optional<Tarea> seleccionarTareaPorId(Integer tareaId);

    /**
     * Modifica una tarea existente en el repositorio.
     *
     * @param tarea La tarea modificada que reemplazará a la tarea existente.
     */
    void modificarTarea(Tarea tarea);

    /**
     * Elimina una tarea del repositorio por su ID.
     *
     * @param tareaId El ID de la tarea a eliminar.
     */
    void eliminarTareaPorId(Integer tareaId);
}
