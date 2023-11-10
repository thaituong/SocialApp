package com.example.socialmediaapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.adapter.ConversationAdapter;
import com.example.socialmediaapp.adapter.PostAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.NewFeedDTO;
import com.example.socialmediaapp.dto.ResponseDTO;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private MainActivity mMainActivity;
    private ResponseDTO litsp;
    private CircleImageView civUserAvatar;
    private TextView tvUserName;
    private TextView tvSLPost;
    private TextView tvSLFlower;
    private TextView tvSLFollowing;
    private Button btEditProfile;
    public PostAdapter postAdapter;
    private ListView list_view_post;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mMainActivity=(MainActivity)getActivity();
        civUserAvatar = (CircleImageView) view.findViewById(R.id.civUserAvatar);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvSLPost = (TextView) view.findViewById(R.id.tvSLPost);
        tvSLFlower = (TextView) view.findViewById(R.id.tvSLFlower);
        tvSLFollowing = (TextView) view.findViewById(R.id.tvSLFollowing);
        btEditProfile = (Button) view.findViewById(R.id.btEditProfile);
        list_view_post=(ListView) view.findViewById(R.id.list_view_post);
        ApiService.apiService.getUserInfo(MainActivity.userID, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp = response.body();
                Glide.with(getContext()).load(litsp.getResult().getUser().getAVATAR()).into(civUserAvatar);
                tvUserName.setText(litsp.getResult().getUser().getFULLNAME());
                tvSLPost.setText(litsp.getResult().getUser().getPOSTS());
                tvSLFlower.setText(litsp.getResult().getUser().getFOLLOWERS());
                tvSLFollowing.setText(litsp.getResult().getUser().getFOLLOWING());
            }
            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Call Api Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });

        ApiService.apiService.getUserPost(MainActivity.userID,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
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
        btEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.goToEditProfileFragment();
            }
        });

        return view;
    }
}