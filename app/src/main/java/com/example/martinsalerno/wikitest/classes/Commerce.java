package com.example.martinsalerno.wikitest.classes;

import java.util.List;

public class Commerce {
    private String nombre;
    private List<CommerceItem> productos;
    private String imagenRef;
    private Position posicion;

    public Commerce(String nombre,  List<CommerceItem> productos, String imagenRef, Position posicion) {
        this.nombre = nombre;
        this.productos = productos;
        this.imagenRef = imagenRef;
        this.posicion = posicion;
    }

    public String getNombre(){
        return this.nombre;
    }

    public List<CommerceItem> getProductos() {
        return this.productos;
    }

    public String getImagenRef() { return this.imagenRef; }

    public Position getPosicion() {
        return this.posicion;
    }
}
