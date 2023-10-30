package com.example.socialmediaapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.RealPathUtil;
import com.example.socialmediaapp.adapter.ImagePagerAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ResponseDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFragment extends Fragment {
    private static final int MY_REQUEST_CODE = 10;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private MainActivity mMainActivity;
    private ImageView ivAddImg;
    private EditText etContent;
    private ViewPager vpImgPost;
    private Button btPost;
    Boolean check=false;
    private ProgressDialog progressDialog;
    ImagePagerAdapter adapter;
    List<Uri> imageUris = new ArrayList<>();
    public AddFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        ivAddImg=(ImageView) view.findViewById(R.id.ivAddImg);
        vpImgPost=(ViewPager) view.findViewById(R.id.vpImgPost);
        etContent=(EditText) view.findViewById(R.id.etContent);
        btPost=(Button) view.findViewById(R.id.btPost);
        adapter = new ImagePagerAdapter(this.getContext(), imageUris);
        progressDialog=new ProgressDialog(this.getContext());
        progressDialog.setMessage("Đang đăng ...");
        vpImgPost.setAdapter(adapter);
        ivAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiLoadPost();
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
                    btPost.setVisibility(View.GONE);
                } else {
                    btPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
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

    private void callApiLoadPost() {
        progressDialog.show();
        String caption=etContent.getText().toString().trim();
        RequestBody requestBodyCaption=RequestBody.create(MediaType.parse("multipart/form-data"),caption);
//        String strRealPath=RealPathUtil.getRealPath(this.getContext(),imageUris.get(0));
//        File file=new File(strRealPath);
//        RequestBody requestBodyFile=RequestBody.create(MediaType.parse("multipart/form-data"),file);
//        MultipartBody.Part multipartBodyFile=MultipartBody.Part.createFormData("file",file.getName(),requestBodyFile);

        List<MultipartBody.Part> multipartBodyFiles = new ArrayList<>();
        for (Uri imageUri : imageUris) {
            String strRealPath = RealPathUtil.getRealPath(this.getContext(), imageUri);
            File file = new File(strRealPath);
            RequestBody requestBodyFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part multipartBodyFile = MultipartBody.Part.createFormData("file", file.getName(), requestBodyFile);
            Log.d("Lik Img", ""+multipartBodyFile);
            multipartBodyFiles.add(multipartBodyFile);
        }

        ApiService.apiService.postNewFeed(requestBodyCaption,multipartBodyFiles, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                progressDialog.dismiss();
                ResponseDTO message=response.body();
                Toast.makeText(getActivity(),"Đăng thành công",Toast.LENGTH_LONG).show();
                imageUris.clear();
                adapter.notifyDataSetChanged();
                etContent.setText("");
                ((MainActivity) requireActivity()).goToHomeFragment();
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Đăng thất bại"+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageUris.add(selectedImageUri);
            adapter.notifyDataSetChanged();
            btPost.setVisibility(View.VISIBLE);
        }
    }
}