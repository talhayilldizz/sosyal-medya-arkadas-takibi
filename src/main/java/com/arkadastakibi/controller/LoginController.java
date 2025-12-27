package com.arkadastakibi.controller;

import com.arkadastakibi.interfaces.IFormKontrolu;
import com.arkadastakibi.model.App;
import com.arkadastakibi.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class LoginController extends BaseController implements Initializable, IFormKontrolu {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink linkRegister;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.app=new App("users.json","post.json","comment.json");

        btnLogin.setOnAction(event -> handleLogin(event));
        linkRegister.setOnAction(event -> navigateToRegister(event));
    }

    @Override
    public boolean validateForm() {
        return !txtUsername.getText().trim().isEmpty() && !txtPassword.getText().trim().isEmpty();
    }

    @Override
    public boolean isValidEmail(String email) {
        return false;
    }

    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (!validateForm()) {
            showMessage("Hata", "Lütfen tüm alanları doldurun.", Alert.AlertType.WARNING);
            return;
        }

        User foundUser=null;

        for(User user: app.Users){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                foundUser=user;
                break;
            }
        }

        //Kullanıcı bulunursa main sayfasına yönlendir
        if (foundUser != null) {
            MainPageController mainCtrl = changeScene(event, "/com.arkadastakibi/main-page.fxml", "Ana Sayfa");

            if (mainCtrl != null) {
                String addSoyad=foundUser.getFirstName() + " " + foundUser.getLastName();
                String kullaniciAdi=foundUser.getUsername();
                String cinsiyet=foundUser.getGender();

                mainCtrl.setKullaniciBilgileri(addSoyad,kullaniciAdi,cinsiyet);
            }
        } else {
            showMessage("Hata", "Hatalı kullanıcı adı veya şifre!", Alert.AlertType.ERROR);
        }
    }

    private void navigateToRegister(ActionEvent event) {
        changeScene(event, "/com.arkadastakibi/register.fxml", "Kayıt Ol");
    }
}