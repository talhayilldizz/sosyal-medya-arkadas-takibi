package com.arkadastakibi.controller;

import com.arkadastakibi.interfaces.IFormKontrolu;
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
        btnLogin.setOnAction(event -> handleLogin(event));
        linkRegister.setOnAction(event -> navigateToRegister(event));
    }

    @Override
    public boolean validateForm() {
        return !txtUsername.getText().trim().isEmpty() && !txtPassword.getText().trim().isEmpty();
    }

    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (!validateForm()) {
            showMessage("Hata", "Lütfen tüm alanları doldurun.", Alert.AlertType.WARNING);
            return;
        }

        // JSON Dosyasından kullanıcıyı sorgula
        JSONObject foundUser = findUserInJson(username, password);

        //Kullanıcı bulunursa main sayfasına yönlendir
        if (foundUser != null) {
            MainPageController mainCtrl = changeScene(event, "/com.arkadastakibi/main-page.fxml", "Ana Sayfa");

            if (mainCtrl != null) {
                String adSoyad = foundUser.getString("firstName") + " " + foundUser.getString("lastName");
                String kAdi = foundUser.getString("username");
                String cinsiyet = "Erkek";
                if (foundUser.has("gender")) {
                    cinsiyet = foundUser.getString("gender");
                }
                mainCtrl.setKullaniciBilgileri(adSoyad, kAdi, cinsiyet);
            }
        } else {
            showMessage("Hata", "Hatalı kullanıcı adı veya şifre!", Alert.AlertType.ERROR);
        }
    }

    //Kullanıcıyı bulan metot(kullanıcı ve şifreye göre)
    private JSONObject findUserInJson(String username, String password) {
        String filePath = "users.json";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Henüz kayıtlı kullanıcı yok.");
            return null;
        }

        try {
            // Dosyayı string olarak oku
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray usersArray = new JSONArray(content);

            // Döngü ile kullanıcıları tara
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);

                // Kullanıcı adı ve şifre eşleşiyorsa return user
                if (user.getString("username").equals(username) && user.getString("password").equals(password)) {
                    return user;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void navigateToMainPage(JSONObject userData, ActionEvent event) {
        changeScene(event, "/com.arkadastakibi/main-page.fxml", "Ana sayfa");
    }

    private void navigateToRegister(ActionEvent event) {
        changeScene(event, "/com.arkadastakibi/register.fxml", "Kayıt Ol");
    }
}