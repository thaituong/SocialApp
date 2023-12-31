package com.example.socialmediaapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ResponseDTO;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URISyntaxException;

public class SocketManager {
    private static Socket mSocket;
    private static ResponseDTO litsp;
    public static void connect() {
        try {
            IO.Options options = new IO.Options();
            options.forceNew = true;
            mSocket = IO.socket("https://socialappnew.onrender.com?accessToken="+ MainActivity.accessToken);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        if (mSocket != null && mSocket.connected()) {
            mSocket.disconnect();
        }
    }

    public static void addSocketEventListener(Context context) {
        addEventListener("new-messege", new Emitter.Listener() {
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
                        if (!SEND_USER_ID.equalsIgnoreCase(MainActivity.userID)){
                            if(TYPE.equalsIgnoreCase("text")){
                                ApiService.apiService.getUserInfo(SEND_USER_ID, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                                    @Override
                                    public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                                        litsp = response.body();
                                        NotificationHelper.showNotification(context, litsp.getResult().getUser().getFULLNAME()+" vừa nhắn tin cho bạn",CONTENT);
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseDTO> call, Throwable t) {
                                        Log.d("API Response", "Giá trị litsp: " + t.getMessage());
                                    }
                                });
                            } else {
                                ApiService.apiService.getUserInfo(SEND_USER_ID, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                                    @Override
                                    public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                                        litsp = response.body();
                                        NotificationHelper.showNotification(context, litsp.getResult().getUser().getFULLNAME()+" vừa gửi hình cho bạn","image");
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseDTO> call, Throwable t) {
                                        Log.d("API Response", "Giá trị litsp: " + t.getMessage());
                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void addSocketEventListenerNoti(Context context) {
        addEventListener("like", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (args.length > 0) {
                    try {
                        String responseData = args[0].toString();
                        JSONObject jsonObject = new JSONObject(responseData);
                        String USER_ID = jsonObject.getString("USER_ID");
                        String POST_ID = jsonObject.getString("POST_ID");
                        if (!USER_ID.equalsIgnoreCase(MainActivity.userID)){
                            ApiService.apiService.getUserInfo(USER_ID, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                                @Override
                                public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                                    litsp = response.body();
                                    NotificationHelper.showNotification(context, litsp.getResult().getUser().getFULLNAME()+" vừa thích bài viết","❤️️");
                                }
                                @Override
                                public void onFailure(Call<ResponseDTO> call, Throwable t) {
                                    Log.d("API Response", "Giá trị litsp: " + t.getMessage());
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void addSocketEventListenerComment(Context context) {
        addEventListener("comment", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (args.length > 0) {
                    try {
                        String responseData = args[0].toString();
                        JSONObject jsonObject = new JSONObject(responseData);
                        String ID = jsonObject.getString("ID");
                        String CREATED_BY = jsonObject.getString("CREATED_BY");
                        String POST_ID = jsonObject.getString("POST_ID");
                        String CONTENT = jsonObject.getString("CONTENT");
                        if (!CREATED_BY.equalsIgnoreCase(MainActivity.userID)){
                            ApiService.apiService.getUserInfo(CREATED_BY, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                                @Override
                                public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                                    litsp = response.body();
                                    NotificationHelper.showNotification(context, litsp.getResult().getUser().getFULLNAME()+" vừa bình luận về bài viết",CONTENT);
                                }
                                @Override
                                public void onFailure(Call<ResponseDTO> call, Throwable t) {
                                    Log.d("API Response", "Giá trị litsp: " + t.getMessage());
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void addSocketEventListenerRComment(Context context) {
        addEventListener("r_comment", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (args.length > 0) {
                    try {
                        String responseData = args[0].toString();
                        JSONObject jsonObject = new JSONObject(responseData);
                        String ID = jsonObject.getString("ID");
                        String CREATED_BY = jsonObject.getString("CREATED_BY");
                        String POST_ID = jsonObject.getString("POST_ID");
                        String CONTENT = jsonObject.getString("CONTENT");
                        String COMMENT_REPLIED_TO = jsonObject.getString("COMMENT_REPLIED_TO");
                        if (!CREATED_BY.equalsIgnoreCase(MainActivity.userID)){
                            ApiService.apiService.getUserInfo(CREATED_BY, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                                @Override
                                public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                                    litsp = response.body();
                                    NotificationHelper.showNotification(context, litsp.getResult().getUser().getFULLNAME()+" vừa trả lời bình luận của bạn",CONTENT);
                                }
                                @Override
                                public void onFailure(Call<ResponseDTO> call, Throwable t) {
                                    Log.d("API Response", "Giá trị litsp: " + t.getMessage());
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void addSocketEventListenerFollow(Context context) {
        addEventListener("follow", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (args.length > 0) {
                    try {
                        String responseData = args[0].toString();
                        JSONObject jsonObject = new JSONObject(responseData);
                        String USER_ID = jsonObject.getString("USER_ID");
                        if (!USER_ID.equalsIgnoreCase(MainActivity.userID)){
                            ApiService.apiService.getUserInfo(USER_ID, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
                                @Override
                                public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                                    litsp = response.body();
                                    NotificationHelper.showNotification(context, litsp.getResult().getUser().getFULLNAME()+" vừa theo dõi bạn","\uD83E\uDD1D");
                                }
                                @Override
                                public void onFailure(Call<ResponseDTO> call, Throwable t) {
                                    Log.d("API Response", "Giá trị litsp: " + t.getMessage());
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void addEventListener(String event, Emitter.Listener listener) {
        if (mSocket != null) {
            mSocket.on(event, listener);
        }
    }

    public static void emit(String event, Object... args) {
        if (mSocket != null) {
            mSocket.emit(event, args);
        }
    }
}
