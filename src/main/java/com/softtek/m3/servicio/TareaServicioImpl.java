package com.softtek.m3.servicio;

import com.softtek.m3.excepciones.DatosInvalidosException;
import com.softtek.m3.excepciones.RecursoNoEncontradoException;
import com.softtek.m3.modelo.Tarea;
import com.softtek.m3.repositorio.TareaRepositorio;
import com.softtek.m3.repositorio.TareaRepositorioImpl;

import java.util.List;

public class TareaServicioImpl implements TareaServicio {

    TareaRepositorio repositorio = new TareaRepositorioImpl();

    @Override
    public List<Tarea> obtenerTareas() {
        return repositorio.seleccionarTodasLasTareas();
    }

    @Override
    public void agregarTarea(String titulo, String descripcion) {
        if (titulo.isBlank() && descripcion.isBlank()) throw new DatosInvalidosException("El titulo y la descripción no deben estar en blanco ni ser nulo.");
        if (titulo.isBlank()) throw new DatosInvalidosException("El título no debe estar en blanco.");
        if (descripcion.isBlank()) throw new DatosInvalidosException("La descripción no debe estar en blanco.");

        Tarea tarea = new Tarea(titulo, descripcion);
        repositorio.agregarTarea(tarea);
    }

    @Override
    public Tarea obtenerTareaPorId(Integer tareaId) {
        return repositorio.seleccionarTareaPorId(tareaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró una tarea con el id: " + tareaId));
    }

    @Override
    public void modificarTarea(Integer tareaId, String nuevoTitulo, String nuevaDescripcion) {
        if (nuevoTitulo.isBlank() && nuevaDescripcion.isBlank()) throw new DatosInvalidosException("El titulo y la descripción no deben estar en blanco ni ser nulo.");
        if (nuevoTitulo.isBlank()) throw new DatosInvalidosException("El título no debe estar en blanco.");
        if (nuevaDescripcion.isBlank()) throw new DatosInvalidosException("La descripción no debe estar en blanco.");

        Tarea tarea = repositorio.seleccionarTareaPorId(tareaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró una tarea con el id: " + tareaId));

        boolean cambio = false;

        if (!nuevoTitulo.equals(tarea.getTitulo())){
            tarea.setTitulo(nuevoTitulo);
            cambio = true;
        }

        if (!nuevaDescripcion.equals(tarea.getDescripcion())){
            tarea.setDescripcion(nuevaDescripcion);
            cambio = true;
        }

        if (!cambio) throw new DatosInvalidosException("No se presentaron modificaciones");

        repositorio.modificarTarea(tarea);

    }

    @Override
    public void eliminarTarea(Integer tareaId) {
        if (!repositorio.existeTareaPorId(tareaId)) throw new RecursoNoEncontradoException("No se encontró una tarea con el id: " + tareaId);
        repositorio.eliminarTareaPorId(tareaId);
    }
}
