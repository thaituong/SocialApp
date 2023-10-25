package com.example.socialmediaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.AccountDTO;
import com.example.socialmediaapp.dto.ResponseDTO;
import com.example.socialmediaapp.dto.ResultDTO;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODER = 10;
    private Button btLogin;
    private Button btCreateAccount;
    private EditText emailET;
    private EditText passET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailET=(EditText)findViewById(R.id.emailET);
        passET=(EditText)findViewById(R.id.passET);
        btLogin=(Button)findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody requestBodyEmail=RequestBody.create(MediaType.parse("multipart/form-data"),emailET.getText().toString());
                RequestBody requestBodyPassword=RequestBody.create(MediaType.parse("multipart/form-data"),passET.getText().toString());
                sendDN(requestBodyEmail,requestBodyPassword);
            }
        });
        btCreateAccount=(Button)findViewById(R.id.btCreateAccount);
        btCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent1);
            }
        });
    }
    private void sendDN(RequestBody email,RequestBody password) {
        ApiService.apiService.postLogin("*",email,password).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                    ResponseDTO responseDTO = response.body();
                    if (responseDTO.getStatus().equals("Accepted")) {
                        ResultDTO resultDTO = responseDTO.getResult();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("login", resultDTO);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, MY_REQUEST_CODER);
                    } else {
                        Toast.makeText(LoginActivity.this, responseDTO.getMessage(), Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Log.e("Response", "Call to API failed", t);
                Toast.makeText(LoginActivity.this, "Call API Error", Toast.LENGTH_LONG).show();
            }
        });
    }

}