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
import com.example.martinsalerno.wikitest.classes.SessionHandler;
import com.example.martinsalerno.wikitest.fragments.ProfileFragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class SavePostTaskProfile extends AsyncTask <String, Void, Void> {
    private ProfileFragment fragment;
    Bitmap bitmapImage;

    public SavePostTaskProfile(ProfileFragment fragment) {
        this.fragment = fragment;
    }

    protected Void doInBackground(String... params) {
        bitmapImage = loadImageFromStorage(Uri.parse(params[0]));
        RequestHandler handler = new RequestHandler();
        try {
            handler.uploadImageUser(fragment, new SessionHandler(fragment.getContext()).getId(), bitmapImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        fragment.notifyImagePostFinished();
    }

    private Bitmap loadImageFromStorage(Uri photoURI) {
        try {
            Bitmap rawBitmap = BitmapFactory.decodeStream(fragment.getActivity().getContentResolver().openInputStream(photoURI), null, null);
            return rotateImageIfRequired(fragment.getActivity(), rawBitmap, photoURI);
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
