package com.example.socialmediaapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.adapter.ConversationAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ConversationDTO;
import com.example.socialmediaapp.dto.MessageDTO;
import com.example.socialmediaapp.dto.ResponseDTO;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationFragment extends Fragment {
    public static final String TAG=ConversationFragment.class.getName();
    private static final int MY_REQUEST_CODE = 10;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private CircleImageView civUserAvatar;
    private ConversationAdapter conversationAdapter;
    private ListView lvConversation;
    private ResponseDTO litsp;
    private TextView tvUserName;
    int page=0;
    private ImageView ivBackConversation;
    private ImageView ivImgSend;
    private EditText etContent;
    private ImageView ivMessSend;
    private ImageView ivCommentIcon;
    ConversationDTO conversation;
    List<MessageDTO> messageDTOList=new ArrayList<>();
    public ConversationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        lvConversation = (ListView) view.findViewById(R.id.lvConversation);
        civUserAvatar = (CircleImageView) view.findViewById(R.id.civUserAvatar);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        ivBackConversation = (ImageView) view.findViewById(R.id.ivBackConversation);
        ivImgSend = (ImageView) view.findViewById(R.id.ivImgSend);
        ivCommentIcon = (ImageView) view.findViewById(R.id.ivCommentIcon);
        etContent=(EditText) view.findViewById(R.id.etContent);
        ivMessSend=(ImageView) view.findViewById(R.id.ivMessSend);
        Bundle bundleReceive=getArguments();
        conversation= (ConversationDTO) bundleReceive.get("conversation");
        tvUserName.setText(conversation.getCONVERSATION().getUSER_CONVERSATIONs().get(0).getUSER().getFULLNAME());
        Glide.with(getContext()).load(conversation.getCONVERSATION().getUSER_CONVERSATIONs().get(0).getUSER().getAVATAR()).into(civUserAvatar);
        ivBackConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        ivMessSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etContent.getText().length()>0){
                    String content=etContent.getText().toString().trim();
                    RequestBody requestBodyContent=RequestBody.create(MediaType.parse("multipart/form-data"),content);
                    RequestBody requestBodyType=RequestBody.create(MediaType.parse("multipart/form-data"),"text");
                    ApiService.apiService.postMessage(MainActivity.accessToken,conversation.getCONVERSATION().getID(),requestBodyType,requestBodyContent).enqueue(new Callback<ResponseDTO>() {
                        @Override
                        public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                            ResponseDTO message=response.body();
                            Toast.makeText(getActivity(),"Đã gửi",Toast.LENGTH_LONG).show();
                            etContent.setText("");
                        }

                        @Override
                        public void onFailure(Call<ResponseDTO> call, Throwable t) {
                            Toast.makeText(getActivity(),"Gửi thất bại"+t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
        ivCommentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });


        ApiService.apiService.getListConversation(conversation.getCONVERSATION().getID(),0,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp=response.body();
                for(int i=0;i<litsp.getResult().getMesseges().size();i++){
                    messageDTOList.add(0,litsp.getResult().getMesseges().get(i));
                }
                conversationAdapter=new ConversationAdapter(getContext(),messageDTOList);
                lvConversation.setAdapter(conversationAdapter);
                lvConversation.smoothScrollToPosition(0);

            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(),"Call Api Error"+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });
        lvConversation.smoothScrollToPosition(0);
//        setupLazyLoading();
        return view;
    }
    private void openImagePicker() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, MY_REQUEST_CODE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            ViewGroup.LayoutParams params = ivImgSend.getLayoutParams();
            params.width = 300;
            params.height = 300;
            ivImgSend.setLayoutParams(params);
            Glide.with(getContext()).load(selectedImageUri).into(ivImgSend);

        }
    }
    private void setupLazyLoading() {
        lvConversation.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    // Gọi API để tải thêm dữ liệu ở đầu danh sách
                    page++;
                    if(page<5){
                        loadMoreDataAtTop(page);
                    }

                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Xử lý khác khi trạng thái cuộn thay đổi (nếu cần)
            }
        });
    }

    private void loadMoreDataAtTop(int page) {
        ApiService.apiService.getListConversation(conversation.getCONVERSATION().getID(),page,MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp=response.body();
                for(int i=0;i<litsp.getResult().getMesseges().size();i++){
                    messageDTOList.add(0,litsp.getResult().getMesseges().get(i));
                }
                conversationAdapter=new ConversationAdapter(getContext(),messageDTOList);
                lvConversation.setAdapter(conversationAdapter);
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(),"Call Api Error"+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });
    }

}