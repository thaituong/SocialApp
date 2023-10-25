package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.dto.ConversationDTO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<ConversationDTO> listData;
    private MessageAdapter.IClickItemListener iClickItemListener;
    public interface IClickItemListener{
        void onClickItemUser(ConversationDTO conversationDTO);
    }
    private Context context;
    public MessageAdapter(Context aContext, List<ConversationDTO> listData, MessageAdapter.IClickItemListener listener) {
        this.context = aContext;
        this.listData=listData;
        layoutInflater = LayoutInflater.from(aContext);
        this.iClickItemListener=listener;
    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MessageAdapter.ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_message_item, null);
            holder = new MessageAdapter.ViewHolder();
            holder.tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            holder.civUserAvatar = (CircleImageView) view.findViewById(R.id.civUserAvatar);
            holder.rlItemMessage=(RelativeLayout) view.findViewById(R.id.rlItemMessage);

            view.setTag(holder);
        } else {
            holder = (MessageAdapter.ViewHolder) view.getTag();
        }
        final ConversationDTO itemConversationDTO = this.listData.get(i);

        holder.tvUserName.setText(itemConversationDTO.getCONVERSATION().getUSER_CONVERSATIONs().get(0).getUSER().getFULLNAME());
        Glide.with(context).load(itemConversationDTO.getCONVERSATION().getUSER_CONVERSATIONs().get(0).getUSER().getAVATAR()).into(holder.civUserAvatar);
        holder.rlItemMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.onClickItemUser(itemConversationDTO);
            }
        });
        return view;
    }

    static class ViewHolder {
        TextView tvUserName;
        CircleImageView civUserAvatar;
        RelativeLayout rlItemMessage;

    }
}
