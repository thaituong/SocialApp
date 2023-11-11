package com.example.socialmediaapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.NotificationHelper;
import com.example.socialmediaapp.SocketManager;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.adapter.ItemAdapter;
import com.example.socialmediaapp.adapter.PostAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ItemStoryDTO;
import com.example.socialmediaapp.dto.NewFeedDTO;
import com.example.socialmediaapp.dto.ResponseDTO;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static final String TAG=HomeFragment.class.getName();
    private RecyclerView recyclerView;
    private ItemStoryDTO item;
    private ArrayList<ItemStoryDTO> listitem;
    private ArrayList<String> listimg;
    private ItemAdapter mitemAdapter;
    private MainActivity mMainActivity;
    public PostAdapter postAdapter;
    private ImageView idClickTest;
    private ResponseDTO litsp;
    public static int[] imgpos;
    private ListView list_view_post;
    private ImageView ivNotification;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cnView(view);
        setEvent();
        return view;
    }
    private void cnView(View view) {
        mMainActivity=(MainActivity)getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        idClickTest = (ImageView) view.findViewById(R.id.idClickTest);
        list_view_post=(ListView) view.findViewById(R.id.list_view_post);
        ivNotification = (ImageView) view.findViewById(R.id.ivNotification);
    }
    private void setEvent() {
        createVarLoadImg();
        loadStory();
        loadPost();
        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.goToNotificationFragment();
            }
        });
    }
    private void createVarLoadImg() {
        imgpos=new int[100];
        for (int i = 0; i < imgpos.length; i++) {
            imgpos[i] = 0;
        }
    }

    private void loadStory() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        listitem = new ArrayList<>();
        data();
        mitemAdapter = new ItemAdapter(listitem,getContext());
        recyclerView.setAdapter(mitemAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void loadPost() {
        ApiService.apiService.getListPost(MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp=response.body();
                postAdapter=new PostAdapter(getContext(), litsp.getResult().getNewFeeds(), new PostAdapter.IClickItemListener() {
                    @Override
                    public void onClickItemUser(NewFeedDTO itemPostDTO) {
                        mMainActivity.goToCommentFragment(itemPostDTO.getID());
                    }
                }, new PostAdapter.iClickItemListenerSetting() {
                    @Override
                    public void onClickItemSetting(NewFeedDTO itemPostDTO) {
                        mMainActivity.goToEditPostFragment(itemPostDTO);
                    }
                }, new PostAdapter.iClickItemListenerProfile() {
                    @Override
                    public void onClickItemProfile(NewFeedDTO itemPostDTO) {
                        mMainActivity.goToFProfileFragment(itemPostDTO.getUSER().getID());
                    }
                });
                list_view_post.setAdapter(postAdapter);
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(),"Call Api Error"+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });
    }



    private void data(){
        listitem.add(new ItemStoryDTO("Hồng ngọc",R.drawable.avtbabe));
        listitem.add(new ItemStoryDTO("Hồng ngọc",R.drawable.avtbabe));
        listitem.add(new ItemStoryDTO("Hồng ngọc",R.drawable.avtbabe));
        listitem.add(new ItemStoryDTO("Hồng ngọc",R.drawable.avtbabe));
        listitem.add(new ItemStoryDTO("Hồng ngọc",R.drawable.avtbabe));
        listitem.add(new ItemStoryDTO("Hồng ngọc",R.drawable.avtbabe));
        listitem.add(new ItemStoryDTO("Hồng ngọc",R.drawable.avtbabe));
        listitem.add(new ItemStoryDTO("Hồng ngọc",R.drawable.avtbabe));
    }



}