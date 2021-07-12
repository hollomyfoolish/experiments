package cn.hollomyfoolish.mq;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

public class B1Message {
    private int idx;
    private String message;

    public B1Message() {
    }

    public B1Message(int idx, String message) {
        this.idx = idx;
        this.message = message;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] toBytes(){
        return new Gson().toJson(this).getBytes(StandardCharsets.UTF_8);
    }
}
