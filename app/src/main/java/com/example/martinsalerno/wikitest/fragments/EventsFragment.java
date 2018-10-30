package com.example.martinsalerno.wikitest.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.martinsalerno.wikitest.BottomNavigationActivity;
import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.adapters.EventAdapter;
import com.example.martinsalerno.wikitest.classes.Event;
import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EventsFragment extends Fragment {

    private RecyclerView recycler;
    private ProgressBar progressBar;

    private Event[] events;
    public EventsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        progressBar = view.findViewById(R.id.loadingEvents);
        recycler = view.findViewById(R.id.recyclerEvents);
        recycler.setVisibility(View.INVISIBLE);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        loadEvents();
        return view;
    }

    public void loadEvents() {
        new RequestHandler().loadEvents(this);
    }

    public void setEvents(Event[] events){
        this.events = events;
        debugEvents(events);
        EventAdapter adapter = new EventAdapter(events, this);
        recycler.setAdapter(adapter);
    }

    public void showEvents() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.INVISIBLE);
        recycler.setVisibility(View.VISIBLE);
    }

    public void debugEvents(Event[] events) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(events);
        Log.d("mapppeeoooo", jsonOutput);
    }
}
