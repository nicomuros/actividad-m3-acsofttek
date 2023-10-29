package com.softtek.m3.servicio;

import com.softtek.m3.db.MySQLConnection;
import com.softtek.m3.excepciones.DatosInvalidosException;
import com.softtek.m3.excepciones.RecursoNoEncontradoException;
import com.softtek.m3.modelo.Tarea;
import com.softtek.m3.repositorio.TareaRepositorio;
import com.softtek.m3.repositorio.TareaRepositorioImpl;

import java.util.List;

public class TareaServicioImpl implements TareaServicio {

    // Declaración de la dependencia que será inyectada
    TareaRepositorio tareaRepositorio;

    public TareaServicioImpl(TareaRepositorio tareaRepositorio){

        // Se instancia el repositorio inyectado
        this.tareaRepositorio = tareaRepositorio;
    }

    @Override
    public List<Tarea> obtenerTareas() {

        // Se solicita al repositorio todas las tareas y se retorna la respuesta
        return tareaRepositorio.seleccionarTodasLasTareas();
    }

    @Override
    public void agregarTarea(Tarea tarea) {

        // Validaciones
        if (tarea.getTitulo().isBlank() && tarea.getDescripcion().isBlank())
            throw new DatosInvalidosException("El titulo y la descripción no deben estar en blanco ni ser nulo.");
        if (tarea.getTitulo().isBlank())
            throw new DatosInvalidosException("El título no debe estar en blanco.");
        if (tarea.getDescripcion().isBlank())
            throw new DatosInvalidosException("La descripción no debe estar en blanco.");

        // Se solicita al repositorio agregar la nueva tarea
        tareaRepositorio.agregarTarea(tarea);
    }

    @Override
    public void modificarTarea(Tarea tareaModificada) {

        // Validaciones
        if (tareaModificada.getTitulo().isBlank() && tareaModificada.getDescripcion().isBlank())
            throw new DatosInvalidosException("El titulo y la descripción no deben estar en blanco ni ser nulo.");
        if (tareaModificada.getTitulo().isBlank())
            throw new DatosInvalidosException("El título no debe estar en blanco.");
        if (tareaModificada.getDescripcion().isBlank())
            throw new DatosInvalidosException("La descripción no debe estar en blanco.");

        // Se solicita la tarea original, para comparar y comprobar si hay modificaciones
        Tarea tarea = tareaRepositorio.seleccionarTareaPorId(tareaModificada.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró una tarea con el id: "
                        + tareaModificada.getId()));

        // Comparación de campos, uno por uno, de la tarea original con la modificada.
        boolean cambio = false;
        if (!tareaModificada.getTitulo().equals(tarea.getTitulo())){
            tarea.setTitulo(tareaModificada.getTitulo());
            cambio = true;
        }

        if (!tareaModificada.getDescripcion().equals(tarea.getDescripcion())){
            tarea.setDescripcion(tareaModificada.getDescripcion());
            cambio = true;
        }

        // Si no hubieron cambios, se lanza un error
        if (!cambio) throw new DatosInvalidosException("No se presentaron modificaciones");

        // Se solicita al repositorio modificar la tarea
        tareaRepositorio.modificarTarea(tarea);

    }

    public Tarea obtenerTareaPorId(Integer tareaId){

        // Se solicita al repositorio una tarea especifica. Si no existe, se lanza un error
        return tareaRepositorio.seleccionarTareaPorId(tareaId)
                .orElseThrow(() -> new RecursoNoEncontradoException
                        ("No se encontró una tarea con el id: " + tareaId));
    }
    @Override
    public void eliminarTarea(Integer tareaId) {

        // Si no existe la tarea que se desea modificar, se lanza un error
        if (!tareaRepositorio.existeTareaPorId(tareaId))
            throw new RecursoNoEncontradoException("No se encontró una tarea con el id: " + tareaId);

        // Solicitud al repositorio de eliminar la tarea
        tareaRepositorio.eliminarTareaPorId(tareaId);
    }
}
