package com.example.martinsalerno.wikitest.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.martinsalerno.wikitest.BottomNavigationActivity;
import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.classes.Friend;
import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.classes.SessionHandler;
import com.example.martinsalerno.wikitest.fragments.FriendsFragment;
import com.example.martinsalerno.wikitest.tasks.LoadFriendTask;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolderFriend> {

    private Friend[] friends;
    private Context context;
    private FriendsFragment fragment;
    private BottomNavigationActivity activity;

    public FriendAdapter(Friend[] friends, FriendsFragment fragment){
        this.friends = friends;
        this.context = fragment.getContext();
        this.fragment = fragment;
        this.activity = (BottomNavigationActivity) fragment.getActivity();
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
        String username;
        Integer nextStatus;

        public ViewHolderFriend(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.addFriend = itemView.findViewById(R.id.agregarAmigo);
            this.locateFriend = itemView.findViewById(R.id.localizar);
            this.friendName = itemView.findViewById(R.id.friendName);
            this.friendImage = itemView.findViewById(R.id.friendImage);
            this.nextStatus = 0;
            addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new RequestHandler().addFriend(fragment.getContext(), friendId);
                    addFriend.setEnabled(false);
                    addFriend.setBackgroundColor(context.getResources().getColor(R.color.colorButtomDisabled));
                    if (nextStatus == 2) {
                        addFriend.setText("Solicitud Enviada");
                    }
                    else if (nextStatus == 3) {
                        addFriend.setText("Amigos");
                        locateFriend.setVisibility(View.VISIBLE);
                        locateFriend.setEnabled(true);
                    }
                }
            });

            this.locateFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new RequestHandler().getLocation(activity, friendId, username);
                }
            });
        }

        public void assignFriends(Friend friend) {
            this.friendId = friend.getId();
            this.username = friend.getUsername();
            Log.d("AMIGOS", Integer.toString(friend.getFriendStatus()));
            switch (friend.getFriendStatus()) {
                case 0:
                    addFriend.setText("Agregar");
                    locateFriend.setVisibility(View.GONE);
                    this.nextStatus = 2;
                    break;
                case 1:
                    addFriend.setText("Amigos");
                    addFriend.setEnabled(false);
                    addFriend.setBackgroundColor(context.getResources().getColor(R.color.colorButtomDisabled));
                    this.nextStatus = 3;
                    break;
                case 2:
                    addFriend.setText("Confirmar solicitud");
                    locateFriend.setVisibility(View.GONE);
                    this.nextStatus = 3;
                    break;
                case 3:
                    addFriend.setText("Solicitud enviada");
                    locateFriend.setVisibility(View.GONE);
                    addFriend.setEnabled(false);
                    this.nextStatus = 2;
                    addFriend.setBackgroundColor(context.getResources().getColor(R.color.colorButtomDisabled));
            }
            new RequestHandler().loadProfileImageAsync(itemView.getContext(), friendImage, friendId);
            friendName.setText(username);
            if (this.username.equals(new SessionHandler(context).getUsername())) {
                addFriend.setText("Amigos");
                locateFriend.setVisibility(View.VISIBLE);
                addFriend.setBackgroundColor(context.getResources().getColor(R.color.colorButtomDisabled));
                locateFriend.setBackgroundColor(context.getResources().getColor(R.color.colorButtomDisabled));
                addFriend.setEnabled(false);
                locateFriend.setEnabled(false);
            }
        }

        @Override
        public void onClick(final View view) {

        }
    }
}
