package classes;

import java.text.SimpleDateFormat;

public class Show {
    private String id;
    private Long fecha;
    private String artista;
    private String setList;
    private String imagenRef;

    public Show(String id, Long fecha, String artista, String setList, String imagenRef) {
        this.id = id;
        this.fecha = fecha;
        this.artista = artista;
        this.setList = setList;
        this.imagenRef = imagenRef;
    }

    public String getId() {
        return this.id;
    }

    public String getArtista(){
        return this.artista;
    }

    public String getFecha()
    {
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha).toString();
    }

    public String getSetList() {
        return setList.replaceAll("\n", ", ");
    }

    public String getImagenRef() { return imagenRef; }
}
