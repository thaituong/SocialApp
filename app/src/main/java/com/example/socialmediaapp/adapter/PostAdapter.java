package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ResponseDTO;
import com.example.socialmediaapp.fragment.HomeFragment;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.dto.NewFeedDTO;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<NewFeedDTO> listData;
    private IClickItemListener iClickItemListener;
    private iClickItemListenerSetting iClickItemListenerSetting;
    private iClickItemListenerProfile iClickItemListenerProfile;
    public interface IClickItemListener{
        void onClickItemUser(NewFeedDTO itemPostDTO);
    }
    public interface iClickItemListenerSetting{
        void onClickItemSetting(NewFeedDTO itemPostDTO);
    }
    public interface iClickItemListenerProfile{
        void onClickItemProfile(NewFeedDTO itemPostDTO);
    }
    private Context context;
    private PhotoAdapter photoAdapter;
    public PostAdapter(Context aContext, List<NewFeedDTO> listData, IClickItemListener listener, iClickItemListenerSetting listenerSetting, iClickItemListenerProfile listenerProfile) {
        this.context = aContext;
        this.listData=listData;
        layoutInflater = LayoutInflater.from(aContext);
        this.iClickItemListener=listener;
        this.iClickItemListenerSetting=listenerSetting;
        this.iClickItemListenerProfile=listenerProfile;
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
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_post_item, null);
            holder = new ViewHolder();
            holder.tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            holder.civUserAvatar = (CircleImageView) view.findViewById(R.id.civUserAvatar);
            holder.tvPostTime = (TextView) view.findViewById(R.id.tvPostTime);
            holder.tvCaption = (TextView) view.findViewById(R.id.tvCaption);
            holder.imgViewPost=(ImageView) view.findViewById(R.id.imgViewPost);
            holder.ivHeart = (ImageView) view.findViewById(R.id.ivHeart);
            holder.ivChat = (ImageView) view.findViewById(R.id.ivChat);
            holder.tvSLHeart = (TextView) view.findViewById(R.id.tvSLHeart);
            holder.tvSLComment = (TextView) view.findViewById(R.id.tvSLComment);
            holder.ivEditPost = (ImageView) view.findViewById(R.id.ivEditPost);
            holder.llUser=(LinearLayout) view.findViewById(R.id.llUser);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final NewFeedDTO itemPostDTO = this.listData.get(i);
        if(!itemPostDTO.getUSER().getID().equalsIgnoreCase(MainActivity.userID)){
            holder.ivEditPost.setVisibility(View.GONE);
        }else{
            holder.ivEditPost.setVisibility(View.VISIBLE);
        }
        if (itemPostDTO.getISLIKED().equalsIgnoreCase("0")){
            holder.ivHeart.setImageResource(R.drawable.heart);
        }else {
            holder.ivHeart.setImageResource(R.drawable.redheart);
        }
        holder.tvUserName.setText(itemPostDTO.getUSER().getFULLNAME());

        Glide.with(context).load(itemPostDTO.getUSER().getAVATAR()).into(holder.civUserAvatar);

        Instant instant = Instant.parse(itemPostDTO.getCreatedAt().toString());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        String formattedDate = zonedDateTime.format(formatter);
        holder.tvPostTime.setText(formattedDate);

        Log.d("Aloo1", ""+itemPostDTO.getCreatedAt().toString());
        Log.d("Aloo2", ""+formattedDate);
        holder.tvCaption.setText(itemPostDTO.getCAPTION());
        if(itemPostDTO.getPOST_IMAGEs().size()>0){
            holder.imgViewPost.setVisibility(View.VISIBLE);
            Glide.with(context).load(itemPostDTO.getPOST_IMAGEs().get(0).getIMAGE()).into(holder.imgViewPost);
//            Picasso.get().load(itemPostDTO.getPOST_IMAGEs().get(0).getIMAGE()).into(holder.imgViewPost);
        }else if(itemPostDTO.getPOST_IMAGEs().size()==0){
            holder.imgViewPost.setVisibility(View.GONE);
        }

        holder.tvSLHeart.setText(itemPostDTO.getLIKES()+" Likes");
        holder.tvSLComment.setText(itemPostDTO.getCOMMENTS()+" Comments");
        holder.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemPostDTO.getISLIKED().equalsIgnoreCase("0")){
                    ApiService.apiService.postLike(itemPostDTO.getID(),MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                        @Override
                        public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                            ResponseDTO message = response.body();
                        }

                        @Override
                        public void onFailure(Call<ResponseDTO> call, Throwable t) {
                            Toast.makeText(context, "Like thất bại" + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    holder.ivHeart.setImageResource(R.drawable.redheart);
                    itemPostDTO.setISLIKED("1");
                    holder.tvSLHeart.setText(Integer.parseInt(itemPostDTO.getLIKES())+1+" Likes");
                    itemPostDTO.setLIKES(Integer.parseInt(itemPostDTO.getLIKES())+1+"");
                }else {
                    ApiService.apiService.postUnLike(itemPostDTO.getID(),MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                        @Override
                        public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                            ResponseDTO message = response.body();
                        }

                        @Override
                        public void onFailure(Call<ResponseDTO> call, Throwable t) {
                            Toast.makeText(context, "Un Like thất bại" + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    holder.ivHeart.setImageResource(R.drawable.heart);
                    itemPostDTO.setISLIKED("0");
                    holder.tvSLHeart.setText(Integer.parseInt(itemPostDTO.getLIKES())-1+" Likes");
                    itemPostDTO.setLIKES(Integer.parseInt(itemPostDTO.getLIKES())-1+"");
                }
            }
        });
        holder.imgViewPost.setOnTouchListener(new View.OnTouchListener() {
            private float x1, x2;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        if (deltaX < 0) {
                            int imgpos1 = HomeFragment.imgpos[i]+1;
                            HomeFragment.imgpos[i]++;
                            if(imgpos1<itemPostDTO.getPOST_IMAGEs().size()){
                                Glide.with(context).load(itemPostDTO.getPOST_IMAGEs().get(imgpos1).getIMAGE().toString()).into(holder.imgViewPost);
                            }else{
                                HomeFragment.imgpos[i]--;
                            }
                        } else if (deltaX > 0){
                            int imgpos1 = HomeFragment.imgpos[i]-1;
                            HomeFragment.imgpos[i]--;
                            if(imgpos1>-1){
                                Glide.with(context).load(itemPostDTO.getPOST_IMAGEs().get(imgpos1).getIMAGE().toString()).into(holder.imgViewPost);
                            }else{
                                HomeFragment.imgpos[i]++;
                            }
                        }
                        return true;
                }
                return false;
            }
        });
        holder.ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.onClickItemUser(itemPostDTO);
            }
        });
        holder.ivEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListenerSetting.onClickItemSetting(itemPostDTO);
            }
        });
        holder.llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListenerProfile.onClickItemProfile(itemPostDTO);
            }
        });

        return view;
    }

    static class ViewHolder {
        TextView tvUserName;
        CircleImageView civUserAvatar;
        TextView tvPostTime;
        TextView tvCaption;
        ImageView imgViewPost;
        ImageView ivHeart;
        ImageView ivChat;
        TextView tvSLHeart;
        TextView tvSLComment;
        ViewPager viewPager;
        ImageView ivEditPost;
        LinearLayout llUser;

    }
}
