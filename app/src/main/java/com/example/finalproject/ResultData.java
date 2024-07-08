package com.example.finalproject;

public class ResultData {
    private String result;
    private int imageUrl;
    private String classname;

    public ResultData(String result, int imageUrl, String classname) {
        this.result = result;
        this.imageUrl = imageUrl;
        this.classname = classname;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }
}
