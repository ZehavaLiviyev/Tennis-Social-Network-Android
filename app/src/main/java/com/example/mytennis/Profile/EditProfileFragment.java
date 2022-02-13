package com.example.mytennis.Profile;

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
import com.example.mytennis.model.User;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;


public class EditProfileFragment extends Fragment {


    ImageButton camBtn, galleryBtn,delBtn;
    ProgressBar progressBar;
    ImageView imgProfile;
    TextView username;
    Button saveBtn;
    View view;

    static final int REQUESTS_IMAGE_CAPTURE = 1;
    static final int REQUESTS_IMAGE_GALLERY = 2;
    Bitmap imageBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        delBtn = view.findViewById(R.id.frag_editProfile_del_btn);
        camBtn = view.findViewById(R.id.frag_editProfile_cam_btn);
        saveBtn = view.findViewById(R.id.frag_editProfile_save_btn);
        username = view.findViewById(R.id.frag_editProfile_username_et);
        imgProfile = view.findViewById(R.id.frag_editProfile_profil_imv);
        galleryBtn = view.findViewById(R.id.frag_editProfile_gallery_btn);
        progressBar = view.findViewById(R.id.frag_editProfile_progressBar);

        progressBar.setVisibility(View.GONE);


        setUserDetails();

        delBtn.setOnClickListener(v -> deleteImage());
        saveBtn.setOnClickListener(v -> save());
        camBtn.setOnClickListener(v -> openCam());
        galleryBtn.setOnClickListener(v -> openGallery());

        return view;
    }



    /* ************************************ init **************************************************** */

    private void setUserDetails() {
        username.setText(Model.instance.getActiveUser().getUserName());
        if (Model.instance.getActiveUser().getProImageUrl() != null) {
            Picasso.get()
                    .load(Model.instance.getActiveUser().getProImageUrl())
                    .into(imgProfile);
        }
    }

    /* ************************************ save **************************************************** */

    private void save() {

        Model.instance.checkUserName(username.getText().toString(), flag -> {
            // it`s ok , userName is available
            if (flag == true) {


                camBtn.setEnabled(false);
                saveBtn.setEnabled(false);
                galleryBtn.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                User user = Model.instance.getActiveUser();

                if (imageBitmap != null) {
                    Model.instance.saveUserImage(imageBitmap, user.getEmail() + ".jpg",
                            url -> {
                                user.setProImageUrl(url);
                                Model.instance.saveUpdateUser(username.getText().toString(),
                                        () -> Navigation.findNavController(view).navigateUp());
                            });
                } else {
                    Model.instance.saveUpdateUser(username.getText().toString(),
                            () -> Navigation.findNavController(view).navigateUp());
                }

            } else {
                username.setError("Username is not available");
                username.requestFocus();
            }
        });

    }


    /* ************************************ profile image ******************************************* */

    private void openCam() {
        Intent openCamIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCamIntent, REQUESTS_IMAGE_CAPTURE);
    }

    private void openGallery() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, REQUESTS_IMAGE_GALLERY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTS_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imgProfile.setImageBitmap(imageBitmap);
            }
        } else if (requestCode == REQUESTS_IMAGE_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                try {
                    imageBitmap = BitmapFactory.decodeStream(getContext()
                            .getContentResolver().openInputStream(uri));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imgProfile.setImageBitmap(imageBitmap);
            }
        }
    }


    private void deleteImage() {
        Model.instance.deleteUserImage(Model.instance.getActiveUser().getEmail(), new Model.DeleteUserImageListener() {
            @Override
            public void onComplete(Boolean flag) {
                if (flag==true){
                    imgProfile.setImageResource(R.drawable.avatar_logo);
                    Navigation.findNavController(view).navigateUp();
                }
            }
        });
    }

}