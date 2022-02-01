package com.example.mytennis.feed;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytennis.R;
import com.example.mytennis.model.Model;
import com.example.mytennis.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FeedRvFragment extends Fragment {

    View view;
    String user_email;
    SwipeRefreshLayout swipeRefresh;
    FeedViewModel viewModel;
    MyAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feed, container, false);

        //Model.instance.getAllPosts(list -> data = list);
        swipeRefresh = view.findViewById(R.id.postslist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshStudentList());

        viewModel.getData().observe(getViewLifecycleOwner(), list1 -> refresh());

        swipeRefresh.setRefreshing(
                Model.instance.getPoststListLoadingState().getValue() == Model.PostsListLoadingState.loading
        );

        Model.instance.getPoststListLoadingState().observe(getViewLifecycleOwner(), postsListLoadingState -> {
            if (postsListLoadingState == Model.PostsListLoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }
        });

        RecyclerView list = view.findViewById(R.id.feed_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        list.setAdapter(adapter);
        user_email = FeedRvFragmentArgs.fromBundle(getArguments()).getUserEmail();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("TAG", "row was clicked " + position);
            }
        });

        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView desc_tv;
        TextView postuser_tv;
        ImageView post_imv;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            desc_tv = itemView.findViewById(R.id.feedPost_row_des_tv);
            post_imv = itemView.findViewById(R.id.feedPost_row_imv);
            postuser_tv = itemView.findViewById(R.id.feedPost_row_userpost_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v, pos);
                }
            });
        }
        void bind(Post post){
            desc_tv.setText(post.getDescription());
            post_imv.setImageResource(R.drawable.postimage);
            postuser_tv.setText(post.getPostUser());
            if(post.getImageUrl() != null){
                Picasso.get()
                        .load(post.getImageUrl())
                        .into(post_imv);

            }

        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.feed_post_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post post = viewModel.getData().getValue().get(position);
            holder.bind(post);
           // holder.desc_tv.setText(post.getDescription());
          //  holder.post_imv.setImageBitmap();
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData().getValue() == null) {
                return 0;
            }
            return  viewModel.getData().getValue().size();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.feed_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case R.id.menu_about:
                    Navigation.findNavController(this.view).navigate(R.id.action_global_aboutFragment);
                    break;
                case R.id.menu_profile:
                    Navigation.findNavController(this.view).navigate(R.id.action_global_profileFragment);
                    break;
                case R.id.menu_search:
                    break;
                case R.id.menu_tennisShop:
                    break;
                case R.id.menu_add:
                    Navigation.findNavController(this.view).navigate(R.id.action_global_addPostFragment);

                    break;
            }
        } else {
            return true;
        }
        return false;
    }

}