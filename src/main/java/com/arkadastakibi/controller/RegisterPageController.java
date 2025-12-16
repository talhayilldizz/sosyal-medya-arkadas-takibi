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

public class RegisterPageController implements Initializable {
    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Button btnRegister;

    @FXML
    private Hyperlink linkLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnRegister.setOnAction(event -> handleRegister()); //Butona Basınca çalışacak

        linkLogin.setOnAction(event -> navigateToLogin()); //Logine gönderecek
    }

    private void handleRegister(){
        String username = txtUsername.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        //Şimdilik konsola yazdır
        System.out.println("Kayıt İşlemi Başlatıldı:");
        System.out.println("Kullanıcı Adı: " + username);
        System.out.println("Email: " + email);

        if(username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Hata: Lütfen tüm alanları doldurun!");
            return; // Alanlar boşsa yönlendirme yapma
        }

        if(!password.equals(confirmPassword)) {
            System.out.println("Şifreler Uyuşmuyor..");
            return;
        }

        navigateToLogin();
    }

    private void navigateToLogin(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.arkadastakibi/login.fxml"));
            Parent root=loader.load();

            Stage stage = (Stage) btnRegister.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Hata: Login sayfası yüklenemedi. Dosya yolunu kontrol et.");
        }
    }
}
