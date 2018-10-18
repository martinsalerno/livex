package classes;

import java.util.List;

public class Commerce {
    private String nombre;
    private List<CommerceItem> productos;
    private String imagenRef;

    public Commerce(String nombre,  List<CommerceItem> productos, String imagenRef) {
        this.nombre = nombre;
        this.productos = productos;
        this.imagenRef = imagenRef;
    }

    public String getNombre(){
        return this.nombre;
    }

    public List<CommerceItem> getProductos() {
        return this.productos;
    }

    public String imagenRef() { return this.imagenRef; }
}
