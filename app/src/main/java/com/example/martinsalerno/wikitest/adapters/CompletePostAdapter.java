package com.example.martinsalerno.wikitest.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.classes.Post;
import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.fragments.HomeFragment;
import com.example.martinsalerno.wikitest.interfaces.PostsFragmentInterface;
import com.example.martinsalerno.wikitest.tasks.LoadPostImageTask;

import java.util.List;

public class CompletePostAdapter extends RecyclerView.Adapter<CompletePostAdapter.ViewHolderCompletePost> {

    private Post[] posts;
    private Context context;
    private PostsFragmentInterface fragment;

    public CompletePostAdapter(Post[] posts, PostsFragmentInterface fragment){
        this.posts = posts;
        this.context = fragment.getContext();
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public CompletePostAdapter.ViewHolderCompletePost onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.complete_post_list, viewGroup, false);
        return new CompletePostAdapter.ViewHolderCompletePost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletePostAdapter.ViewHolderCompletePost viewHolderCompletePost, int i) {
        viewHolderCompletePost.assignPost(this.posts[i]);
    }

    @Override
    public int getItemCount() {
        return this.posts.length;
    }

    public class ViewHolderCompletePost extends RecyclerView.ViewHolder {
        TextView username;
        TextView eventName;
        TextView postDate;
        TextView description;
        ImageView postImage;

        public ViewHolderCompletePost(@NonNull View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            this.username = itemView.findViewById(R.id.completePostUsername);
            this.eventName = itemView.findViewById(R.id.completePostEventName);
            this.postDate = itemView.findViewById(R.id.completePostDate);
            this.description = itemView.findViewById(R.id.completePostDescription);
            this.postImage = itemView.findViewById(R.id.completePostImage);
        }

        public void assignPost(Post post) {
            username.setText(post.getUsername());
            eventName.setText(post.getEspectaculoName());
            postDate.setText(post.getFecha());
            description.setText(post.getDesc());
            new RequestHandler().loadPostImage(context, postImage, post.getId());
        }
    }
}