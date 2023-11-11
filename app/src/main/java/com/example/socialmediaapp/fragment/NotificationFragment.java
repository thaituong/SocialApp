package com.example.socialmediaapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.adapter.FollowAdapter;
import com.example.socialmediaapp.adapter.NotificationAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.NotificationDTO;
import com.example.socialmediaapp.dto.ResponseDTO;
import com.example.socialmediaapp.dto.UserDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {
    private ListView list_view_noti;
    private NotificationAdapter notificationAdapter;
    private ImageView ivBackNotification;
    private ResponseDTO litsp;
    private MainActivity mMainActivity;
    public static final String TAG = NotificationFragment.class.getName();

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        cnView(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        loadNotification();
        ivBackNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void loadNotification() {
        ApiService.apiService.getListNotification(MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp=response.body();
                notificationAdapter=new NotificationAdapter(getContext(), litsp.getResult().getNotifications(), new NotificationAdapter.IClickItemListener() {
                    @Override
                    public void onClickItemUser(NotificationDTO notificationDTO) {

                    }
                });
                list_view_noti.setAdapter(notificationAdapter);
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(),"Call Api Error"+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });
    }

    private void cnView(View view) {
        mMainActivity=(MainActivity)getActivity();
        list_view_noti=(ListView) view.findViewById(R.id.list_view_noti);
        ivBackNotification=(ImageView) view.findViewById(R.id.ivBackNotification);
    }
}