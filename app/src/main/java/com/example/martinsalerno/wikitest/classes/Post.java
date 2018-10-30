package com.example.martinsalerno.wikitest.classes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Post {
    private String id;
    private String desc;
    private Long created;
    private String username;
    private String userId;
    private String eventoId;
    private String espectaculoName;
    private Boolean isMine;

    public Post(String id, String description, String userId, String username, String eventoId, String espectaculoName, Long created, Boolean isMine) {
        this.id = id;
        this.desc = description;
        this.userId = userId;
        this.username = username;
        this.eventoId = eventoId;
        this.espectaculoName = espectaculoName;
        this.created = created;
        this.isMine = isMine;
    }

    public String getId() {
        return this.id;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getEspectaculoName() {
        if(espectaculoName.equals("NULL event")) {
            return "";
        }
        return this.espectaculoName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFecha() {
        return new SimpleDateFormat("dd/MM/yyyy").format(created).toString();
    }

}
