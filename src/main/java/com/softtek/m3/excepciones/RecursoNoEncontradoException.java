package com.softtek.m3.excepciones;

public class RecursoNoEncontradoException extends RuntimeException{
    public RecursoNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
