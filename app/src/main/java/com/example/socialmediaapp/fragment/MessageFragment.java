package com.example.socialmediaapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.adapter.MessageAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ConversationDTO;
import com.example.socialmediaapp.dto.ResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFragment extends Fragment {
    private static MainActivity mMainActivity;
    private static ListView list_view_message;
    private static ResponseDTO litsp;
    private static MessageAdapter messageAdapter;

    public MessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        cnView(view);
        setEvent();
        return view;
    }
    private void cnView(View view) {
        mMainActivity=(MainActivity)getActivity();
        list_view_message=(ListView) view.findViewById(R.id.list_view_message);
    }

    private void setEvent() {
        loadMessage(getContext());
    }

    public static void loadMessage(Context context) {
        ApiService.apiService.getListMessage(MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp=response.body();
                messageAdapter=new MessageAdapter(context, litsp.getResult().getConversations(), new MessageAdapter.IClickItemListener() {
                    @Override
                    public void onClickItemUser(ConversationDTO conversationDTO) {
                        mMainActivity.goToConversationFragment(conversationDTO);
                    }
                });
                list_view_message.setAdapter(messageAdapter);
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(mMainActivity,"Call Api Error"+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });
    }
}