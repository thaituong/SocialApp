package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.dto.MessageDTO;

import java.util.List;

public class ConversationAdapter extends BaseAdapter {
    private List<MessageDTO> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    public ConversationAdapter(Context aContext,  List<MessageDTO> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
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
        ConversationAdapter.ViewHolder holder;
        MessageDTO tn = this.listData.get(i);
        if (tn.getIS_SEND_USER() == 1) {
            if (view == null) {
                view = layoutInflater.inflate(R.layout.listview_conversation_c_item, null);
                holder = new ConversationAdapter.ViewHolder();
                holder.tinnhan = (TextView) view.findViewById(R.id.tvTinNhanKhach);
                view.setTag(holder);
            } else {
                holder = (ConversationAdapter.ViewHolder) view.getTag();
            }
            holder.tinnhan.setText(tn.getCONTENT());
        } else {
            if (view == null) {
                view = layoutInflater.inflate(R.layout.listview_conversation_f_item, null);
                holder = new ConversationAdapter.ViewHolder();
                holder.tinnhan = (TextView) view.findViewById(R.id.tvTinNhanShop);
                view.setTag(holder);
            } else {
                holder = (ConversationAdapter.ViewHolder) view.getTag();

            }
            holder.tinnhan.setText(tn.getCONTENT());
        }

        return view;
    }
    static class ViewHolder {
        TextView tinnhan;
    }
}
