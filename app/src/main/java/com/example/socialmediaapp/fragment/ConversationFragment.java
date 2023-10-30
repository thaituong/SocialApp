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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationFragment extends Fragment {
    public static final String TAG = ConversationFragment.class.getName();
    private static final int MY_REQUEST_CODE = 10;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private CircleImageView civUserAvatar;
    private MainActivity mMainActivity;
    private ConversationAdapter conversationAdapter;
    private RecyclerView rvConversation;
    private ResponseDTO litsp;
    private TextView tvUserName;
    int page = 0;
    private ImageView ivBackConversation;
    private ImageView ivImgSend;
    private EditText etContent;
    private ImageView ivMessSend;
    private ImageView ivCommentIcon;
    ConversationDTO conversation;
    List<MessageDTO> messageDTOList = new ArrayList<>();

    public ConversationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        SocketManager.connect();
        rvConversation = view.findViewById(R.id.rvConversation);
        civUserAvatar = view.findViewById(R.id.civUserAvatar);
        tvUserName = view.findViewById(R.id.tvUserName);
        ivBackConversation = view.findViewById(R.id.ivBackConversation);
        ivImgSend = view.findViewById(R.id.ivImgSend);
        ivCommentIcon = view.findViewById(R.id.ivCommentIcon);
        etContent = view.findViewById(R.id.etContent);
        ivMessSend = view.findViewById(R.id.ivMessSend);

        Bundle bundleReceive = getArguments();
        conversation = (ConversationDTO) bundleReceive.get("conversation");
        tvUserName.setText(conversation.getCONVERSATION().getUSER_CONVERSATIONs().get(0).getUSER().getFULLNAME());
        Glide.with(getContext()).load(conversation.getCONVERSATION().getUSER_CONVERSATIONs().get(0).getUSER().getAVATAR()).into(civUserAvatar);

        ivBackConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra khi nào nên hiển thị nút
                if (s.toString().trim().isEmpty()) {
                    ivMessSend.setVisibility(View.GONE);
                } else {
                    ivMessSend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ivMessSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etContent.getText().length() > 0) {
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
            }
        });

        ivCommentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvConversation.setLayoutManager(mLayoutManager);

        ApiService.apiService.getListConversation(conversation.getCONVERSATION().getID(), 0, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp = response.body();
                for(int i=0;i<litsp.getResult().getMesseges().size();i++){
                    messageDTOList.add(0,litsp.getResult().getMesseges().get(i));
                }
                conversationAdapter = new ConversationAdapter(getContext(), messageDTOList);
                rvConversation.setAdapter(conversationAdapter);
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Call Api Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });

        SocketManager.addEventListener("new-messege", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (args.length > 0) {
                    try {
                        String responseData = args[0].toString();
                        JSONObject jsonObject = new JSONObject(responseData);

                        // Truy cập các trường dữ liệu trong JSON
                        String ID = jsonObject.getString("ID");
                        String SEND_USER_ID = jsonObject.getString("SEND_USER_ID");
                        String CONVERSATION_ID = jsonObject.getString("CONVERSATION_ID");
                        String TYPE = jsonObject.getString("TYPE");
                        String CONTENT = jsonObject.getString("CONTENT");
                        String updatedAt = jsonObject.getString("updatedAt");
                        String createdAt = jsonObject.getString("createdAt");
                        int IS_SEND_USER = jsonObject.getInt("IS_SEND_USER");
                        Log.d(TAG, "call: alooooo" + IS_SEND_USER);
                        getActivity().runOnUiThread(new Runnable() {
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
        return view;
    }

    private void addConversation(String ID, String SEND_USER_ID, String CONVERSATION_ID, String TYPE, String CONTENT, String updatedAt, String createdAt, int IS_SEND_USER) {
        if (CONVERSATION_ID.equalsIgnoreCase(conversation.getCONVERSATION().getID())) {
            MessageDTO messageDTO = new MessageDTO(ID, SEND_USER_ID, CONVERSATION_ID, TYPE, CONTENT, updatedAt, createdAt, IS_SEND_USER);
            messageDTOList.add(messageDTO);
            conversationAdapter.notifyDataSetChanged();
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
            Uri selectedImageUri = data.getData();
            ViewGroup.LayoutParams params = ivImgSend.getLayoutParams();
            params.width = 300;
            params.height = 300;
            ivImgSend.setLayoutParams(params);
            Glide.with(getContext()).load(selectedImageUri).into(ivImgSend);
        }
    }
}
