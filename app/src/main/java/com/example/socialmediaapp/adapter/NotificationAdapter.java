package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.NotificationDTO;
import com.example.socialmediaapp.dto.ResponseDTO;
import com.example.socialmediaapp.dto.UserDTO;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<NotificationDTO> listData;
    private NotificationAdapter.IClickItemListener iClickItemListener;
    public interface IClickItemListener{
        void onClickItemUser(NotificationDTO notificationDTO);
    }
    private Context context;
    public NotificationAdapter(Context aContext, List<NotificationDTO> listData, NotificationAdapter.IClickItemListener listener) {
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
        NotificationAdapter.ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_notification_item, null);
            holder = new NotificationAdapter.ViewHolder();
            holder.tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            holder.civUserAvatar = (CircleImageView) view.findViewById(R.id.civUserAvatar);
            holder.tvContent = (TextView) view.findViewById(R.id.tvContent);
            holder.tvNotiTime = (TextView) view.findViewById(R.id.tvNotiTime);
            holder.rlItemNotification = (RelativeLayout) view.findViewById(R.id.rlItemNotification);
            view.setTag(holder);
        } else {
            holder = (NotificationAdapter.ViewHolder) view.getTag();
        }
        final NotificationDTO notificationDTO = this.listData.get(i);

        holder.tvUserName.setText(notificationDTO.getUSER().getUSERNAME());
        Instant instant = null;
        ZonedDateTime zonedDateTime = null;
        DateTimeFormatter formatter = null;
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            instant = Instant.parse(notificationDTO.getCreatedAt());
            zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
            formatter = DateTimeFormatter.ofPattern("HH:mm");
            formattedDate = zonedDateTime.format(formatter);
        }
        if (notificationDTO.getTYPE().equalsIgnoreCase("like")){
            holder.tvContent.setText(notificationDTO.getUSER().getUSERNAME()+" đã thích bài viết của bạn");
            holder.rlItemNotification.setBackgroundColor(0x80FFCDD2);
        } else if (notificationDTO.getTYPE().equalsIgnoreCase("comment")){
            holder.tvContent.setText(notificationDTO.getUSER().getUSERNAME()+" đã bình luận về bài viết của bạn");
            holder.rlItemNotification.setBackgroundColor(0x80BBDEFB);
        } else if (notificationDTO.getTYPE().equalsIgnoreCase("r_comment")){
            holder.tvContent.setText(notificationDTO.getUSER().getUSERNAME()+" đã trả lời bình luận của bạn");
            holder.rlItemNotification.setBackgroundColor(0x80B0E0F0);
        } else {
            holder.tvContent.setText(notificationDTO.getUSER().getUSERNAME()+" đã follow bạn");
            holder.rlItemNotification.setBackgroundColor(0x80C8E6C9);
        }
        holder.tvNotiTime.setText(formattedDate);
        Glide.with(context).load(notificationDTO.getUSER().getAVATAR()).into(holder.civUserAvatar);
        holder.rlItemNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.onClickItemUser(notificationDTO);
            }
        });
        return view;
    }

    static class ViewHolder {
        TextView tvUserName;
        TextView tvContent;
        CircleImageView civUserAvatar;
        TextView tvNotiTime;
        RelativeLayout rlItemNotification;

    }
}
