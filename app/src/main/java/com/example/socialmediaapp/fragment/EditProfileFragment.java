package com.example.socialmediaapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.RealPathUtil;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ResponseDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {
    public static final String TAG = EditProfileFragment.class.getName();
    private static final int MY_REQUEST_CODE = 10;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private MainActivity mMainActivity;
    private ResponseDTO litsp;
    private CircleImageView civUserAvatar;
    private EditText etUserName;
    private EditText etFullName;
    private EditText etMobile;
    private EditText etAddress;
    private EditText etDescriptions;
    private EditText etPassWord;
    private EditText etNewPassword;
    private EditText etReNewPassword;
    private Button btAvt;
    private Button btInfo;
    private Button btSecurity;
    private ImageView ivBackEditProfile;
    private Uri imageUri;
    private boolean isTextChanged = false;

    public EditProfileFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        cnView(view);
        setEvent();
        return view;
    }

    private void cnView(View view) {
        mMainActivity=(MainActivity)getActivity();
        civUserAvatar = (CircleImageView) view.findViewById(R.id.civUserAvatar);
        etUserName = (EditText) view.findViewById(R.id.etUserName);
        etFullName = (EditText) view.findViewById(R.id.etFullName);
        etMobile = (EditText) view.findViewById(R.id.etMobile);
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etDescriptions = (EditText) view.findViewById(R.id.etDescriptions);
        etPassWord = (EditText) view.findViewById(R.id.etPassWord);
        etNewPassword = (EditText) view.findViewById(R.id.etNewPassword);
        etReNewPassword = (EditText) view.findViewById(R.id.etReNewPassword);
        btAvt = (Button) view.findViewById(R.id.btAvt);
        btInfo = (Button) view.findViewById(R.id.btInfo);
        btSecurity = (Button) view.findViewById(R.id.btSecurity);
        ivBackEditProfile = (ImageView) view.findViewById(R.id.ivBackEditProfile);
    }

    private void setEvent() {
        loadUserInfo();
        civUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        btAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiPutUserAvt();
            }
        });
        btSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiPutUserSecurity();
            }
        });
        btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiPutUserInfo();
            }
        });
        ivBackEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment.loadUserInfo(getContext());
                ProfileFragment.loadPost(getContext());
                getFragmentManager().popBackStack();
            }
        });
    }

    private void callApiPutUserInfo() {
        String username = etUserName.getText().toString().trim();
        RequestBody requestBodyUserName = RequestBody.create(MediaType.parse("multipart/form-data"), username);
        String fullname = etFullName.getText().toString().trim();
        RequestBody requestBodyFullName = RequestBody.create(MediaType.parse("multipart/form-data"), fullname);
        String mobile = etMobile.getText().toString().trim();
        RequestBody requestBodyMobile = RequestBody.create(MediaType.parse("multipart/form-data"), mobile);
        String address = etAddress.getText().toString().trim();
        RequestBody requestBodyAddress = RequestBody.create(MediaType.parse("multipart/form-data"), address);
        String description = etDescriptions.getText().toString().trim();
        RequestBody requestBodyDescription = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody requestBodyGender = RequestBody.create(MediaType.parse("multipart/form-data"), "Male");
        ApiService.apiService.putUserInfo(requestBodyUserName,requestBodyFullName,requestBodyMobile,requestBodyAddress,requestBodyDescription,requestBodyGender, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                ResponseDTO message=response.body();
                Toast.makeText(getActivity(),""+message.getStatus().trim(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(),""+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callApiPutUserSecurity() {
        String password = etPassWord.getText().toString().trim();
        RequestBody requestBodyPassword = RequestBody.create(MediaType.parse("multipart/form-data"), password);
        String newpassword = etNewPassword.getText().toString().trim();
        RequestBody requestBodyNewPassword = RequestBody.create(MediaType.parse("multipart/form-data"), newpassword);
        String renewpassword = etReNewPassword.getText().toString().trim();
        RequestBody requestBodyReNewPassword = RequestBody.create(MediaType.parse("multipart/form-data"), renewpassword);
        ApiService.apiService.putUserPass(requestBodyPassword,requestBodyNewPassword,requestBodyReNewPassword, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                ResponseDTO message=response.body();
                Toast.makeText(getActivity(),""+message.getStatus().trim(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(),""+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUserInfo() {
        ApiService.apiService.getUserInfo(MainActivity.userID, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                litsp = response.body();
                Glide.with(getContext()).load(litsp.getResult().getUser().getAVATAR()).into(civUserAvatar);
                etUserName.setText(litsp.getResult().getUser().getUSERNAME());
                etFullName.setText(litsp.getResult().getUser().getFULLNAME());
                etMobile.setText(litsp.getResult().getUser().getMOBILE());
                etAddress.setText(litsp.getResult().getUser().getADDRESS());
                etDescriptions.setText(litsp.getResult().getUser().getDESCRIPTION());
            }
            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Call Api Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("API Response", "Giá trị litsp: " + t.getMessage());
            }
        });
    }

    private void openImagePicker() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, MY_REQUEST_CODE);
        }
    }

    private void callApiPutUserAvt() {

        String strRealPath=RealPathUtil.getRealPath(this.getContext(),imageUri);
        File file=new File(strRealPath);
        RequestBody requestBodyFile=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part multipartBodyFile=MultipartBody.Part.createFormData("file",file.getName(),requestBodyFile);

        ApiService.apiService.putUserAvatar(multipartBodyFile, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                ResponseDTO message=response.body();
                Toast.makeText(getActivity(),""+message.getStatus().trim(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(getActivity(),""+t.getMessage(),Toast.LENGTH_LONG).show();
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
            imageUri=data.getData();
            Glide.with(getContext()).load(imageUri).into(civUserAvatar);
            btAvt.setVisibility(View.VISIBLE);
        }
    }
}