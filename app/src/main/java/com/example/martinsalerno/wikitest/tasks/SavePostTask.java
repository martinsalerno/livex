package com.example.martinsalerno.wikitest.tasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.martinsalerno.wikitest.PostActivity;
import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.classes.RequestHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class SavePostTask extends AsyncTask <String, Void, Void> {
    private PostActivity activity;
    Bitmap bitmapImage;

    public SavePostTask(PostActivity activity) {
        this.activity = activity;
    }

    protected Void doInBackground(String... params) {
        bitmapImage = loadImageFromStorage(Uri.parse(params[0]));
        String eventId = params[1];
        String description = params[2];
        RequestHandler handler = new RequestHandler();
        String postId = handler.createPost(activity, eventId, description);
        Log.d("POST SAVE TASK", postId);
        try {
            handler.uploadImage(activity, postId, bitmapImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        activity.notifyImagePostFinished();
    }

    private Bitmap loadImageFromStorage(Uri photoURI) {
        try {
            Bitmap rawBitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(photoURI), null, null);
            return rotateImageIfRequired(activity, rawBitmap, photoURI);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
