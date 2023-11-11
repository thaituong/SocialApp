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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.adapter.CommentAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.CommentDTO;
import com.example.socialmediaapp.dto.ResponseDTO;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment {
    public static final String TAG=CommentFragment.class.getName();
    private static RecyclerView recyclerView;
    private ImageView closeComment;
    private EditText etContent;
    private ImageView ivCommentSend;
    static String id;
    static CommentAdapter commentAdapter;
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
        id= (String) bundleReceive.get("repcomment");
        loadComment();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        closeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        // Check Send Message Button
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    ivCommentSend.setVisibility(View.GONE);
                } else {
                    ivCommentSend.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        // Send Message
        ivCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etContent.getText().length() > 0) {
                    sendComment();
                }
            }
        });
    }

    public static void loadComment() {
        ApiService.apiService.getListComment(id,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                ResponseDTO responseDTO=response.body();
                commentAdapter = new CommentAdapter((ArrayList<CommentDTO>) responseDTO.getResult().getComments(),recyclerView.getContext());
                recyclerView.setAdapter(commentAdapter);
            }
            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(recyclerView.getContext(), "Call Api Error"+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });
    }

    private void sendComment() {
        String content = etContent.getText().toString().trim();
        RequestBody requestBodyContent = RequestBody.create(MediaType.parse("multipart/form-data"), content);
        ApiService.apiService.postComment(id,requestBodyContent,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                ResponseDTO message = response.body();
                Toast.makeText(getActivity(), "Đã bình luận", Toast.LENGTH_LONG).show();
                etContent.setText("");
                loadComment();
            }
            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Bình luận thất bại" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cnView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rvComment);
        closeComment = (ImageView) view.findViewById(R.id.ivCloseComment);
        etContent = (EditText) view.findViewById(R.id.etContent);
        ivCommentSend = (ImageView) view.findViewById(R.id.ivCommentSend);
    }
}