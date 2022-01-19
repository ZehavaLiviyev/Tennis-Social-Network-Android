package com.example.mytennis.feed;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytennis.R;


public class FeedFragment extends Fragment {

    FeedViewModel viewModel;
    MyAdapter adapter;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=  inflater.inflate(R.layout.fragment_feed, container, false);

        RecyclerView list = view.findViewById(R.id.feed_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
             //   String stId = viewModel.getData().getValue().get(position).getId();
                //   Navigation.findNavController(v).navigate(StudentListRvFragmentDirections.actionStudentListRvFragmentToStudentDetailsFragment(stId));
            }
        });

        return view;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView postTv;


        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            postTv = itemView.findViewById(R.id.feedPost_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v,pos);
                }
            });
        }
    }
    interface OnItemClickListener{
        void onItemClick(View v,int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        OnItemClickListener listener;
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.feed_post,parent,false);
            MyViewHolder holder = new MyViewHolder(view,listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//            Student student = viewModel.getData().getValue().get(position);
//            holder.postTv.setText(student.getName());

        }

        @Override
        public int getItemCount() {
//            if(viewModel.getData().getValue() == null){
//                return 0;
//            }
//            return viewModel.getData().getValue().size();
           return 0;
        }

    }





}