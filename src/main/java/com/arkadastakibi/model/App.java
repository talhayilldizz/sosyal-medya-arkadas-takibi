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

    // bu kısıman dosyaya veri yazma kodu yazılacak
}
