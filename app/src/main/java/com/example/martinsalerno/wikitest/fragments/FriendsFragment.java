package com.example.martinsalerno.wikitest.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.adapters.FriendAdapter;
import com.example.martinsalerno.wikitest.classes.Friend;
import com.example.martinsalerno.wikitest.classes.RequestHandler;

import org.w3c.dom.Text;

public class FriendsFragment extends Fragment implements View.OnClickListener {
    EditText searchFriends;
    RecyclerView recyclerFriends;
    Button buttonSearchFriends;
    ProgressBar progressBar;
    TextView temporaryText;
    public FriendsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        temporaryText = view.findViewById(R.id.temporaryFriendText);
        progressBar = view.findViewById(R.id.loadingFriends);
        progressBar.setVisibility(View.GONE);
        searchFriends = view.findViewById(R.id.buscarAmigo);
        buttonSearchFriends = view.findViewById(R.id.buscarAmigoButton);
        buttonSearchFriends.setOnClickListener(this);
        recyclerFriends = view.findViewById(R.id.recyclerBuscar);
        recyclerFriends.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    public void setFriends(Friend[] friendsFound){
        FriendAdapter adapter = new FriendAdapter(friendsFound, this);
        recyclerFriends.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerFriends.setVisibility(View.INVISIBLE);
        temporaryText.setVisibility(View.GONE);
        new RequestHandler().loadUsers(this, searchFriends.getText().toString());
    }

    public void showRecycler() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.INVISIBLE);
        recyclerFriends.setVisibility(View.VISIBLE);
    }
}
