package com.example.mytennis.pages;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytennis.R;
import com.example.mytennis.model.Model;
import com.example.mytennis.model.Post;


public class AddPostFragment extends Fragment {

    Button postBtn;
    ImageButton camBtn, galleryBtn;
    ImageView imgPost;
    TextView descTv;
    View view;


    static final int REQUSET_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    //static final int REQUSET_IMAGE_CAPTURE = 2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_post, container, false);
        postBtn = view.findViewById(R.id.frag_addP_post_btn);
        camBtn = view.findViewById(R.id.frag_addP_cam_btn);
        galleryBtn = view.findViewById(R.id.frag_addP_gallery_btn);
        imgPost = view.findViewById(R.id.frag_addP_iv_p);
        descTv = view.findViewById(R.id.frag_addP_desc_tv);
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

    private void savePost() {
        postBtn.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);

        String desc = descTv.getText().toString();
        Model.instance.getPostId(id -> {
            Post post = new Post(desc, id,Model.instance.getActiveUser().getUserName());
            if (imageBitmap != null) {
                Model.instance.savePostImage(imageBitmap, post.getId() + ".jpg", url -> {
                    post.setImageUrl(url);
                    Model.instance.addPost(post, () ->
                            Navigation.findNavController(view).navigateUp());
                });
            } else {
                Model.instance.addPost(post, () -> {
                    Navigation.findNavController(view).navigateUp();
                });
            }

        });


    }

    private void openGallery() {
    }

    private void openCam() {
        Intent openCamintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCamintent, REQUSET_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extars = data.getExtras();
                imageBitmap = (Bitmap) extars.get("data");
                imgPost.setImageBitmap(imageBitmap);
            }
        }
    }
}