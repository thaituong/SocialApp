package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.dto.MessageDTO;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<MessageDTO> listData;
    private Context context;

    public ConversationAdapter(Context context, List<MessageDTO> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getItemViewType(int position) {
        if (listData.get(position).getIS_SEND_USER() == 1) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_conversation_c_item, parent, false);
            return new UserViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_conversation_f_item, parent, false);
            return new OtherViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageDTO message = listData.get(position);
        if(message.getTYPE().equalsIgnoreCase("text")){
            if (holder instanceof UserViewHolder) {
                UserViewHolder userViewHolder = (UserViewHolder) holder;
                userViewHolder.img.setVisibility(View.GONE);
                userViewHolder.tinnhan.setVisibility(View.VISIBLE);
                userViewHolder.tinnhan.setText(message.getCONTENT());
            } else if (holder instanceof OtherViewHolder) {
                OtherViewHolder otherViewHolder = (OtherViewHolder) holder;
                otherViewHolder.img.setVisibility(View.GONE);
                otherViewHolder.tinnhan.setVisibility(View.VISIBLE);
                otherViewHolder.tinnhan.setText(message.getCONTENT());
            }
        }else{
            if (holder instanceof UserViewHolder) {
                UserViewHolder userViewHolder = (UserViewHolder) holder;
                userViewHolder.tinnhan.setVisibility(View.GONE);
                userViewHolder.img.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = userViewHolder.img.getLayoutParams();
                params.width = 600;
                params.height = 600;
                userViewHolder.img.setLayoutParams(params);
                Glide.with(context).load(message.getCONTENT()).into(userViewHolder.img);

            } else if (holder instanceof OtherViewHolder) {
                OtherViewHolder otherViewHolder = (OtherViewHolder) holder;
                otherViewHolder.tinnhan.setVisibility(View.GONE);
                otherViewHolder.img.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = otherViewHolder.img.getLayoutParams();
                params.width = 600;
                params.height = 600;
                otherViewHolder.img.setLayoutParams(params);
                Glide.with(context).load(message.getCONTENT()).into(otherViewHolder.img);
            }
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tinnhan;
        ImageView img;

        UserViewHolder(View itemView) {
            super(itemView);
            tinnhan = itemView.findViewById(R.id.tvTinNhanKhach);
            img = itemView.findViewById(R.id.ivTinNhanKhach);
        }
    }

    private static class OtherViewHolder extends RecyclerView.ViewHolder {
        TextView tinnhan;
        ImageView img;

        OtherViewHolder(View itemView) {
            super(itemView);
            tinnhan = itemView.findViewById(R.id.tvTinNhanShop);
            img = itemView.findViewById(R.id.ivTinNhanShop);
        }
    }
}
