package com.example.a64929.mynote;

public class Note {
    private String title="Title";
    private String time="1998";
    private String content="Hello World";
    private int id=0;
    public Note(int id,String title,String content){
        setId(id);
        setTitle(title);
        setContent(content);
    }

    public Note(int id,String title,String time,String content){
        setId(id);
        setTitle(title);
        setTime(time);
        setContent(content);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTime(String time){
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
