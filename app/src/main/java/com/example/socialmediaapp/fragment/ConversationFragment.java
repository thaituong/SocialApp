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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.RealPathUtil;
import com.example.socialmediaapp.SocketManager;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.adapter.ConversationAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ConversationDTO;
import com.example.socialmediaapp.dto.MessageDTO;
import com.example.socialmediaapp.dto.ResponseDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationFragment extends Fragment {
    private FragmentActivity fragmentActivity;
    public static final String TAG = ConversationFragment.class.getName();
    private static final int MY_REQUEST_CODE = 10;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private CircleImageView civUserAvatar;
    private MainActivity mMainActivity;
    private ConversationAdapter conversationAdapter;
    private RecyclerView rvConversation;
    private ResponseDTO litsp;
    private TextView tvUserName;
    int page = 2;
    Boolean typeImg=false;
    private ImageView ivBackConversation;
    private ImageView ivImgSend;
    private EditText etContent;
    private ImageView ivMessSend;
    private ImageView ivCommentIcon;
    private Uri selectedImageUri;
    ConversationDTO conversation;
    List<MessageDTO> messageDTOList = new ArrayList<>();

    public ConversationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        fragmentActivity = getActivity();
        cnView(view);
        setEvent();
        return view;
    }

    private void cnView(View view) {
        SocketManager.connect();
        rvConversation = view.findViewById(R.id.rvConversation);
        civUserAvatar = view.findViewById(R.id.civUserAvatar);
        tvUserName = view.findViewById(R.id.tvUserName);
        ivBackConversation = view.findViewById(R.id.ivBackConversation);
        ivImgSend = view.findViewById(R.id.ivImgSend);
        ivCommentIcon = view.findViewById(R.id.ivCommentIcon);
        etContent = view.findViewById(R.id.etContent);
        ivMessSend = view.findViewById(R.id.ivMessSend);
    }

    private void setEvent() {
        Bundle bundleReceive = getArguments();
        conversation = (ConversationDTO) bundleReceive.get("conversation");
        tvUserName.setText(conversation.getCONVERSATION().getUSER_CONVERSATIONs().get(0).getUSER().getFULLNAME());
        Glide.with(getContext()).load(conversation.getCONVERSATION().getUSER_CONVERSATIONs().get(0).getUSER().getAVATAR()).into(civUserAvatar);
        loadConversation();
        eventListenerSocket();
        // Back
        ivBackConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        // Check Send Message Button
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    ivMessSend.setVisibility(View.GONE);
                } else {
                    ivMessSend.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        // Send Message
        ivMessSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(typeImg==false){
                    if (etContent.getText().length() > 0) {
                        sendMessText();
                    }
                } else {
                    sendMessImg();
                }
                
            }
        });
        // Load Img
        ivCommentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        rvConversation.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if the user is scrolling up and has reached the top
                if (dy < 0 && !recyclerView.canScrollVertically(-1)) {
                    // Load more data when scrolling to the top
                    loadMoreData();
                }
            }
        });
    }

    private void loadMoreData() {
        // Implement your logic to load more data from the API
        // For example, you can increment the page number and make a new API call
        int nextPage = page + 1;
        ApiService.apiService.getListConversation(conversation.getCONVERSATION().getID(), nextPage, MainActivity.accessToken)
                .enqueue(new Callback<ResponseDTO>() {
                    @Override
                    public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                        // Process the response and update the adapter with new data
                        litsp = response.body();
                        for (int i = 0; i < litsp.getResult().getMesseges().size(); i++) {
                            messageDTOList.add(0, litsp.getResult().getMesseges().get(i));
                        }
                        conversationAdapter.notifyDataSetChanged();
                        page = nextPage; // Update the page number
                    }

                    @Override
                    public void onFailure(Call<ResponseDTO> call, Throwable t) {
                        Toast.makeText(getActivity(), "Call Api Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("API Response", "Giá trị litsp: " + t.getMessage());
                    }
                });
    }

    private void eventListenerSocket() {
        SocketManager.addEventListener("new-messege", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (args.length > 0) {
                    try {
                        String responseData = args[0].toString();
                        JSONObject jsonObject = new JSONObject(responseData);

                        String ID = jsonObject.getString("ID");
                        String SEND_USER_ID = jsonObject.getString("SEND_USER_ID");
                        String CONVERSATION_ID = jsonObject.getString("CONVERSATION_ID");
                        String TYPE = jsonObject.getString("TYPE");
                        String CONTENT = jsonObject.getString("CONTENT");
                        String updatedAt = jsonObject.getString("updatedAt");
                        String createdAt = jsonObject.getString("createdAt");
                        int IS_SEND_USER = jsonObject.getInt("IS_SEND_USER");
                        Log.d(TAG, "call: alooooo" + IS_SEND_USER);
                        fragmentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addConversation(ID, SEND_USER_ID, CONVERSATION_ID, TYPE, CONTENT, updatedAt, createdAt, IS_SEND_USER);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadConversationRecursive(final int index) {
        ApiService.apiService.getListConversation(conversation.getCONVERSATION().getID(), index, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp = response.body();
                for (int i = 0; i < litsp.getResult().getMesseges().size(); i++) {
                    messageDTOList.add(0, litsp.getResult().getMesseges().get(i));
                }
                if(index==0){
                    conversationAdapter = new ConversationAdapter(getContext(), messageDTOList);
                    rvConversation.setAdapter(conversationAdapter);
                } else {
                    conversationAdapter.notifyDataSetChanged();
                }

                if (index < 2) {
                    loadConversationRecursive(index + 1);
                } else {
                    scrollToBottom();
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Call Api Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });
    }
    private void scrollToBottom() {
        if (conversationAdapter != null && rvConversation != null) {
            rvConversation.post(new Runnable() {
                @Override
                public void run() {
                    rvConversation.scrollToPosition(conversationAdapter.getItemCount() - 1);
                }
            });
        }
    }

    // Gọi phương thức để bắt đầu quá trình đệ quy
    private void loadConversation() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvConversation.setLayoutManager(mLayoutManager);
        loadConversationRecursive(0);
    }


    private void sendMessText() {
        String content = etContent.getText().toString().trim();
        RequestBody requestBodyContent = RequestBody.create(MediaType.parse("multipart/form-data"), content);
        RequestBody requestBodyType = RequestBody.create(MediaType.parse("multipart/form-data"), "text");
        ApiService.apiService.postMessage(MainActivity.accessToken, conversation.getCONVERSATION().getID(), requestBodyType, requestBodyContent).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                ResponseDTO message = response.body();
                Toast.makeText(getActivity(), "Đã gửi", Toast.LENGTH_LONG).show();
                etContent.setText("");
            }
            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Gửi thất bại" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void sendMessImg() {
        String strRealPath= RealPathUtil.getRealPath(this.getContext(),selectedImageUri);
        File file=new File(strRealPath);
        RequestBody requestBodyFile=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part multipartBodyFile=MultipartBody.Part.createFormData("file",file.getName(),requestBodyFile);
        RequestBody requestBodyType = RequestBody.create(MediaType.parse("multipart/form-data"), "image");
        ApiService.apiService.postMessageImg(MainActivity.accessToken, conversation.getCONVERSATION().getID(), requestBodyType, multipartBodyFile).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                ResponseDTO message = response.body();
                Log.d("Alo1234", ""+message.getMessage());
                Toast.makeText(getActivity(), "Đã gửi ảnh", Toast.LENGTH_LONG).show();
                ivImgSend.setVisibility(View.GONE);
                etContent.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Gửi thất bại" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addConversation(String ID, String SEND_USER_ID, String CONVERSATION_ID, String TYPE, String CONTENT, String updatedAt, String createdAt, int IS_SEND_USER) {
        if (CONVERSATION_ID.equalsIgnoreCase(conversation.getCONVERSATION().getID())) {
            MessageDTO messageDTO = new MessageDTO(ID, SEND_USER_ID, CONVERSATION_ID, TYPE, CONTENT, updatedAt, createdAt, IS_SEND_USER);
            messageDTOList.add(messageDTO);
            conversationAdapter.notifyDataSetChanged();
            rvConversation.smoothScrollToPosition(conversationAdapter.getItemCount() - 1);
        }
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
            selectedImageUri = data.getData();
            ViewGroup.LayoutParams params = ivImgSend.getLayoutParams();
            params.width = 300;
            params.height = 300;
            ivImgSend.setLayoutParams(params);
            etContent.setVisibility(View.GONE);
            ivImgSend.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(selectedImageUri).into(ivImgSend);
            typeImg=true;
        }
    }
}
