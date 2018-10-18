package classes;

public class Friend {
    private String username;
    private String id;

    public Friend(String username, String id) {
        this.username = username;
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public String getId(){
        return id;
    }
}
