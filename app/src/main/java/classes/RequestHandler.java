package classes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.se.omapi.Session;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.example.martinsalerno.wikitest.EventsActivity;
import com.example.martinsalerno.wikitest.FriendsActivity;
import com.example.martinsalerno.wikitest.ProfileActivity;
import com.example.martinsalerno.wikitest.R;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class RequestHandler {

    private final String EVENTS_URL = "http://livexws.sa-east-1.elasticbeanstalk.com/espectaculos/";
    private final String FRIEND_SEARCH_URL = "http://livexws.sa-east-1.elasticbeanstalk.com/usuarios/buscar?username=";
    private final String USERS_URL = "http://livexws.sa-east-1.elasticbeanstalk.com/usuarios/";
    private final Gson mapper = new Gson();

    public void loadEvents(final EventsActivity activity) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, EVENTS_URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Event[] eventos = mapper.fromJson(response.toString(), Event[].class);
                        activity.setEvents(eventos);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mapper not ok", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + new SessionHandler(activity).getToken());
                return headers;

            }
        };

        LivexExchange.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }

    public void loadUsers(final FriendsActivity activity, String textToSearch) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, FRIEND_SEARCH_URL + textToSearch, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Friend[] friends = mapper.fromJson(response.toString(), Friend[].class);
                        activity.setFriends(friends);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mapper not ok", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + new SessionHandler(activity).getToken());
                return headers;

            }
        };

        LivexExchange.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }

    public void loadFriends(final FriendsActivity activity) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, USERS_URL + new SessionHandler(activity).getId() + "/friends", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Friend[] friends = mapper.fromJson(response.toString(), Friend[].class);
                        activity.setFriends(friends);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mapper not ok", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + new SessionHandler(activity).getToken());
                return headers;

            }
        };

        LivexExchange.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }

    public void loadProfileImage(Activity activity, ImageView imageView, String userId) {
        GlideUrl glideUrl = new GlideUrl(USERS_URL + userId + "/profilePicture", new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + new SessionHandler(activity).getToken())
                .build());
        Glide.with(activity).load(glideUrl).into(imageView);
    }

    public void loadSmallProfileImage(Activity activity, ImageView imageView, String userId) {
        GlideUrl glideUrl = new GlideUrl(USERS_URL + userId + "/profilePicture", new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + new SessionHandler(activity).getToken())
                .build());
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .override(70, 80);
        Glide.with(activity).load(glideUrl).apply(myOptions).into(imageView);
    }

    public void loadEventImage(Activity activity, ImageView imageView, String eventId) {
        GlideUrl glideUrl = new GlideUrl(EVENTS_URL + eventId + "/imagen", new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + new SessionHandler(activity).getToken())
                .build());
        Glide.with(activity).load(glideUrl).into(imageView);
    }

    public void loadImageFromRef(Activity activity, ImageView imageView, String imageUrl) {
        Glide.with(activity).load(imageUrl).into(imageView);
    }
}