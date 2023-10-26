package com.softtek.m3.excepciones;

public class DatosInvalidosException extends RuntimeException{
    public DatosInvalidosException(String mensaje){
        super(mensaje);
    }
}
