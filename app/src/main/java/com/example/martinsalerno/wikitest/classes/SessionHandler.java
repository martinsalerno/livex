package com.example.martinsalerno.wikitest.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionHandler {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;
    private final String PREF_NAME = "session";

    public SessionHandler(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        this.editor = prefs.edit();
    }

    public void setSession(String usename, String id, String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", usename);
        editor.putString("id", id);
        editor.putString("token", token);
        editor.putBoolean("loggedIn", true);
        editor.commit();
    }

    public String getUsername() {
        return this.prefs.getString("username", null);
    }

    public String getId() {
        return this.prefs.getString("id", null);
    }

    public String getToken() {
        return this.prefs.getString("token", null);
    }

    public Boolean isLoggedIn() {
        return this.prefs.getBoolean("loggedIn", false);
    }

    public void logOut() {
        this.editor.clear();
        this.editor.commit();
    }

}
