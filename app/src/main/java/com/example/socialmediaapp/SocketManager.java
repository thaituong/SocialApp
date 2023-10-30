package com.example.socialmediaapp;

import com.example.socialmediaapp.activity.MainActivity;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class SocketManager {
    private static Socket mSocket;

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
