package com.example.mytennis.post;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mytennis.R;
import com.example.mytennis.model.Model;
import com.example.mytennis.model.Post;

import java.io.FileNotFoundException;


public class AddPostFragment extends Fragment {


    ImageButton camBtn, galleryBtn;
    ProgressBar progressBar;
    ImageView imgPost;
    TextView descTv;
    Button postBtn;
    View view;


    static final int REQUESTS_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    Bitmap imageBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_post, container, false);
        progressBar = view.findViewById(R.id.frag_addP_progressBar);
        galleryBtn = view.findViewById(R.id.frag_addP_gallery_btn);
        postBtn = view.findViewById(R.id.frag_addP_post_btn);
        camBtn = view.findViewById(R.id.frag_addP_cam_btn);
        descTv = view.findViewById(R.id.frag_addP_desc_tv);
        imgPost = view.findViewById(R.id.frag_addP_iv_p);

        progressBar.setVisibility(View.GONE);

        camBtn.setOnClickListener(v -> {
            openCam();
        });
        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });
        postBtn.setOnClickListener(v -> {
            savePost();
        });


        return view;
    }

    private void openCam() {
        Intent openCamIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCamIntent, REQUESTS_IMAGE_CAPTURE);
    }

    private void openGallery() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, REQUEST_IMAGE_GALLERY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTS_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imgPost.setImageBitmap(imageBitmap);
            }
        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                try {
                    imageBitmap = BitmapFactory.decodeStream(getContext()
                            .getContentResolver().openInputStream(uri));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imgPost.setImageBitmap(imageBitmap);
            }
        }
    }

    private void savePost() {

        camBtn.setEnabled(false);
        postBtn.setEnabled(false);
        galleryBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        String desc = descTv.getText().toString();

        Model.instance.getPostId(id -> {
            Post post = new Post(desc, id, Model.instance.getActiveUser().getEmail());
            if (imageBitmap != null) {
                Model.instance.savePostImage(imageBitmap, post.getId() + ".jpg", url -> {
                    post.setImageUrl(url);
                    Model.instance.addPost(post, () -> {
                        Model.instance.refreshPostsList();
                        Navigation.findNavController(view).navigateUp();
                    });
                });
            } else {
                Model.instance.addPost(post, () -> {
                    Model.instance.refreshPostsList();
                    Navigation.findNavController(view).navigateUp();
                });
            }
        });


    }
}