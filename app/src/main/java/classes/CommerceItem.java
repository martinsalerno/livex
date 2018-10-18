package classes;

class CommerceItem {
    private String nombre;
    private Integer precio;
    private Integer stock;

    public CommerceItem(String nombre, Integer precio, Integer stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public String getNombre(){
        return this.nombre;
    }

    public Integer getPrecio(){
        return this.precio;
    }

    public Integer getStock() {
        return this.stock;
    }
}
