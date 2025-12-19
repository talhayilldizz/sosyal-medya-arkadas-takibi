package com.arkadastakibi.controller;

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

public class MainPageController extends BaseController {

    @FXML private TextField txtSearch;
    @FXML private Label lblMyName;
    @FXML private Label lblMyUsername;
    @FXML private Circle imgAvatar;
    @FXML private VBox vboxFriendsList;

    private String currentUsername; // Giriş yapan kullanıcı

    // Verileri doldurma ve Avatarı Ayarlama
    public void setKullaniciBilgileri(String name, String username, String gender) {
        this.currentUsername = username;

        lblMyName.setText(name);
        lblMyUsername.setText("@" + username);

        // Avatar Resmi
        String imagePath = (gender != null && gender.equalsIgnoreCase("Kadin"))
                ? "/images/woman.png" : "/images/man.png";

        try {
            URL url = getClass().getResource(imagePath);
            if (url != null) {
                imgAvatar.setFill(new ImagePattern(new Image(url.toExternalForm())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Listeyi doldur
        loadFriendsList();
    }

    // Arkadaş Listesini Dinamik Oluşturma
    private void loadFriendsList() {
        vboxFriendsList.getChildren().clear();
        String filePath = "users.json";
        File file = new File(filePath);
        if (!file.exists()) return;

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray usersArray = new JSONArray(content);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                String uName = user.getString("username");

                if (uName.equals(this.currentUsername)) continue; // Kendi adımı listede gösterme

                String fName = user.getString("firstName") + " " + user.getString("lastName");
                String gender = user.optString("gender", "Erkek");

                // Satır Tasarımı
                HBox row = new HBox();
                row.setAlignment(Pos.CENTER_LEFT);
                row.setSpacing(10);
                row.setStyle("-fx-cursor: hand; -fx-padding: 5;");

                // Küçük Avatar
                Circle avatar = new Circle(20);
                String imgPath = gender.equalsIgnoreCase("Kadin") ? "/images/woman.png" : "/images/man.png";
                URL url = getClass().getResource(imgPath);
                if(url != null) avatar.setFill(new ImagePattern(new Image(url.toExternalForm())));

                // İsimler
                VBox nameBox = new VBox();
                nameBox.setAlignment(Pos.CENTER_LEFT);
                Label nameLbl = new Label(fName);
                nameLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #212529; -fx-font-size: 14px;");
                Label userLbl = new Label("@" + uName);
                userLbl.setStyle("-fx-text-fill: #868686; -fx-font-size: 12px;");
                nameBox.getChildren().addAll(nameLbl, userLbl);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // Buton
                Button btnGit = new Button("Git >");
                btnGit.setStyle("-fx-background-color: transparent; -fx-text-fill: #1e88e5; -fx-font-weight: bold; -fx-cursor: hand;");
                btnGit.setOnAction(event -> navigateToFriendProfile(event, uName));

                row.getChildren().addAll(avatar, nameBox, spacer, btnGit);
                vboxFriendsList.getChildren().add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // arkadaş profiline git
    private void navigateToFriendProfile(ActionEvent event, String targetUser) {
        FriendsProfileController friendCtrl = changeScene(event, "/com.arkadastakibi/friends-profile.fxml", "Arkadaş Profili");

        if (friendCtrl != null) {
            friendCtrl.setArkadasBilgileri(targetUser, this.currentUsername);
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