package com.example.martinsalerno.wikitest.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.martinsalerno.wikitest.PostActivity;
import com.example.martinsalerno.wikitest.R;
import com.example.martinsalerno.wikitest.adapters.CompletePostAdapter;
import com.example.martinsalerno.wikitest.adapters.FriendAdapter;
import com.example.martinsalerno.wikitest.classes.Post;
import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.classes.SessionHandler;
import com.example.martinsalerno.wikitest.interfaces.PostsFragmentInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment implements View.OnClickListener, PostsFragmentInterface {
    private FloatingActionButton fabAdd, fabTake, fabUpload;
    private RecyclerView recyclerPosts;
    private ProgressBar progressbar;
    private final float TRANSLATIONY = 100f;
    private static final int PHOTO_INTENT_CODE = 0;
    private static final int GALLERY_INTENT_CODE = 1;
    private boolean isMenuOpen = false;
    private Toolbar toolbar;
    private String currentImagePath;
    private Uri photoURI;
    private TextView missingPost;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressbar = view.findViewById(R.id.loadingPosts);
        recyclerPosts = view.findViewById(R.id.recyclerCompletePosts);
        recyclerPosts.setVisibility(View.INVISIBLE);
        recyclerPosts.setHasFixedSize(true);
        recyclerPosts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        missingPost = view.findViewById(R.id.missingPosts);
        missingPost.setVisibility(View.INVISIBLE);
        initFabs(view);
        toolbar = view.findViewById(R.id.toolbarHome);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        textView.setText("Livex");
        loadPosts();
        return view;
    }

    public void initFabs(View view) {
        fabAdd = view.findViewById(R.id.addPhoto);
        fabTake = view.findViewById(R.id.takePhoto);
        fabUpload = view.findViewById(R.id.uploadPhoto);

        fabAdd.setOnClickListener(this);
        fabTake.setOnClickListener(this);
        fabUpload.setOnClickListener(this);

        fabTake.setVisibility(View.VISIBLE);
        fabUpload.setVisibility(View.VISIBLE);

        fabTake.setTranslationY(TRANSLATIONY);
        fabUpload.setTranslationY(TRANSLATIONY);

        fabTake.setAlpha(0f);
        fabUpload.setAlpha(0f);
    }

    public void loadPosts() {
        String path = "usuarios/" + new SessionHandler(getContext()).getId() + "/feed";
       new RequestHandler().loadPosts(path, this);
    }

    public void assignPosts(Post[] posts) {
        if (posts.length == 0) {
            missingPost.setVisibility(View.VISIBLE);
        }
        showRecycler();
        CompletePostAdapter adapter = new CompletePostAdapter(posts, this);
        recyclerPosts.setAdapter(adapter);
    }

    public void showRecycler() {
        progressbar.setVisibility(View.INVISIBLE);
        recyclerPosts.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPhoto:
                toggleMenu();
                break;
            case R.id.takePhoto:
                sendToTakePhoto();
                break;
            case R.id.uploadPhoto:
                sendToLoadPhoto();
                break;
        }
    }

    private void toggleMenu() {
        Log.d("SOCIAL", "TOGGLE MENU");
        if (isMenuOpen) {
            animateButtons(0f, 0f);
        }
        else {
            animateButtons(TRANSLATIONY, 2f);
        }
        isMenuOpen = !isMenuOpen;
    }

    private void sendToTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (photoFile != null) {
            photoURI = FileProvider.getUriForFile(getContext(), "com.livex.martinsalerno.ar_project", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, PHOTO_INTENT_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == PHOTO_INTENT_CODE) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtra("imageUri", photoURI.toString());
                startActivity(intent);
            }
            else if (requestCode == GALLERY_INTENT_CODE) {
                Uri picUri = data.getData();
                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtra("imageUri", picUri.toString());
                startActivity(intent);
            }
        }
    }

    private void animateButtons(float translationY, float alpha) {
        fabAdd.animate().setInterpolator(interpolator).rotationBy(45f).setDuration(300).start();
        fabUpload.animate().translationY(translationY).alpha(alpha).setInterpolator(interpolator).setDuration(300).start();
        fabTake.animate().translationY(translationY).alpha(alpha).setInterpolator(interpolator).setDuration(300).start();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentImagePath = image.getAbsolutePath();
        return image;
    }

    private void sendToLoadPhoto(){
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_INTENT_CODE);
    }

    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getContext(),    contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

}
