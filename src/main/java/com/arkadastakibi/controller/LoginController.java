package com.arkadastakibi.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink linkForgotPassword;

    @FXML
    private Hyperlink linkRegister;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction(event -> handleLogin());

        linkRegister.setOnAction(event -> System.out.println("Kayıt ol sayfasına gidilecek..."));
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if(username.equals("admin") && password.equals("1234")) {
            System.out.println("Giriş Başarılı! Yönlendiriliyor...");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.arkadastakibi/main-page.fxml"));
                Parent root = loader.load();

                // MainPageController'a eriş ve veriyi gönder
                MainPageController mainController = loader.getController();
                mainController.setKullaniciBilgileri(username);

                // Sahneyi değiştir
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("FXML Yükleme Hatası: Dosya yolu yanlış olabilir.");
            }

        } else {
            System.out.println("Hatali kullanici adi veya sifre!");
        }
    }
}