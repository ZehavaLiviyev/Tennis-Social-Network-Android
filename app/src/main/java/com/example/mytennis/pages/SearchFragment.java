package com.example.mytennis.pages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    EditText search_view_et;
    ImageButton search_btn;
    RecyclerView list;

    TextView tV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);
        search_view_et = view.findViewById(R.id.search_textField_et);
        search_btn = view.findViewById(R.id.search__searchBtn);
        list = view.findViewById(R.id.search_rv);

        tV = view.findViewById(R.id.text_test);

       /* search_view_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("before",s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged",s.toString());

                tV.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("after",s.toString());
            }
        });*/

        return view;
    }



}