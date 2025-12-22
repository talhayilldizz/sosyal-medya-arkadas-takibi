package com.arkadastakibi.model;

import org.json.JSONObject;

public class Comment {
    private int id;
    private int postId;
    private int userId;
    private String content;
    private String postDate;


    public Comment(int id, int postId, int userId, String content, String postDate) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.postDate = postDate;
    }
    public Comment(JSONObject commentJson){
        this.id = commentJson.getInt("id");
        this.postId = commentJson.getInt("postId");
        this.userId = commentJson.getInt("userId");
        this.content = commentJson.getString("content");
        this.postDate = commentJson.getString("postDate");
    }

    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("postId", postId);
        obj.put("userId", userId);
        obj.put("content", content);
        obj.put("postDate", postDate);
        return obj;
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
        return userId;
    }
    public void setUserId(int UserId) {
        this.userId = UserId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getPostDate() {
        return postDate;
    }
    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

}
