package com.example.mytennis.Profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytennis.R;
import com.example.mytennis.model.Model;
import com.example.mytennis.model.Post;
import com.example.mytennis.model.User;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    User user;
    View view;
    ImageView photoImv;
    TextView usernameTv;
    ProfileAdapter adapter;
    ProfileViewModel viewModel;
    Button logout_btn, edit_btn;
    SwipeRefreshLayout swipeRefresh;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = Model.instance.getActiveUser();
        photoImv = view.findViewById(R.id.frag_profile_imv);
        edit_btn = view.findViewById(R.id.frag_profile_edit_btn);
        usernameTv = view.findViewById(R.id.frag_profile_usern_tv);
        logout_btn = view.findViewById(R.id.frag_profile_logout_btn);
        swipeRefresh = view.findViewById(R.id.profilePostslist_swiperefresh);
        viewModel.getPostsData().observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshUserPostsList());
        swipeRefresh.setRefreshing(
                Model.instance.getPostsListLoadingState()
                        .getValue() == Model.PostsListLoadingState.loading
        );

        Model.instance.getPostsListLoadingState().observe(getViewLifecycleOwner(), postsListLoadingState -> {
            if (postsListLoadingState == Model.PostsListLoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }
        });
        setUserDetails();

        edit_btn.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(ProfileFragmentDirections
                                .actionProfileFragmentToEditProfileFragment()));

        logout_btn.setOnClickListener(v -> logout());


        // rv :

        RecyclerView list = view.findViewById(R.id.profile_rv);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProfileAdapter();
        list.setHasFixedSize(true);
        list.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String pId = String.valueOf(viewModel.getPostsData().getValue().get(position).getId());
            Navigation.findNavController(view)
                    .navigate(ProfileFragmentDirections.actionProfileFragmentToPostDetailsFragment(pId));
        });

        return view;
    }


    private void setUserDetails() {
        photoImv.setImageResource(R.drawable.avatar_logo);
        usernameTv.setText(user.getUserName());
        if (user.getProImageUrl() != null) {
            Picasso.get()
                    .load(Model.instance.getActiveUser().getProImageUrl())
                    .into(photoImv);
        }
    }

    private void logout() {
        Model.instance.logout(() -> {
            Navigation.findNavController(this.view)
                    .navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment());
        });
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    /* ************************************ interface *********************************************** */

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }



    /* ************************************ view holder ********************************************* */

    class ProfileViewHolder extends RecyclerView.ViewHolder {

        TextView desc_tv;
        ImageView post_imv;
        TextView postUser_tv;
        ImageView postUser_iv;

        public ProfileViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            post_imv = itemView.findViewById(R.id.feedPost_row_imv);
            desc_tv = itemView.findViewById(R.id.feedPost_row_des_tv);
            postUser_tv = itemView.findViewById(R.id.feedPost_row_upost_tv);
            postUser_iv = itemView.findViewById(R.id.feedPost_row_upostImage_iv);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v, pos);
            });
        }

        void bind(Post post, String userName , String userImage) {
            desc_tv.setText(post.getDescription());
            post_imv.setImageResource(R.drawable.postimage);
            postUser_iv.setImageResource(R.drawable.avatar_logo);
            postUser_tv.setText(userName);
            if (post.getImageUrl() != null) {
                Picasso.get()
                        .load(post.getImageUrl())
                        .into(post_imv);
            }
            if(userImage!=null){
                Picasso.get()
                        .load(userImage)
                        .into(postUser_iv);
            }
        }

    }

    /* ************************************ adapter ************************************************* */

    class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.feed_post_row, parent, false);
            ProfileViewHolder holder = new ProfileViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
            Post post = viewModel.getPostsData().getValue().get(position);
            User u =  Model.instance.getActiveUser();
            String postUserName =u.getUserName();
            String postUserImage = u.getProImageUrl();


            holder.bind(post, postUserName , postUserImage);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getPostsData().getValue() == null) {
                return 0;
            }
            return viewModel.getPostsData().getValue().size();
        }
    }


}