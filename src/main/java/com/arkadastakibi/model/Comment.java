package com.arkadastakibi.model;

public class Comment {
    private int id;
    private int postId;
    private int UserId;
    private String content;


    public Comment(int id, int postId, int UserId, String content) {
        this.id = id;
        this.postId = postId;
        this.UserId = UserId;
        this.content = content;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }
    public int getUserId() {
        return UserId;
    }
    public void setUserId(int UserId) {
        this.UserId = UserId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
