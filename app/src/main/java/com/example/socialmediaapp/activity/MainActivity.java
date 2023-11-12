package com.example.socialmediaapp.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.socialmediaapp.SocketManager;
import com.example.socialmediaapp.dto.NewFeedDTO;
import com.example.socialmediaapp.fragment.EditPostFragment;
import com.example.socialmediaapp.fragment.EditProfileFragment;
import com.example.socialmediaapp.fragment.FProfileFragment;
import com.example.socialmediaapp.fragment.HomeFragment;


import com.example.socialmediaapp.R;
import com.example.socialmediaapp.adapter.ViewPagerAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ConversationDTO;
import com.example.socialmediaapp.dto.ResponseDTO;
import com.example.socialmediaapp.dto.ResultDTO;
import com.example.socialmediaapp.fragment.CommentFragment;
import com.example.socialmediaapp.fragment.ConversationFragment;
import com.example.socialmediaapp.fragment.HomeFragment;
import com.example.socialmediaapp.fragment.ListFollowFragment;
import com.example.socialmediaapp.fragment.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static String accessToken;
    public static String userID;
    private ViewPager2 viewPager;
    private ResultDTO resultDTO;
    private BottomNavigationView bottomNavigationView;
    public static ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getExtras() != null) {
            resultDTO= (ResultDTO) getIntent().getExtras().get("login");
            accessToken=resultDTO.getToken();
            userID=resultDTO.getUser().getID();
        }
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        viewPager=findViewById(R.id.viewPager);
        bottomNavigationView=findViewById(R.id.bottomNavigation);
        SocketManager.connect();
        SocketManager.addSocketEventListener(getBaseContext());
        SocketManager.addSocketEventListenerNoti(getBaseContext());
        SocketManager.addSocketEventListenerComment(getBaseContext());
        SocketManager.addSocketEventListenerRComment(getBaseContext());
        SocketManager.addSocketEventListenerFollow(getBaseContext());
        adapter=new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_search).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_add).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_message).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.menu_profile).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_home){
                    viewPager.setCurrentItem(0);
                } else if(item.getItemId()==R.id.menu_search){
                    viewPager.setCurrentItem(1);
                } else if(item.getItemId()==R.id.menu_add) {
                    viewPager.setCurrentItem(2);
                } else if(item.getItemId()==R.id.menu_message) {
                    viewPager.setCurrentItem(3);
                } else if(item.getItemId()==R.id.menu_profile) {
                    viewPager.setCurrentItem(4);
                }
                return true;

            }
        });
    }
    public void goToCommentFragment(String id){
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        CommentFragment commentFragment=new CommentFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("repcomment",id);
        commentFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.mainLayout,commentFragment);
        fragmentTransaction.addToBackStack(CommentFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToConversationFragment(ConversationDTO conversation){
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        ConversationFragment conversationFragment=new ConversationFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("conversation",conversation);
        conversationFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.mainLayout,conversationFragment);
        fragmentTransaction.addToBackStack(ConversationFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToHomeFragment() {
        adapter.notifyItemChanged(0);
        viewPager.setCurrentItem(0);
    }

    public void goToProfileFragment() {
        adapter.notifyItemChanged(4);
        viewPager.setCurrentItem(4);
    }

    public void goToEditProfileFragment(){
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        EditProfileFragment editProfileFragment=new EditProfileFragment();
        Bundle bundle=new Bundle();
        editProfileFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.mainLayout,editProfileFragment);
        fragmentTransaction.addToBackStack(EditProfileFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToEditPostFragment(NewFeedDTO newFeedDTO){
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        EditPostFragment editPostFragment=new EditPostFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("editpost",newFeedDTO);
        editPostFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.mainLayout,editPostFragment);
        fragmentTransaction.addToBackStack(EditPostFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToFProfileFragment(String id){
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        FProfileFragment fProfileFragment=new FProfileFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("idprofile",id);
        fProfileFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.mainLayout,fProfileFragment);
        fragmentTransaction.addToBackStack(FProfileFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToListFollowFragment(ResultDTO resultDTO){
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        ListFollowFragment listFollowFragment=new ListFollowFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("listfollower",resultDTO);
        listFollowFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.mainLayout,listFollowFragment);
        fragmentTransaction.addToBackStack(ListFollowFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToNotificationFragment(){
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        NotificationFragment notificationFragment=new NotificationFragment();
        Bundle bundle=new Bundle();
        notificationFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.mainLayout,notificationFragment);
        fragmentTransaction.addToBackStack(NotificationFragment.TAG);
        fragmentTransaction.commit();
    }


}
