package com.sbitbd.deshi_express.model;

public class state_rep_model {
    int img;
    String title,count;

    public state_rep_model(int img, String title, String count) {
        this.img = img;
        this.title = title;
        this.count = count;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
