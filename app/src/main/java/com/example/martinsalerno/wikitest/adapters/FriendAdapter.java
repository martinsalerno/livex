package com.example.martinsalerno.wikitest.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinsalerno.wikitest.R;

import java.util.Random;

import com.example.martinsalerno.wikitest.classes.Friend;
import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.fragments.FriendsFragment;
import com.example.martinsalerno.wikitest.tasks.LoadFriendTask;

import org.json.JSONObject;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolderFriend> {

    private Friend[] friends;
    private Context context;
    private FriendsFragment fragment;

    public FriendAdapter(Friend[] friends, FriendsFragment fragment){
        this.friends = friends;
        this.context = fragment.getContext();
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolderFriend onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_list, viewGroup, false);
        return new FriendAdapter.ViewHolderFriend(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolderFriend ViewHolderFriend, int i) {
        ViewHolderFriend.assignFriends(friends[i]);
        if (i >= (getItemCount() - 2)) {
            fragment.showRecycler();
        }
    }

    @Override
    public int getItemCount() {
        return friends.length;
    }

    public class ViewHolderFriend extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView friendName;
        Button addFriend;
        Button locateFriend;
        View itemView;
        ImageView friendImage;
        String friendId;

        public ViewHolderFriend(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.addFriend = itemView.findViewById(R.id.agregarAmigo);
            this.locateFriend = itemView.findViewById(R.id.localizar);
            this.friendName = itemView.findViewById(R.id.friendName);
            this.friendImage = itemView.findViewById(R.id.friendImage);
            addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new RequestHandler().addFriend(fragment.getContext(), friendId);
                    addFriend.setEnabled(false);
                    addFriend.setBackgroundColor(context.getResources().getColor(R.color.colorButtomDisabled));
                    addFriend.setText("Pendiente");
                }
            });

            this.locateFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject response = new RequestHandler().getLocation(context, friendId);
                    Log.d("RESPONSE LOCATION", response.toString());
                }
            });
        }

        public void assignFriends(Friend friend) {
            this.friendId = friend.getId();
            new LoadFriendTask(context, itemView).execute(friend.getId(), friend.getUsername());
        }

        @Override
        public void onClick(final View view) {

        }
    }
}
