package com.flavienlaurent.notboringactionbar.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by User on 2016-08-11.
 */

public class ListItem {
    private Drawable iconD;
    private Bitmap iconDrawable ;
    private String titleStr ;
    private String descStr ;
    private int type;

    public void setType(int type){this.type = type ;}
    public void setIconDrawable(Drawable icon){ iconD = icon ;}
    public void setIcon(Bitmap icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public int getType(){return this.type;}
    public Drawable getIconD(){ return this.iconD; }
    public Bitmap getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}