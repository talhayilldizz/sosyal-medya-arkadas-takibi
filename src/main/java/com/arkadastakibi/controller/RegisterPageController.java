package com.arkadastakibi.controller;

import com.arkadastakibi.interfaces.IFormKontrolu;
import com.arkadastakibi.model.DataBase;
import com.arkadastakibi.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;




public class RegisterPageController extends BaseController implements Initializable, IFormKontrolu {
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
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
    @FXML
    private Label lblMessage;
    @FXML
    private RadioButton radioMale;
    @FXML
    private RadioButton radioFemale;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnRegister.setOnAction(event -> handleRegister()); //Butona Basınca çalışacak
        linkLogin.setOnAction(event -> navigateToLogin(event)); //Logine gönderecek
    }

    @Override
    public boolean validateForm() {
        return !txtFirstName.getText().isEmpty() &&
                !txtLastName.getText().isEmpty() &&
                !txtUsername.getText().isEmpty() &&
                !txtEmail.getText().isEmpty() &&
                !txtPassword.getText().isEmpty() &&
                !txtConfirmPassword.getText().isEmpty();
    }

    @Override
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@(gmail|hotmail)\\.com$";
        return email != null && email.matches(emailRegex);
    }

    private void handleRegister(){
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String username = txtUsername.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String gender = "Erkek";

        if(radioFemale.isSelected()){
            gender = "Kadin";
        }

        if(!validateForm()){
            lblMessage.setText("Boş Alanları Doldurunuz!");
            lblMessage.setVisible(true);
            return;
        }

        if(!isValidEmail(email)) {
            lblMessage.setText("Geçersiz E-posta Formatı!");
            lblMessage.setVisible(true);
            return;
        }

        if(!password.equals(confirmPassword)) {
            lblMessage.setText("Şifreler Uyuşmuyor!");
            lblMessage.setVisible(true);
            return;
        }

        if((password.length() < 6)) {
            lblMessage.setText("Şifreniz en az 6 karakter olmalıdır!");
            lblMessage.setVisible(true);
            return;
        }
        if(isUserExist(username,email)){
            lblMessage.setText("Bu Mail veya Kullanıcı Adı Sistemde Kayıtlı");
            lblMessage.setVisible(true);
            return;
        }

        boolean success=saveUserToFile(firstName,lastName,username,email,password,gender);

        if(!success){
            return;
        }

        lblMessage.setText("Kayıt Başarılı! Giriş Sayfasına Yönlendiriliyorsunuz...");
        lblMessage.setVisible(true);

        navigateToLogin(new ActionEvent(btnRegister,null));
    }

    private void navigateToLogin(ActionEvent event){
        changeScene(event, "/com.arkadastakibi/login.fxml", "Giriş Yap");
    }

    //Kullanıcıya id üretir
    private int getNextID(){
        return app.Users.get(app.Users.size()-1).getId() + 1;
    }

    //Aynı mail ve kullanıcı adı kontrolü
    private boolean isUserExist(String username, String email){
        for(User user : this.app.Users){
            if(user.getUsername().equals(username) || user.getEmail().equals(email)){
                return true;
            }
        }

        return false;
    }


    //Kullanıcıyı dosyaya ekler
    private boolean saveUserToFile(String fName, String lName, String uName, String mail, String pass, String gender){



           int newID=getNextID();

           User user = new User(newID,fName,lName,uName,gender,pass,mail);
           app.Users.add(user);

           app.update();

            return true;

    }
}
