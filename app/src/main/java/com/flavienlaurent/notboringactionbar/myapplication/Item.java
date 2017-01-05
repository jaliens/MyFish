package com.flavienlaurent.notboringactionbar.myapplication;

/**
 * Created by 도훈 on 2016-11-16.
 */

public class Item {
    String image;
    String nick;
    String age;
    String area;
    String key;
    String getImage() {
        return this.image;
    }
    String getAge(){
        return this.age;
    }
    String getKey() { return this.key;}
    String getarea(){
        return this.area;
    }
    String getNick() {
        return this.nick;
    }

    Item(String image, String nick, String age, String area, String key) {
        this.image = image;
        this.nick = nick;
        this.age = age;
        this.area = area;
        this.key = key;
    }


}