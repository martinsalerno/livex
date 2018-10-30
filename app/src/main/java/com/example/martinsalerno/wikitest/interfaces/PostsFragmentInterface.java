package com.example.martinsalerno.wikitest.interfaces;

import android.content.Context;

import com.example.martinsalerno.wikitest.classes.Post;

public interface PostsFragmentInterface {
    void loadPosts();
    void assignPosts(Post[] posts);
    void showRecycler();
    Context getContext();
}
