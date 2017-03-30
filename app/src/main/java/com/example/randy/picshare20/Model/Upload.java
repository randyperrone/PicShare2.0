package com.example.randy.picshare20.Model;

/**
 * Created by Randy on 3/28/2017.
 */

public class Upload {
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String url) {
        this.url= url;
    }

    public String getUrl() {
        return url;
    }
}
