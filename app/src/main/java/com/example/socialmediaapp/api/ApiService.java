package com.example.socialmediaapp.api;

import com.example.socialmediaapp.Const;
import com.example.socialmediaapp.dto.ResponseDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson=new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Set the connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Set the read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Set the write timeout
            .build();

    ApiService apiService=new Retrofit.Builder()
            .baseUrl("https://socialappnew.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(ApiService.class);

    @GET("home")
    Call<ResponseDTO> getListPost(@Header("accessToken") String accessToken);

    @GET("post/comment/{id}")
    Call<ResponseDTO> getListComment(@Path("id") String groupId, @Header("accessToken") String accessToken);

    @Multipart
    @POST("post")
    Call<ResponseDTO> postNewFeed(@Part(Const.KEY_CAPTION) RequestBody caption, @Part List<MultipartBody.Part> file, @Header("accessToken") String accessToken);

    @GET("messege")
    Call<ResponseDTO> getListMessage(@Header("accessToken") String accessToken);
    @Multipart
    @POST("signIn")
    Call<ResponseDTO> postLogin(@Header("Accept") String acceptHeader,@Part(Const.KEY_EMAIL) RequestBody email,@Part(Const.KEY_PASSWORD) RequestBody password);

    @GET("messege/{id}")
    Call<ResponseDTO> getListConversation(@Path("id") String groupId, @Query("page") int page, @Header("accessToken") String accessToken);

    @Multipart
    @POST("messege/{id}")
    Call<ResponseDTO> postMessage(@Header("accessToken") String accessToken,@Path("id") String groupId,@Part(Const.KEY_CONTENT_TYPE) RequestBody type,@Part(Const.KEY_CONTENT) RequestBody content);

    @GET("user/info/{id}")
    Call<ResponseDTO> getUserInfo(@Path("id") String groupId,@Header("accessToken") String accessToken);

    @GET("post/user/{id}")
    Call<ResponseDTO> getUserPost(@Path("id") String groupId,@Header("accessToken") String accessToken);

    @Multipart
    @PUT("user/avatar")
    Call<ResponseDTO> putUserAvatar(@Part MultipartBody.Part file, @Header("accessToken") String accessToken);

    @POST("post/like/{id}")
    Call<ResponseDTO> postLike(@Path("id") String groupId,@Header("accessToken") String accessToken);

    @DELETE("post/like/{id}")
    Call<ResponseDTO> postUnLike(@Path("id") String groupId,@Header("accessToken") String accessToken);

    @Multipart
    @PUT("post/{id}")
    Call<ResponseDTO> postEdit(@Path("id") String groupId,@Part(Const.KEY_CAPTION) RequestBody caption,@Part(Const.KEY_ARRDELETE) RequestBody deletedImages, @Part List<MultipartBody.Part> file, @Header("accessToken") String accessToken);

//    @POST("taikhoan/otp")
//    Call<OTP> sendOTP(@Body Email email);
//
//    @POST("taikhoan/dangky")
//    Call<Message> sendTTDK(@Body TTCN ttcn);
//


}
