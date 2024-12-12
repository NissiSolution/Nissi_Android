package com.nissisolution.nissibeta.Classes;

public class SupportSos {
    public int position;
    public String title, content, author, timestamp;

    public SupportSos() {
    }

    public SupportSos(int position, String title, String content, String author, String timestamp) {
        this.position = position;
        this.title = title;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
    }
}
