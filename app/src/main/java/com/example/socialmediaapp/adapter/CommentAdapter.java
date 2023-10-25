package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.dto.CommentDTO;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
                    holder.repComment.setVisibility(View.INVISIBLE);
                }
            }
        });
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
