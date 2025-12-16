package com.arkadastakibi.controller;
import com.arkadastakibi.model.User;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable{
    @FXML
    private Label lblName;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblBio;
    @FXML
    private Label lblFollowers;
    @FXML
    private Label lblFollowing;
    @FXML
    private Button btnEditProfile;
    @FXML
    private Button btnDeleteAccount;
    @FXML
    private Button btnBack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserData();
    }


    private void loadUserData(){
        User user = new User(1,"TestAd","TestSoyad","Test","test@test.com","Yeni Hesap");

        user.setFollowingCount(100);
        user.setFollowerCount(100);

        lblName.setText(user.getUsername());
        lblEmail.setText(user.getEmail());
        lblBio.setText(user.getBio());
        lblFollowers.setText(String.valueOf(user.getFollowerCount()));
        lblFollowing.setText(String.valueOf(user.getFollowingCount()));
    }

    public void AnaSayfayaGeriDon(ActionEvent event){
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com.arkadastakibi/main-page.fxml"));
            Parent root = loader.load();

            MainPageController mainController = loader.getController();
            mainController.setKullaniciBilgileri("admin");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Ana sayfaya dönülemedi! Dosya yolu hatası olabilir.");
        }
    }
}
