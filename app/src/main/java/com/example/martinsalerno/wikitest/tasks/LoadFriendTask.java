package com.example.martinsalerno.wikitest.tasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.classes.RequestHandler;


public class LoadFriendTask extends AsyncTask <String, Void, Void> {
    private Context context;
    TextView friendName;
    ImageView friendImage;
    Bitmap bitmapProfile;
    String friendNameText;

    public LoadFriendTask(Context context, View itemView) {
        this.context = context;
        this.friendName = itemView.findViewById(R.id.friendName);
        this.friendImage = itemView.findViewById(R.id.friendImage);
    }

    protected Void doInBackground(String... params) {
        bitmapProfile = new RequestHandler().loadSmallProfileImage((Activity) context, friendImage, params[0]);
        friendNameText = params[1];
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        friendImage.setImageBitmap(bitmapProfile);
        friendName.setText(friendNameText);
    }
}
