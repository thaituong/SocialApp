package com.example.socialmediaapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.adapter.FollowAdapter;
import com.example.socialmediaapp.adapter.MessageAdapter;
import com.example.socialmediaapp.dto.ConversationDTO;
import com.example.socialmediaapp.dto.ResultDTO;
import com.example.socialmediaapp.dto.UserDTO;

public class ListFollowFragment extends Fragment {
    ResultDTO resultDTO;
    private ListView list_view_follow;
    private FollowAdapter followAdapter;
    private ImageView ivBackListFollow;
    private MainActivity mMainActivity;
    public static final String TAG = ListFollowFragment.class.getName();

    public ListFollowFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_follow, container, false);
        cnView(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        Bundle bundleReceive = getArguments();
        resultDTO = (ResultDTO) bundleReceive.get("listfollower");
        followAdapter=new FollowAdapter(getContext(), resultDTO.getUsers(), new FollowAdapter.IClickItemListener() {
            @Override
            public void onClickItemUser(UserDTO userDTO) {
                mMainActivity.goToFProfileFragment(userDTO.getID());
            }
        });
        list_view_follow.setAdapter(followAdapter);
        ivBackListFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void cnView(View view) {
        mMainActivity=(MainActivity)getActivity();
        list_view_follow=(ListView) view.findViewById(R.id.list_view_follow);
        ivBackListFollow=(ImageView) view.findViewById(R.id.ivBackListFollow);
    }
}