package com.example.martinsalerno.wikitest.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
        TextView eventDate;
        TextView eventPresence;
        TextView eventArtists;
        TextView eventCommerces;
        ImageView eventImg;

        public ViewHolderEvent(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            eventName = itemView.findViewById(R.id.eventListName);
            eventImg = itemView.findViewById(R.id.eventListImg);
            eventDate = itemView.findViewById(R.id.eventDateList);
            eventPresence = itemView.findViewById(R.id.eventPresenceList);
            eventArtists = itemView.findViewById(R.id.eventArtistsList);
            eventCommerces = itemView.findViewById(R.id.eventCommercesList);
        }

        public void assignEvents(Event event) {
            eventName.setText(event.getNombre());
            eventDate.setText(event.getDateRange());
            eventPresence.setText(event.getIsGoingTo());
            if (event.getHasBeenTo()) {
                eventPresence.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_black_24dp, 0, 0, 0);
                eventPresence.setTextColor(context.getResources().getColor(R.color.colorPresenceGreen));
                setTextViewDrawableColor(eventPresence, R.color.colorPresenceGreen);
            } else {
                eventPresence.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                eventPresence.setTextColor(context.getResources().getColor(R.color.colorPresencerED));
                setTextViewDrawableColor(eventPresence, R.color.colorPresencerED);
            }
            if (event.getFunciones() == null) {
                eventArtists.setText("0 artistas");
            } else {
                eventArtists.setText(Integer.toString(event.getFunciones().size()) + " artistas");
            }
            if (event.getFunciones() == null) {
                eventCommerces.setText("0 comercios");
            } else {
                eventCommerces.setText(Integer.toString(event.getComercios().size()) + " comercios");
            }
            new RequestHandler().loadEventImageSync(context, eventImg, event.getId());
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

        private void setTextViewDrawableColor(TextView textView, int color) {
            for (Drawable drawable : textView.getCompoundDrawables()) {
                if (drawable != null) {
                    drawable.setColorFilter(new PorterDuffColorFilter(context.getColor(color), PorterDuff.Mode.SRC_IN));
                }
            }
        }
    }
}
