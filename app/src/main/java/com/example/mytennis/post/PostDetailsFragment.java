package com.example.mytennis.post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mytennis.R;
import com.example.mytennis.model.Model;
import com.example.mytennis.model.Post;
import com.squareup.picasso.Picasso;

public class PostDetailsFragment extends Fragment {

    View view;
    Post post_;
    TextView postDesc_et;
    ImageView postImage_iv;
    ProgressBar progressBar;
    Button edit_btn, delete_btn;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_post_details, container, false);

        postImage_iv = view.findViewById(R.id.details_img);
        edit_btn = view.findViewById(R.id.details_edit_btn);
        postDesc_et = view.findViewById(R.id.details_desc_tv);
        delete_btn = view.findViewById(R.id.details_delete_btn);
        progressBar = view.findViewById(R.id.frag_details_progressBar);

        progressBar.setVisibility(View.GONE);
        String pId = PostDetailsFragmentArgs.fromBundle(getArguments()).getPostId();

        Model.instance.getPostById(pId, post -> {
            post_ = post;
            postDesc_et.setText(post.getDescription());

            if (post.getImageUrl() != null)
                Picasso.get()
                        .load(post.getImageUrl())
                        .into(postImage_iv);
        });

        edit_btn.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(PostDetailsFragmentDirections
                                .actionPostDetailsFragmentToEditPostFragment(pId)));

        delete_btn.setOnClickListener(v -> deletePost());

        return view;
    }

    private void deletePost() {
        Model.instance.refreshPostsList();
        // TODO:: NAVIGATE TO SOME PAGE
        Model.instance.deletePost(post_, () -> {
            edit_btn.setEnabled(false);
            delete_btn.setEnabled(false);
            postDesc_et.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            Log.d("TAG", "delete successful");
            Model.instance.refreshPostsList();
            Navigation.findNavController(view).navigateUp();
        });
    }
}