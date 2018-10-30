package com.example.martinsalerno.wikitest.classes;

public class Position {
    private Double longitud;
    private Double latitud;

    public Position(Double longitud, Double latitud) {
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public Double getLongitud(){
        return this.longitud;
    }

    public Double getLatitud(){
        return this.latitud;
    }
}
