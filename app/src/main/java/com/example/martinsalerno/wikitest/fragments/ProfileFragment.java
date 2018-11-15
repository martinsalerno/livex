package com.example.martinsalerno.wikitest.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinsalerno.wikitest.PostActivity;
import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.adapters.CompletePostAdapter;
import com.example.martinsalerno.wikitest.adapters.EventAdapter;
import com.example.martinsalerno.wikitest.classes.Event;
import com.example.martinsalerno.wikitest.classes.Friend;
import com.example.martinsalerno.wikitest.classes.Post;
import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.classes.SessionHandler;
import com.example.martinsalerno.wikitest.interfaces.PostsFragmentInterface;
import com.example.martinsalerno.wikitest.tasks.SavePostTask;
import com.example.martinsalerno.wikitest.tasks.SavePostTaskProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.Random;

public class ProfileFragment extends Fragment implements PostsFragmentInterface {

    private ImageView profilePic;
    private ImageView backgroundPic;
    private TextView username;
    private TextView postsNumber;
    private TextView eventsNumber;
    private TextView friendsNumber;
    private TextView missingPost;
    private RecyclerView recyclerPosts;
    private ProgressBar progressBar;
    private static final int GALLERY_INTENT_CODE = 1;

    public ProfileFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePic = view.findViewById(R.id.userImage);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToLoadPhoto();
            }
        });
        backgroundPic = view.findViewById(R.id.profileBackground);
        username = view.findViewById(R.id.userName);
        postsNumber = view.findViewById(R.id.publicacionesNumber);
        eventsNumber = view.findViewById(R.id.espectaculosNumber);
        friendsNumber = view.findViewById(R.id.amigosNumber);
        recyclerPosts = view.findViewById(R.id.recyclerCompletePostsProfile);
        recyclerPosts.setVisibility(View.INVISIBLE);
        missingPost = view.findViewById(R.id.missingPostsProfile);
        missingPost.setVisibility(View.INVISIBLE);
        recyclerPosts.setHasFixedSize(true);
        recyclerPosts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        progressBar = view.findViewById(R.id.loadingPostsProfile);

        loadRandomBackground();
        loadProfileUsername();
        loadProfileImage();
        loadFriends();
        loadPosts();
        loadEvents();
        return view;
    }

    public void loadRandomBackground() {
        TypedArray profileImages = getResources().obtainTypedArray(R.array.profileImages);
        int i = new Random().nextInt(profileImages.length());
        int id = profileImages.getResourceId(i, -1);
        profileImages.recycle();
        backgroundPic.setImageResource(id);
    }

    public void loadProfileUsername() {
        String currentUsername = new SessionHandler(getActivity()).getUsername();
        username.setText(currentUsername);
    }

    public void loadProfileImage() {
        new RequestHandler().loadProfileImage(this, profilePic, new SessionHandler(getActivity()).getId());
    }

    public void loadFriends() {
        new RequestHandler().loadFriends(this);
    }

    public void loadPosts() {
        String path = "publicaciones/usuario/" + new SessionHandler(getContext()).getId();
        new RequestHandler().loadPosts(path, this);
    }

    @Override
    public void assignPosts(Post[] posts) {
        showRecycler();
        postsNumber.setText(Integer.toString(posts.length));
        if (posts.length == 0) {
            missingPost.setVisibility(View.VISIBLE);
        }
        CompletePostAdapter adapter = new CompletePostAdapter(posts, this);
        recyclerPosts.setAdapter(adapter);
    }

    @Override
    public void showRecycler() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerPosts.setVisibility(View.VISIBLE);
    }

    public void loadEvents() {
        new RequestHandler().loadEventsProfile(this);
    }

    public void setEvents(Event[] events){
        eventsNumber.setText(Integer.toString(events.length));
    }

    public void setFriends(Friend[] friends) {
        friendsNumber.setText(Integer.toString(friends.length ));
    }

    private void sendToLoadPhoto(){
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_INTENT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == GALLERY_INTENT_CODE) {
                Uri picUri = data.getData();
                new SavePostTaskProfile(this).execute(picUri.toString());
            }
        }
    }

    public void notifyImagePostFinished() {
        Toast.makeText(getContext(), "Imagen subida", Toast.LENGTH_LONG).show();
        loadProfileImage();
    }
}
