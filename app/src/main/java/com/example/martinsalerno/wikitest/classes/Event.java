package com.example.martinsalerno.wikitest.classes;

import android.util.Log;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Event {
    private String nombre;
    private String id;
    private Place establecimiento;
    private List<Show> funciones;
    private List<Commerce> comercios;
    private Boolean hasBeenTo;
    private String linkCompra;

    public Event(String nombre, String id, Place establecimiento, List<Show> funciones, List<Commerce> comercios, Boolean hasBeenTo, String linkCompra) {
        this.nombre = nombre;
        this.id = id;
        this.establecimiento = establecimiento;
        this.funciones = funciones;
        this.comercios = comercios;
        this.hasBeenTo = hasBeenTo;
        this.linkCompra = linkCompra;
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

    public String getLinkCompra() {
        return this.linkCompra;
    }

    public List<Date> getDates() {
        List<Date> dates = new ArrayList<>();
        for(int i = 0; i < funciones.size(); i++) {
            dates.add(funciones.get(i).getDate());
        }
        Collections.sort(dates);
        Log.d("DATES", dates.toString());
        return dates;
    }

    public String getDateRange() {
        if (funciones.size() == 0) {
            return "";
        }
        List<Date> dates = getDates();
        List<String> dateStrings = new ArrayList<>();
        for(int i = 0; i < dates.size(); i++) {
            dateStrings.add(new SimpleDateFormat("dd/MM/yy").format(dates.get(i)).toString());
        }
        String init = dateStrings.get(0);
        String last = dateStrings.get(dateStrings.size() - 1);
        if (init.equals(last)) {
            return init;
        }
        return (init + " - " + last);
    }

    public String getIsGoingTo() {
        Date today = new Date();
        int lastIndex = getDates().size() - 1;
        if (getDates().get(0).before(today)) {
            if (getHasBeenTo()) {
                return "Ticket escaneado";
            }
            else {
                return "Ticket no escaneado";
            }
        }

        if ((getDates().get(0).after(today) || getDates().get(0).equals(today)) && (getDates().get(lastIndex).before(today) | getDates().get(lastIndex).equals(today))) {
            if (getHasBeenTo()) {
                return "Ticket escaneado - Evento iniciado";
            }
            else {
                return "Ticket no escaneado - Evento iniciado";
            }
        }

        if (getDates().get(lastIndex).after(today)) {
            if (getHasBeenTo()) {
                return "Concurriste";
            }
            else {
                return "No concurriste";
            }
        }
        return "";
    }
}
