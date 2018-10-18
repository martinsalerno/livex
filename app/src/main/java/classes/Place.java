package classes;

public class Place {
    private String nombre;
    private String id;
    private String direccion;
    private Integer capacidad;

    public Place(String nombre, String id, String direccion, Integer capacidad) {
        this.nombre = nombre;
        this.id = id;
        this.direccion = direccion;
        this.capacidad = capacidad;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getId(){
        return this.id;
    }

    public String getDireccion(){
        return this.direccion;
    }

    public Integer getCapacidad() {
        return this.capacidad;
    }
}
