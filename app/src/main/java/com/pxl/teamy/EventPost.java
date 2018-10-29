package com.pxl.teamy;


import java.util.Date;

public class EventPost extends EventPostId {

    public String user_id, image_url, desc, image_thumb, location;
    public String time, date;
    public int aantalDeel;

    public EventPost(String user_id, String image_url, String desc, String image_thumb, String time, String date, String location, int aantalDeel) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.desc = desc;
        this.image_thumb = image_thumb;
        this.date = date;
        this.time = time;
        this.location = location;
        this.aantalDeel = aantalDeel;
    }

    public EventPost() {

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAantalDeel() {
        return aantalDeel;
    }

    public void setAantalDeel(int aantalDeel) {
        this.aantalDeel = aantalDeel;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_uri() {
        return image_url;
    }

    public void setImage_uri(String image_uri) {
        this.image_url = image_uri;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }


}
