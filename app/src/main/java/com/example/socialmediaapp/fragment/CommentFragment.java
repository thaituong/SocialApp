package com.example.socialmediaapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.adapter.CommentAdapter;
import com.example.socialmediaapp.dto.CommentDTO;
import com.example.socialmediaapp.dto.ResponseDTO;

import java.util.ArrayList;

public class CommentFragment extends Fragment {
    public static final String TAG=CommentFragment.class.getName();
    private RecyclerView recyclerView;
    private ImageView closeComment;
    private CommentAdapter commentAdapter;
    public CommentFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        cnView(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        // Load Comment
        Bundle bundleReceive=getArguments();
        ResponseDTO itemPostDTO= (ResponseDTO) bundleReceive.get("repcomment");
        commentAdapter = new CommentAdapter((ArrayList<CommentDTO>) itemPostDTO.getResult().getComments(),getContext());
        recyclerView.setAdapter(commentAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        closeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void cnView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rvComment);
        closeComment = (ImageView) view.findViewById(R.id.ivCloseComment);
    }
}