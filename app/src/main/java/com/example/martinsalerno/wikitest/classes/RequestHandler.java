package com.example.martinsalerno.wikitest.classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.se.omapi.Session;
import android.support.design.bottomappbar.BottomAppBarTopEdgeTreatment;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.example.martinsalerno.wikitest.BottomNavigationActivity;
import com.example.martinsalerno.wikitest.CreateUserActivity;
import com.example.martinsalerno.wikitest.LoginActivity;
import com.example.martinsalerno.wikitest.PostActivity;
import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.fragments.EventsFragment;
import com.example.martinsalerno.wikitest.fragments.FriendsFragment;
import com.example.martinsalerno.wikitest.fragments.HomeFragment;
import com.example.martinsalerno.wikitest.fragments.ProfileFragment;
import com.example.martinsalerno.wikitest.interfaces.PostsFragmentInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class RequestHandler {

    private final String BASE_URL = "http://livexws.sa-east-1.elasticbeanstalk.com/";
    private final String LOGIN_URL = BASE_URL + "auth";
    private final String EVENTS_URL = BASE_URL + "espectaculos/";
    private final String USERS_URL = BASE_URL + "usuarios/";
    private final String FRIEND_SEARCH_URL = USERS_URL + "buscar?username=";
    private final String POSTS_URL =  BASE_URL + "publicaciones/";
    private final String POSTS_IMAGES_URL = POSTS_URL + "imagenes/";
    private final String REGISTER_URL = USERS_URL + "registrar";
    private final Gson mapper = new Gson();

    public void logUser(final LoginActivity activity, final String username, String password) {
        JSONObject jsonBody = buildPayload(username, password);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, LOGIN_URL, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("LOGIN", response.toString());
                        activity.loginSucceeded(username, response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LOGIN", error.toString());
                        activity.loginFailed();
                    }
                });
        LivexExchange.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }

    public JSONObject buildPayload(String username, String password) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("username", username);
            payload.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    public void loadEvents(final EventsFragment fragment) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, EVENTS_URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Event[] eventos = mapper.fromJson(response.toString(), Event[].class);
                        fragment.setEvents(eventos);
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
                headers.put("Authorization", "Bearer " + new SessionHandler(fragment.getContext()).getToken());
                return headers;

            }
        };

        LivexExchange.getInstance(fragment.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void loadEventsProfile(final ProfileFragment fragment) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, EVENTS_URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Event[] eventos = mapper.fromJson(response.toString(), Event[].class);
                        fragment.setEvents(eventos);
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
                headers.put("Authorization", "Bearer " + new SessionHandler(fragment.getContext()).getToken());
                return headers;

            }
        };

        LivexExchange.getInstance(fragment.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void loadEventsFromPost(final PostActivity activity) {
        String path = USERS_URL + new SessionHandler(activity).getId() + "/espectaculos";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (com.android.volley.Request.Method.GET, path, null, new Response.Listener<JSONArray>() {
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

    public void addEvent(final Activity activity, String eventId) {
        String path = USERS_URL + new SessionHandler(activity).getId() + "/espectaculos/" + eventId;
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (com.android.volley.Request.Method.POST, path, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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

    public void loadPosts(String partialPath, final PostsFragmentInterface fragment) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET,  BASE_URL + partialPath, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());
                        Post[] posts = mapper.fromJson(response.toString(), Post[].class);
                        fragment.assignPosts(posts);
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
                headers.put("Authorization", "Bearer " + new SessionHandler(fragment.getContext()).getToken());
                return headers;

            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        LivexExchange.getInstance(fragment.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void loadUsers(final FriendsFragment fragment, String textToSearch) {
        Log.d("requester", "tamo bien");
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (com.android.volley.Request.Method.GET, FRIEND_SEARCH_URL + textToSearch, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Friend[] friends = mapper.fromJson(response.toString(), Friend[].class);
                        Log.d("requester", friends.toString());
                        fragment.setFriends(friends);
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
                headers.put("Authorization", "Bearer " + new SessionHandler(fragment.getContext()).getToken());
                return headers;

            }
        };

        LivexExchange.getInstance(fragment.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void loadFriends(final ProfileFragment fragment) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, USERS_URL + new SessionHandler(fragment.getContext()).getId() + "/friends", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Friend[] friends = mapper.fromJson(response.toString(), Friend[].class);
                        fragment.setFriends(friends);
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
                headers.put("Authorization", "Bearer " + new SessionHandler(fragment.getContext()).getToken());
                return headers;

            }
        };

        LivexExchange.getInstance(fragment.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void sendLocation(final Context context, final String userId, Double latitude, Double longitude) {
        JSONObject jsonBody = buildLocationPayload(latitude, longitude);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.PUT, USERS_URL + userId + "/posicion", jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) { }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("com.example.martinsalerno.wikitest.location not ok", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + new SessionHandler(context).getToken());
                return headers;

            }
        };

        LivexExchange.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void getLocation(final BottomNavigationActivity activity, final String userId, final String username) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, USERS_URL + userId + "/posicion", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Position position = mapper.fromJson(response.toString(), Position.class);
                        Log.d("ENTRE A LOCATION", response.toString());
                        activity.setFriendPosition(position, userId, username);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ENTRE A LOCATION error", error.toString());
                        activity.setFriendPositionNotOk();
                    }
                }) {
             @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + new SessionHandler(activity).getToken());
                return headers;

            }
        };
        Log.d("Strirtirrt", jsonObjectRequest.toString());
        LivexExchange.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }


    public JSONObject buildLocationPayload(Double latitude, Double longitude) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("latitud", latitude);
            payload.put("longitud", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    public void loadPostImage(Context context, ImageView image, String postId){
        GlideUrl glideUrl = new GlideUrl(POSTS_IMAGES_URL + postId, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + new SessionHandler(context).getToken())
                .build());
        RequestOptions myOptions = new RequestOptions()
                .timeout(10000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.image_not_available);
        Glide.with(context).load(glideUrl).apply(myOptions).into(image);
    }

    public Bitmap smallPostImage(String postId, Context context) {
        GlideUrl glideUrl = new GlideUrl(POSTS_IMAGES_URL + postId, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + new SessionHandler(context).getToken())
                .build());

        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .override(70, 80)
                .timeout(1000);
        try {
            return Glide.with(context).asBitmap().load(glideUrl).apply(myOptions).submit().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadProfileImage(ProfileFragment fragment, ImageView imageView, String userId) {
        GlideUrl glideUrl = new GlideUrl(USERS_URL + userId + "/profilePicture", new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + new SessionHandler(fragment.getActivity()).getToken())
                .build());
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .override(200, 200)
                .timeout(5000)
                .placeholder(R.drawable.background_profile_1)
                .error(R.drawable.background_profile_1);

        Glide.with(fragment).load(glideUrl).apply(myOptions).into(imageView);
    }

    public Bitmap loadSmallProfileImage(Activity activity, ImageView imageView, String userId) {
        GlideUrl glideUrl = new GlideUrl(USERS_URL + userId + "/profilePicture", new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + new SessionHandler(activity).getToken())
                .build());
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .override(70, 80).placeholder(R.drawable.background_profile_1);
        try {
            return Glide.with(activity).asBitmap().load(glideUrl).apply(myOptions).submit(70, 80).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createPost(final Activity activity, String eventId, String description) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JSONObject payload = buildPostPayload(new SessionHandler(activity).getId(), eventId, description);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(payload);
        Log.d("mapppeeoooo", jsonOutput);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, POSTS_URL, payload, future, future){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + new SessionHandler(activity).getToken());
                return headers;

            }
        };
        LivexExchange.getInstance(activity).addToRequestQueue(request);
        try {
            JSONObject response = future.get(50, TimeUnit.SECONDS);
            return response.getString("id");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "failed";
        } catch (ExecutionException e) {
            e.printStackTrace();
            return "failed";
        } catch (TimeoutException e) {
            e.printStackTrace();
            return "failed";
        } catch (JSONException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    public JSONObject buildPostPayload(String userId, String eventId, String description) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("userId", userId);
            payload.put("eventoId", eventId);
            payload.put("desc", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    public void addFriend(final Context context, String userId) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, USERS_URL + new SessionHandler(context).getId() + "/friends/" + userId, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + new SessionHandler(context).getToken());
                return headers;

            }
        };
        LivexExchange.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void loadEventImageSync(Context activity, ImageView imageView, String eventId) {
        GlideUrl glideUrl = new GlideUrl(EVENTS_URL + eventId + "/imagen", new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + new SessionHandler(activity).getToken())
                .build());
        Glide.with(activity).load(glideUrl).apply(new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.image_not_available)).into(imageView);
    }

    public Bitmap loadEventImage(Activity activity, String eventId) {
        GlideUrl glideUrl = new GlideUrl(EVENTS_URL + eventId + "/imagen", new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + new SessionHandler(activity).getToken())
                .build());
        try {
            return Glide.with(activity).asBitmap().load(glideUrl).submit().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadImageFromRef(Activity activity, ImageView imageView, String imageUrl) {
        Glide.with(activity).load(imageUrl).into(imageView);
    }

    public void uploadImage(Activity activity, String postId, Bitmap bitmap) throws IOException {
        String requestURL = POSTS_IMAGES_URL + postId;
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
        File file = new File(activity.getCacheDir(), postId);
        file.createNewFile();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40 , bos);
        byte[] bitmapdata = bos.toByteArray();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", postId, RequestBody.create(MediaType.parse("image/jpeg"), file))
                    .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(requestURL)
                    .addHeader("Authorization", "Bearer " + new SessionHandler(activity).getToken())
                    .put(requestBody)
                    .build();

        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response.body().string());

    }

    public void registerUser(final CreateUserActivity activity, final String username, String password) {
        JSONObject jsonBody = buildPayload(username, password);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, REGISTER_URL, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("USER CREATION", response.toString());
                        activity.userCreated();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("USER CREATION", error.toString());
                        activity.userNotCreated();
                    }
                });
        LivexExchange.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }

}