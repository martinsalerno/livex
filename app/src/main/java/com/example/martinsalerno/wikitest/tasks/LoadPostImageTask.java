package com.example.martinsalerno.wikitest.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.classes.RequestHandler;

public class LoadPostImageTask extends AsyncTask<String, Void, Void> {
    private Context context;
    ImageView postImage;
    Bitmap bitmapPost;

    public LoadPostImageTask(Context context, ImageView postImage) {
        this.context = context;
        this.postImage = postImage;
    }

    protected Void doInBackground(String... params) {
        //bitmapPost = new RequestHandler().loadPostImage(params[0], context);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        postImage.setImageBitmap(bitmapPost);
    }
}