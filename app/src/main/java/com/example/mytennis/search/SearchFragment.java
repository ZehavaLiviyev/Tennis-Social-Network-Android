package com.example.mytennis.search;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytennis.R;
import com.example.mytennis.model.User;
import com.squareup.picasso.Picasso;

public class SearchFragment extends Fragment {

    View view;
   // EditText search_view_et;
  //  ImageButton search_btn;
    RecyclerView list;
    SearchViewModel viewModel;
    SearchUserAdapter adapter;
   // TextView tV;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);
        //search_view_et = view.findViewById(R.id.search_textField_et);
       // search_btn = view.findViewById(R.id.search__searchBtn);
        list = view.findViewById(R.id.search_rv);

      //  tV = view.findViewById(R.id.text_test);

        adapter = new SearchUserAdapter();
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setHasFixedSize(true);
        list.setAdapter(adapter);

        viewModel.getDataList().observe(getViewLifecycleOwner(), list1 -> refresh());


        adapter.setOnItemClickListener((v, position) ->
                Log.d("TAG", "row was clicked " + position));

/*

        search_view_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                for (User user : viewModel.getDataList().getValue()) {
                    if (user.getUserName().contains(s.toString())) {
                        viewModel.data.getValue().add(user);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("after search", s.toString());
            }
        });

*/

        return view;
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    class SearchUserViewHolder extends RecyclerView.ViewHolder {

        ImageView user_iv;
        TextView userName_tv;


        public SearchUserViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            user_iv = itemView.findViewById(R.id.searchUser_row_userImage_iv);
            userName_tv = itemView.findViewById(R.id.searchUser_row_userName_tv);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v, pos);
            });

        }

        void bind(User user) {
            userName_tv.setText(user.getUserName());

            if (user.getProImageUrl() != null) {
                Picasso.get()
                        .load(user.getProImageUrl())
                        .into(user_iv);
            }
        }

    }

    class SearchUserAdapter extends RecyclerView.Adapter<SearchFragment.SearchUserViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.search_user_row, parent, false);
            SearchUserViewHolder holder = new SearchUserViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchUserViewHolder holder, int position) {
            User user = viewModel.getDataList().getValue().get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getDataList().getValue() == null) {
                return 0;
            }
            return viewModel.getDataList().getValue().size();
        }
    }


}