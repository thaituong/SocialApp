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
    ViewPagerAdapter adapter;
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
                        bottomNavigationView.getMenu().findItem(R.id.menu_setting).setChecked(true);
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
                } else if(item.getItemId()==R.id.menu_setting) {
                    viewPager.setCurrentItem(4);
                }
                return true;

            }
        });
    }
    public void goToCommentFragment(String id){
        ApiService.apiService.getListComment(id.toString(),accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                ResponseDTO responseDTO=response.body();
                FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
                CommentFragment commentFragment=new CommentFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("repcomment",responseDTO);
                commentFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainLayout,commentFragment);
                fragmentTransaction.addToBackStack(CommentFragment.TAG);
                fragmentTransaction.commit();
            }
            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Call Api Error"+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });
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
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        HomeFragment homeFragment = new HomeFragment();
//        fragmentTransaction.replace(R.id.pagerLayout, homeFragment);
//        fragmentTransaction.addToBackStack(HomeFragment.TAG);
//        fragmentTransaction.commit();
        adapter.notifyItemChanged(0);
        viewPager.setCurrentItem(0);
    }

}
