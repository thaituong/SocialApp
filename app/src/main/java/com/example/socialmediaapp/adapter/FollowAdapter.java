package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.dto.ConversationDTO;
import com.example.socialmediaapp.dto.UserDTO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<UserDTO> listData;
    private FollowAdapter.IClickItemListener iClickItemListener;
    public interface IClickItemListener{
        void onClickItemUser(UserDTO userDTO);
    }
    private Context context;
    public FollowAdapter(Context aContext, List<UserDTO> listData) {
        this.context = aContext;
        this.listData=listData;
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
        FollowAdapter.ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_follow_item, null);
            holder = new FollowAdapter.ViewHolder();
            holder.tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            holder.tvFullName = (TextView) view.findViewById(R.id.tvFullName);
            holder.civUserAvatar = (CircleImageView) view.findViewById(R.id.civUserAvatar);
            holder.btFollow=(Button) view.findViewById(R.id.btFollow);

            view.setTag(holder);
        } else {
            holder = (FollowAdapter.ViewHolder) view.getTag();
        }
        final UserDTO userDTO = this.listData.get(i);

        holder.tvUserName.setText(userDTO.getUSERNAME());
        holder.tvFullName.setText(userDTO.getFULLNAME());
        if(userDTO.getISFOLLOWED().trim().equalsIgnoreCase("1")){
            holder.btFollow.setBackgroundResource(R.drawable.buttonbr);
            holder.btFollow.setText("Following");
        }else{
            holder.btFollow.setBackgroundResource(R.drawable.btfl);
            holder.btFollow.setText("Follow");
        }
        Glide.with(context).load(userDTO.getAVATAR()).into(holder.civUserAvatar);
        return view;
    }

    static class ViewHolder {
        TextView tvUserName;
        TextView tvFullName;
        CircleImageView civUserAvatar;
        Button btFollow;

    }
}
