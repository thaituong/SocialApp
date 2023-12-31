package com.example.socialmediaapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.activity.CreateAccountActivity;
import com.example.socialmediaapp.activity.LoginActivity;
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
    private static MainActivity mMainActivity;
    private static ResponseDTO litsp;
    private static CircleImageView civUserAvatar;
    private static TextView tvUserName;
    private static TextView tvSLPost;
    private static TextView tvSLFlower;
    private static TextView tvSLFollowing;
    private Button btEditProfile;
    private Button btLogout;
    public static PostAdapter postAdapter;
    private static ListView list_view_post;
    private LinearLayout llFollower;
    private LinearLayout llFollowing;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
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
        btEditProfile = (Button) view.findViewById(R.id.btEditProfile);
        list_view_post=(ListView) view.findViewById(R.id.list_view_post);
        llFollower=(LinearLayout) view.findViewById(R.id.llFollower);
        llFollowing=(LinearLayout) view.findViewById(R.id.llFollowing);
        btLogout = (Button) view.findViewById(R.id.btLogout);
    }

    private void setEvent() {
        loadUserInfo(getContext());
        loadPost(getContext());
        btEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.goToEditProfileFragment();
            }
        });
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent1);
            }
        });
        //
        list_view_post.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = list_view_post.getItemAtPosition(i);
                NewFeedDTO newFeedDTO = (NewFeedDTO) o;
                showDeleteConfirmationDialog(newFeedDTO);
                return false;
            }
        });

        // View List Follower
        llFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiService.getFollowers(MainActivity.userID,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
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
                ApiService.apiService.getFollowed(MainActivity.userID,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
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
    private void showDeleteConfirmationDialog(NewFeedDTO newFeedDTO) {
        if (newFeedDTO.getUSER().getID().equalsIgnoreCase(MainActivity.userID)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Xác nhận xóa bài viết");
            builder.setMessage("Bạn có muốn xóa mục này không?")
                    .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ApiService.apiService.deletePost(newFeedDTO.getID(), MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                                @Override
                                public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                                    ResponseDTO message=response.body();
                                    Toast.makeText(getActivity(),"Xóa bài viết thành công",Toast.LENGTH_LONG).show();
                                    loadPost(getContext());
                                }

                                @Override
                                public void onFailure(Call<ResponseDTO> call, Throwable t) {
                                    Toast.makeText(getActivity(),"Xóa bài viết thất bại",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    public static void loadUserInfo(Context context) {
        ApiService.apiService.getUserInfo(MainActivity.userID, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp = response.body();
                Glide.with(context).load(litsp.getResult().getUser().getAVATAR()).into(civUserAvatar);
                tvUserName.setText(litsp.getResult().getUser().getFULLNAME());
                tvSLPost.setText(litsp.getResult().getUser().getPOSTS());
                tvSLFlower.setText(litsp.getResult().getUser().getFOLLOWERS());
                tvSLFollowing.setText(litsp.getResult().getUser().getFOLLOWING());
            }
            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(mMainActivity, "Call Api Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });
    }

    public static void loadPost(Context context) {
        ApiService.apiService.getUserPost(MainActivity.userID,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp=response.body();
                postAdapter=new PostAdapter(context, litsp.getResult().getNewFeeds(), new PostAdapter.IClickItemListener() {
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
                if(list_view_post != null){
                    list_view_post.setAdapter(postAdapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(mMainActivity,"Call Api Error"+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}