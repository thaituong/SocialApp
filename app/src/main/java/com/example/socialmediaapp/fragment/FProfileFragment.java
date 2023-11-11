package com.example.socialmediaapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.adapter.PostAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ConversationDTO;
import com.example.socialmediaapp.dto.ConversationDetailDTO;
import com.example.socialmediaapp.dto.NewFeedDTO;
import com.example.socialmediaapp.dto.ResponseDTO;
import com.example.socialmediaapp.dto.UserConversationDTO;
import com.example.socialmediaapp.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FProfileFragment extends Fragment {
    public static final String TAG = FProfileFragment.class.getName();
    private MainActivity mMainActivity;
    private ResponseDTO litsp;
    String idProfile;
    private CircleImageView civUserAvatar;
    private TextView tvUserName;
    private TextView tvSLPost;
    private TextView tvSLFlower;
    private TextView tvSLFollowing;
    private TextView tvDanhDauLinkAVT;
    public PostAdapter postAdapter;
    private ListView list_view_post;
    private ImageView ivBackProfile;
    private LinearLayout llFollower;
    private LinearLayout llFollowing;
    private Button btFollow;
    private Button btMessage;
    public FProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_f_profile, container, false);
        cnView(view);
        setEvent();
        return view;
    }
    private void cnView(View view) {
        mMainActivity=(MainActivity)getActivity();
        civUserAvatar = (CircleImageView) view.findViewById(R.id.civUserAvatar);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvSLPost = (TextView) view.findViewById(R.id.tvSLPost);
        tvSLFlower = (TextView) view.findViewById(R.id.tvSLFlower);
        tvSLFollowing = (TextView) view.findViewById(R.id.tvSLFollowing);
        list_view_post=(ListView) view.findViewById(R.id.list_view_post);
        ivBackProfile = (ImageView) view.findViewById(R.id.ivBackProfile);
        llFollower = (LinearLayout) view.findViewById(R.id.llFollower);
        llFollowing = (LinearLayout) view.findViewById(R.id.llFollowing);
        btFollow = (Button) view.findViewById(R.id.btFollow);
        btMessage = (Button) view.findViewById(R.id.btMessage);
        tvDanhDauLinkAVT = (TextView) view.findViewById(R.id.tvDanhDauLinkAVT);
    }
    private void setEvent() {
        Bundle bundleReceive = getArguments();
        idProfile = (String) bundleReceive.get("idprofile");
        if(idProfile.equalsIgnoreCase(MainActivity.userID)){
            btFollow.setVisibility(View.GONE);
        }
        loadUserInfo();
        loadPost();
        // Click Follow
        btFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btFollow.getText().equals("Follow")){
                    ApiService.apiService.follow(idProfile,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                        @Override
                        public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                            ResponseDTO message = response.body();
                        }

                        @Override
                        public void onFailure(Call<ResponseDTO> call, Throwable t) {
                            Toast.makeText(getContext(), "Follow thất bại" + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    btFollow.setBackgroundResource(R.drawable.buttonbr);
                    btFollow.setText("Following");
                }else{
                    ApiService.apiService.unFollow(idProfile,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                        @Override
                        public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                            ResponseDTO message = response.body();
                        }

                        @Override
                        public void onFailure(Call<ResponseDTO> call, Throwable t) {
                            Toast.makeText(getContext(), "UnFollow thất bại" + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    btFollow.setBackgroundResource(R.drawable.btfl);
                    btFollow.setText("Follow");
                }
            }
        });

        btMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiService.getConversation(idProfile,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                    @Override
                    public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                        litsp=response.body();
                        ConversationDTO conversationDTO=new ConversationDTO();
                        ConversationDetailDTO conversationDetailDTO=new ConversationDetailDTO();
                        conversationDetailDTO.setID(litsp.getResult().getConversation_id());
                        UserConversationDTO userConversationDTO=new UserConversationDTO();
                        userConversationDTO.setUSER(new UserDTO(tvUserName.getText().toString(),tvDanhDauLinkAVT.getText().toString()));
                        List<UserConversationDTO> list=new ArrayList<>();
                        list.add(userConversationDTO);
                        conversationDetailDTO.setUSER_CONVERSATIONs(list);
                        conversationDTO.setCONVERSATION(conversationDetailDTO);
                        mMainActivity.goToConversationFragment(conversationDTO);
                    }

                    @Override
                    public void onFailure(Call<ResponseDTO> call, Throwable t) {
                        Toast.makeText(getContext(), "Follow thất bại" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Back
        ivBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        // View List Follower
        llFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiService.getFollowers(idProfile,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                    @Override
                    public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                        litsp=response.body();
                        mMainActivity.goToListFollowFragment(litsp.getResult());
                    }

                    @Override
                    public void onFailure(Call<ResponseDTO> call, Throwable t) {
                        Toast.makeText(getActivity(),"Call Api Error"+t.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d("API Response", "Giá trị litsp: " + t.getMessage());
                    }
                });
            }
        });
        // View List Following
        llFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiService.getFollowed(idProfile,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                    @Override
                    public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                        litsp=response.body();
                        mMainActivity.goToListFollowFragment(litsp.getResult());
                    }

                    @Override
                    public void onFailure(Call<ResponseDTO> call, Throwable t) {
                        Toast.makeText(getActivity(),"Call Api Error"+t.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d("API Response", "Giá trị litsp: " + t.getMessage());
                    }
                });
            }
        });
    }

    private void loadPost() {
        ApiService.apiService.getUserPost(idProfile,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
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

    private void loadUserInfo() {
        ApiService.apiService.getUserInfo(idProfile, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp = response.body();
                Glide.with(getContext()).load(litsp.getResult().getUser().getAVATAR()).into(civUserAvatar);
                tvUserName.setText(litsp.getResult().getUser().getFULLNAME());
                tvSLPost.setText(litsp.getResult().getUser().getPOSTS());
                tvSLFlower.setText(litsp.getResult().getUser().getFOLLOWERS());
                tvSLFollowing.setText(litsp.getResult().getUser().getFOLLOWING());
                tvDanhDauLinkAVT.setText(litsp.getResult().getUser().getAVATAR());
                if(litsp.getResult().getUser().getISFOLLOWED().trim().equalsIgnoreCase("1")){
                    btFollow.setBackgroundResource(R.drawable.buttonbr);
                    btFollow.setText("Following");
                }else{
                    btFollow.setBackgroundResource(R.drawable.btfl);
                    btFollow.setText("Follow");
                }
            }
            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Call Api Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });
    }
}