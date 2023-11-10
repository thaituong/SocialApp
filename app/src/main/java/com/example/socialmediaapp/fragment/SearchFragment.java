package com.example.socialmediaapp.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.adapter.FollowAdapter;
import com.example.socialmediaapp.adapter.MessageAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ConversationDTO;
import com.example.socialmediaapp.dto.ResponseDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private EditText etSearch;
    private ListView list_view_follow;
    private FollowAdapter followAdapter;
    private ResponseDTO litsp;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        cnView(view);
        setEvent();
        return view;
    }
    private void cnView(View view) {
        etSearch=(EditText) view.findViewById(R.id.etSearch);
        list_view_follow=(ListView) view.findViewById(R.id.list_view_follow);
    }
    private void setEvent() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String changedText = charSequence.toString();
                if(!changedText.equalsIgnoreCase("")){
                    ApiService.apiService.getListSearch(changedText,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                        @Override
                        public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                            litsp=response.body();
                            followAdapter=new FollowAdapter(getContext(),litsp.getResult().getUsers());
                            list_view_follow.setAdapter(followAdapter);
                        }

                        @Override
                        public void onFailure(Call<ResponseDTO> call, Throwable t) {
                            Toast.makeText(getActivity(),"Call Api Error"+t.getMessage(),Toast.LENGTH_LONG).show();
                            Log.d("API Response", "Giá trị litsp: " + t.getMessage());
                        }
                    });
                } else {
                    followAdapter=new FollowAdapter(getContext(),new ArrayList<>());
                    list_view_follow.setAdapter(followAdapter);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}