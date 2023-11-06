package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.dto.ImgDTO;
import com.example.socialmediaapp.dto.PostImage;
import com.squareup.picasso.Picasso; // Bạn có thể sử dụng Picasso để tải ảnh từ URI

import java.util.List;

public class ImageSPagerAdapter extends PagerAdapter {
    private Context context;
    private List<ImgDTO> imageUris;
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    public ImageSPagerAdapter(Context context, List<ImgDTO> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }
    public ImageSPagerAdapter() {
    }

    @Override
    public int getCount() {
        return imageUris.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
//        Glide.with(context).load(imageUris.get(position).getIMAGE()).into(imageView);
        ImgDTO imageItem = imageUris.get(position);
        if (imageItem.getImageUrl() != null) {
            Glide.with(context).load(imageItem.getImageUrl().getIMAGE()).into(imageView);
        } else if (imageItem.getImageUri() != null) {
            Picasso.get().load(imageItem.getImageUri()).into(imageView);
        }


        if (onItemLongClickListener != null) {
            final int finalPosition = position;
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(finalPosition);
                    return true;
                }
            });
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
