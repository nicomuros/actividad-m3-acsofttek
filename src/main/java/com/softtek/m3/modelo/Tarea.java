package com.softtek.m3.modelo;

import java.util.Objects;

public class Tarea {
    private Integer id;
    private String titulo;
    private String descripcion;

    public Tarea(){} // Constructor vacio, se usa para cargar los datos recibidos desde la base de datos
    public Tarea(String titulo, String descripcion){
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Tarea(Integer id, String titulo, String descripcion){
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    // Getters, setters, equals, hashcode, toString

    public void setId(Integer id){ this.id = id; }
    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarea tarea = (Tarea) o;
        return Objects.equals(id, tarea.id) && Objects.equals(titulo, tarea.titulo) && Objects.equals(descripcion, tarea.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descripcion);
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
