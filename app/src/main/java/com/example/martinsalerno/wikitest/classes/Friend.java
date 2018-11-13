package com.example.martinsalerno.wikitest.classes;

public class Friend {
    private String username;
    private String id;
    private int friendStatus;

    public Friend(String username, String id, int friendStatus) {
        this.username = username;
        this.id = id;
        this.friendStatus = friendStatus;
    }

    public String getUsername(){
        return username;
    }

    public String getId(){
        return id;
    }

    public int getFriendStatus() { return friendStatus; }
}
