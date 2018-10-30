package com.example.martinsalerno.wikitest.tasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.classes.RequestHandler;

public class LoadEventTask extends AsyncTask<String, Void, Void> {
    private Context context;
    TextView eventName;
    ImageView eventImg;
    String eventNameText;
    Bitmap bitmapEvent;

    public LoadEventTask(Context context, View itemView) {
        this.context = context;
        this.eventName = itemView.findViewById(R.id.eventListName);
        this.eventImg = itemView.findViewById(R.id.eventListImg);
    }

    protected Void doInBackground(String... params) {
        bitmapEvent = new RequestHandler().loadEventImage((Activity) context, params[0]);
        eventNameText = params[1];
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        eventImg.setImageBitmap(bitmapEvent);
        eventName.setText(eventNameText);
    }
}