package com.flavienlaurent.notboringactionbar.myapplication;

import android.graphics.drawable.Drawable;

/**
 * Created by user on 2016-12-22.
 */

public class GamesItem {
    public Drawable image;
    private String title;

    public Drawable getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    GamesItem(Drawable image, String title){
        this.image = image;
        this.title = title;
    }
}