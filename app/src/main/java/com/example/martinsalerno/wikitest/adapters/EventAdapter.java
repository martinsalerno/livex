package com.example.martinsalerno.wikitest.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.EventActivity;
import com.example.martinsalerno.wikitest.fragments.EventsFragment;
import com.example.martinsalerno.wikitest.fragments.FriendsFragment;
import com.example.martinsalerno.wikitest.tasks.LoadEventTask;
import com.google.gson.Gson;

import com.example.martinsalerno.wikitest.classes.Event;
import com.example.martinsalerno.wikitest.classes.RequestHandler;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolderEvent> {

    private Event[] events;
    private Context context;
    private EventsFragment fragment;


    public EventAdapter(Event[] events, EventsFragment fragment){
        this.events = events;
        this.context = fragment.getContext();
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolderEvent onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_list, viewGroup, false);
        return new ViewHolderEvent(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolderEvent viewHolderEvent, int i) {
        viewHolderEvent.assignEvents(events[i]);
        Log.d("ADAPTER eve", "ITEM COUNT: " + Integer.toString(getItemCount()));
        Log.d("ADAPTER eve", "I: " + Integer.toString(i));

        if (i == (getItemCount() - 2)) {
            Log.d("ADAPTER", "ENTRE");
            fragment.showEvents();
        }
    }

    @Override
    public int getItemCount() {
        return events.length;
    }

    public class ViewHolderEvent extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView eventName;
        ImageView eventImg;
        public ViewHolderEvent(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            eventName = itemView.findViewById(R.id.eventListName);
            eventImg = itemView.findViewById(R.id.eventListImg);
        }

        public void assignEvents(Event event) {
            eventName.setText(event.getNombre());
            new RequestHandler().loadEventImageSync(context, eventImg, event.getId());
            //new LoadEventTask(context, itemView).execute(event.getId(), event.getNombre());
        }

        @Override
        public void onClick(final View view) {
            Event event = events[getAdapterPosition()];
            Intent intent = new Intent(context, EventActivity.class);
            Gson gson = new Gson();
            String eventJson = gson.toJson(event);
            intent.putExtra("event", eventJson);
            context.startActivity(intent);
        }

    }
}
