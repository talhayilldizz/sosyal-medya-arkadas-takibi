package com.arkadastakibi.controller;

import com.arkadastakibi.model.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;
import org.json.JSONArray;
import org.json.JSONObject;



import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainPageController extends BaseController {

    @FXML private TextField txtSearch;
    @FXML private Label lblMyName;
    @FXML private Label lblMyUsername;
    @FXML private Circle imgAvatar;
    @FXML private VBox vboxFriendsList;
    @FXML private VBox vboxAllUsers;


    private User loggedUser;

    // Verileri doldurma ve Avatarı Ayarlama
    public void setKullaniciBilgileri(String name, String username, String gender) {
        if(this.app.Users == null){
            return;
        }

        for(User user : this.app.Users){
            if(user.getUsername().equals(username)){
                this.loggedUser = user;
                break;
            }
        }

        if(this.loggedUser == null){
            return;
        }

        lblMyName.setText(this.loggedUser.getFirstName()+ " "+this.loggedUser.getLastName());
        lblMyUsername.setText("@" + username);

        imgAvatar.setFill(getAvatarPattern(this.loggedUser.getGender()));

        loadList();

    }

    //Tek metotla iki listeyi doldur
    private void loadList(){
        vboxAllUsers.getChildren().clear();
        vboxFriendsList.getChildren().clear();

        if(this.app.Users == null || this.loggedUser == null){
            return;
        }

        List<Integer> followingUserId = this.loggedUser.getFollowingUser();
        for(User user : this.app.Users){
            if(user.getId() == this.loggedUser.getId()){
                continue;
            }
            vboxAllUsers.getChildren().add(createUserRow(user));
            if(followingUserId.contains(user.getId())){
                vboxFriendsList.getChildren().add(createUserRow(user));
            }
        }
    }


    //Tasarım Kısmı
    private HBox createUserRow(User user) {
        String uName = user.getUsername();
        String name = user.getFirstName() + " " + user.getLastName();
        String gender = user.getGender();

        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setSpacing(10);
        // Altına hafif çizgi çektik, daha şık durur
        row.setStyle("-fx-cursor: hand; -fx-padding: 8; -fx-border-color: #f0f0f0; -fx-border-width: 0 0 1 0;");

        // Avatar
        Circle avatar = new Circle(18); // Biraz küçülttüm kibar dursun diye
        avatar.setFill(getAvatarPattern(gender));

        // İsimler
        VBox nameBox = new VBox();
        nameBox.setAlignment(Pos.CENTER_LEFT);
        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #212529; -fx-font-size: 13px;");
        Label userLbl = new Label("@" + uName);
        userLbl.setStyle("-fx-text-fill: #868686; -fx-font-size: 11px;");
        nameBox.getChildren().addAll(nameLbl, userLbl);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnGit = new Button("Git >");
        btnGit.setStyle("-fx-background-color: transparent; -fx-text-fill: #1e88e5; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 11px;");
        btnGit.setOnAction(event -> navigateToFriendProfile(event, uName));

        row.getChildren().addAll(avatar, nameBox, spacer, btnGit);
        return row;
    }

    // 4. AVATAR YARDIMCI METODU (Kod tekrarını önlemek için)
    private ImagePattern getAvatarPattern(String gender) {
        String imgPath = (gender != null && gender.equalsIgnoreCase("Kadin"))
                ? "/images/woman.png" : "/images/man.png";
        try {
            URL url = getClass().getResource(imgPath);
            if (url != null) {
                return new ImagePattern(new Image(url.toExternalForm()));
            }
        } catch (Exception e) { }
        return null;
    }

    // arkadaş profiline git
    private void navigateToFriendProfile(ActionEvent event, String targetUser) {
        FriendsProfileController friendCtrl = changeScene(event, "/com.arkadastakibi/friends-profile.fxml", "Arkadaş Profili");

        if (friendCtrl != null) {
            friendCtrl.setArkadasBilgileri(targetUser, this.loggedUser.getUsername());
        }
    }

    // kendi profiline git
    @FXML
    public void ProfileGit(ActionEvent event) {
        String kAdi = lblMyUsername.getText().replace("@", "");
        UserProfileController profileCtrl = changeScene(event, "/com.arkadastakibi/profil-page.fxml", "Profilim");
        if (profileCtrl != null) {
            profileCtrl.setKullaniciAdi(kAdi);
        }
    }

    // çıkış yap
    public void CikisYap(ActionEvent event) {
        changeScene(event, "/com.arkadastakibi/login.fxml", "Giriş Yap");
    }
}