package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.CommentDTO;
import com.example.socialmediaapp.dto.ResponseDTO;
import com.example.socialmediaapp.fragment.CommentFragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<CommentDTO> listitems;
    Context context;

    public CommentAdapter(ArrayList<CommentDTO> items, Context context) {
        this.listitems = items;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recyclerview_comment_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentDTO item = listitems.get(position);
        holder.userName.setText(item.getUSER().getFULLNAME());
        Glide.with(context).load(item.getUSER().getAVATAR()).into(holder.userAvatar);
        holder.comment.setText(item.getCONTENT());
        holder.repComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (CommentDTO b : item.getCOMMENTs()) {

                    View child = LayoutInflater.from(context).inflate(R.layout.recyclerview_repcomment_item, null);

                    CircleImageView civRepCommentUserAvatar = (CircleImageView) child.findViewById(R.id.civRepCommentUserAvatar);
                    Glide.with(context).load(b.getUSER().getAVATAR()).into(civRepCommentUserAvatar);

                    TextView tvRepCommentUserName = (TextView) child.findViewById(R.id.tvRepCommentUserName);
                    tvRepCommentUserName.setText(b.getUSER().getFULLNAME());
                    TextView tvRepCommentText = (TextView) child.findViewById(R.id.tvRepCommentText);
                    tvRepCommentText.setText(b.getCONTENT());
                    holder.repCmtLayout.addView(child);
                }
                View childBox = LayoutInflater.from(context).inflate(R.layout.layout_repcomment, null);
                EditText etRepContent=(EditText) childBox.findViewById(R.id.etRepContent);
                ImageView ivRepCommentSend=(ImageView) childBox.findViewById(R.id.ivRepCommentSend);
                etRepContent.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.toString().trim().isEmpty()) {
                            ivRepCommentSend.setVisibility(View.GONE);
                        } else {
                            ivRepCommentSend.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {}
                });
                // Send Message
                ivRepCommentSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etRepContent.getText().length() > 0) {
                            String content = etRepContent.getText().toString().trim();
                            RequestBody requestBodyContent = RequestBody.create(MediaType.parse("multipart/form-data"), content);
                            ApiService.apiService.postRepComment(item.getPOST_ID(),item.getID(),requestBodyContent, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                                @Override
                                public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                                    ResponseDTO message = response.body();
                                    Toast.makeText(context, "Đã trả lời bình luận", Toast.LENGTH_LONG).show();
                                    etRepContent.setText("");
                                    CommentFragment.loadComment();
                                }
                                @Override
                                public void onFailure(Call<ResponseDTO> call, Throwable t) {
                                    Toast.makeText(context, "Bình luận thất bại" + t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
                holder.repCmtLayout.addView(childBox);
                holder.repComment.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void sendRepComment() {

    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        CircleImageView userAvatar;
        TextView comment;
        TextView repComment;
        LinearLayout repCmtLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tvCommentUserName);
            userAvatar = itemView.findViewById(R.id.civCommentUserAvatar);
            comment = itemView.findViewById(R.id.tvCommentText);
            repComment = itemView.findViewById(R.id.tvCommentRep);
            repCmtLayout= itemView.findViewById(R.id.repCmtLayout);
        }
    }
}
