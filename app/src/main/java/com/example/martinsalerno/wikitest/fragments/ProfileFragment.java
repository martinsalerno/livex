package com.example.martinsalerno.wikitest.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.adapters.CompletePostAdapter;
import com.example.martinsalerno.wikitest.adapters.EventAdapter;
import com.example.martinsalerno.wikitest.classes.Event;
import com.example.martinsalerno.wikitest.classes.Friend;
import com.example.martinsalerno.wikitest.classes.Post;
import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.classes.SessionHandler;
import com.example.martinsalerno.wikitest.interfaces.PostsFragmentInterface;

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

    public ProfileFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePic = view.findViewById(R.id.userImage);
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
        friendsNumber.setText(Integer.toString(friends.length));
    }
}
