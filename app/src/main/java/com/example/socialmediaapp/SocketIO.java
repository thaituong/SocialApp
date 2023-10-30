package com.example.socialmediaapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.example.socialmediaapp.activity.MainActivity;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class SocketIO extends Application {
    public static final String TAG = SocketIO.class.getSimpleName();
    private static SocketIO mInstance;
    public static io.socket.client.Socket mSocket;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeSocket();
        mInstance = this;
    }

    private void initializeSocket() {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;
        options.reconnectionDelay = 2000;
        options.reconnectionDelayMax = 5000;
        try {
            mSocket = IO.socket("https://socialappnew.onrender.com?accessToken="+ MainActivity.accessToken);
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            if(!mSocket.connected()){
                mSocket.connect();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
    public Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Socket Connected!");
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "onConnectError");
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "onDisconnect");
           /* if(!mSocket.connected()){
                mSocket.connect();
            }*/
        }
    };


    public static synchronized SocketIO getInstance() {
        SocketIO appController;
        synchronized (SocketIO.class) {
            appController = mInstance;
        }
        return appController;
    }

    public static Socket returnSocketInstance() {
        return mSocket;
    }

}