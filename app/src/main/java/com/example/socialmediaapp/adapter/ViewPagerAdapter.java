package com.example.socialmediaapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.socialmediaapp.fragment.AddFragment;
import com.example.socialmediaapp.fragment.HomeFragment;
import com.example.socialmediaapp.fragment.MessageFragment;
import com.example.socialmediaapp.fragment.SearchFragment;
import com.example.socialmediaapp.fragment.ProfileFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new AddFragment();
            case 3:
                return new MessageFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 5;
    }
}
