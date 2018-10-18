package classes;

import java.util.List;

public class Event {
    private String nombre;
    private String id;
    private Place establecimiento;
    private List<Show> funciones;
    private List<Commerce> comercios;

    public Event(String nombre, String id, Place establecimiento, List<Show> funciones, List<Commerce> comercios) {
        this.nombre = nombre;
        this.id = id;
        this.establecimiento = establecimiento;
        this.funciones = funciones;
        this.comercios = comercios;
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
}
