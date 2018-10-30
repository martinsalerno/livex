package com.example.martinsalerno.wikitest.classes;

import java.util.List;

public class Event {
    private String nombre;
    private String id;
    private Place establecimiento;
    private List<Show> funciones;
    private List<Commerce> comercios;
    private Boolean hasBeenTo;

    public Event(String nombre, String id, Place establecimiento, List<Show> funciones, List<Commerce> comercios, Boolean hasBeenTo) {
        this.nombre = nombre;
        this.id = id;
        this.establecimiento = establecimiento;
        this.funciones = funciones;
        this.comercios = comercios;
        this.hasBeenTo = hasBeenTo;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getId() {
        return this.id;
    }

    public Place getEstablecimiento(){
        return this.establecimiento;
    }

    public List<Show> getFunciones(){
        return this.funciones;
    }

    public List<Commerce> getComercios() {
        return this.comercios;
    }

    public Boolean getHasBeenTo(){
        return this.hasBeenTo;
    }
}
