package com.arkadastakibi.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class App {

    public ArrayList<User> Users;
    public ArrayList<Post> Posts;
    public ArrayList<Comment> Comments;

    DataBase userDataBase;
    DataBase postDataBase;
    DataBase commentDataBase;

    public App(String userPath,String postPath,String commentPath){
        this.Users =  new ArrayList<>();
        this.Posts =  new ArrayList<>();
        this.Comments =  new ArrayList<>();
        this.userDataBase = new DataBase(userPath);
        this.postDataBase = new DataBase(postPath);
        this.commentDataBase = new DataBase(commentPath);

        JSONArray userArray = userDataBase.readData();

        for (int i = 0; i < userArray.length(); i++) {
            JSONObject user = userArray.getJSONObject(i);
            User myuser = new User(user);
            Users.add(myuser);
        }

        JSONArray postArray = postDataBase.readData();

        for (int i = 0; i < postArray.length(); i++) {
            JSONObject post = postArray.getJSONObject(i);
            Post mypost = new Post(post);
            Posts.add(mypost);
        }

        JSONArray commentArray = commentDataBase.readData();

        for (int i = 0; i < commentArray.length(); i++) {
            JSONObject comment = commentArray.getJSONObject(i);
            Comment mycomment = new Comment(comment);
            Comments.add(mycomment);
        }
    }

    public int update(){
        JSONArray usersArray = new  JSONArray();
        for (User user : Users) {
            usersArray.put(user.toJSON());
        }
        userDataBase.writeData(usersArray);

        JSONArray postsArray = new  JSONArray();
        for (Post post : Posts) {
            postsArray.put(post.toJSON());
        }
        postDataBase.writeData(postsArray);

        JSONArray commentsArray = new  JSONArray();
        for (Comment comment : Comments) {
            commentsArray.put(comment.toJSON());
        }
        commentDataBase.writeData(commentsArray);
        return 1;
    }

    public int delete(){
        return this.update();
    }

    public User search_to_user(int id){
        for (User user : Users) {
            if(user.getId() == id){
                return user;
            }
        }
        return null;
    }

    public Post search_to_post(int id){
        for (Post post : Posts){
            if(post.getPostId() == id){
                return post;
            }
        }
        return null;
    }

    public Comment search_to_comment(int id){
        for (Comment comment : Comments){
            if(comment.getId() == id){
                return comment;
            }
        }
        return null;
    }

}
