package com.example.socialmediaapp.dto;

import java.io.Serializable;

public class PostImage implements Serializable {
    private String ID;
    private String IMAGE;

    public PostImage(String ID, String IMAGE) {
        this.ID = ID;
        this.IMAGE = IMAGE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIMAGE() {
        return IMAGE;
    }
    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

}
