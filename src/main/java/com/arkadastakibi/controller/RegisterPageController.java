package com.arkadastakibi.controller;

import com.arkadastakibi.interfaces.IFormKontrolu;
import com.arkadastakibi.model.DataBase;
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

        if(!password.equals(confirmPassword)) {
            lblMessage.setText("Şifreler Uyuşmuyor!");
            lblMessage.setVisible(true);
            return;
        }


        //Veriyi json dosyasına ekler
        boolean success=saveUserToFile(firstName,lastName,username,email,password,gender);

        if(!success){
            return;
        }

        lblMessage.setText("Kayıt Başarılı! Giriş Sayfasına Yönlendiriliyorsunuz...");
        lblMessage.setVisible(true);

        navigateToLogin(new ActionEvent(btnRegister,null));
    }

    public JSONObject createUserDataJson(int id,String fName, String lName, String uName, String mail,String gender, String pass){
        JSONObject data=new JSONObject();
        data.put("id",id);
        data.put("firstName",fName);
        data.put("lastName",lName);
        data.put("username",uName);
        data.put("email",mail);
        data.put("password",pass);
        data.put("gender",gender);

        data.put("bio",JSONObject.NULL);
        data.put("instagram",JSONObject.NULL);
        data.put("twitter",JSONObject.NULL);
        data.put("tiktok",JSONObject.NULL);

        data.put("followingUser",new JSONArray());
        data.put("followerUser",new JSONArray());

        return data;
    }

    private void navigateToLogin(ActionEvent event){
        changeScene(event, "/com.arkadastakibi/login.fxml", "Giriş Yap");
    }

    //Kullanıcıya id üretir
    private int getNextID(JSONArray usersArray){
        int maxID=0;
        for(int i=0;i<usersArray.length();i++){
            JSONObject user=usersArray.getJSONObject(i);

            if(user.has("id")){
                int currentID=user.getInt("id");
                if(currentID>maxID){
                    maxID=currentID;
                }
            }
        }

        return maxID +1;
    }

    //Aynı mail ve kullanıcı adı kontrolü
    private boolean isUserExist(JSONArray usersArray,String username,String email){
        for(int i=0;i<usersArray.length();i++){
            JSONObject user=usersArray.getJSONObject(i);

            String tempUsername=user.getString("username");
            String tempMail=user.getString("email");

            if(tempUsername.equals(username) || tempMail.equals(email)){
                return true;
            }
        }

        return false;
    }


    //Kullanıcıyı dosyaya ekler
    private boolean saveUserToFile(String fName, String lName, String uName, String mail, String pass, String gender){
        try{
           File file=new File("users.json");

           JSONArray usersArray;

           if(file.exists()){
               String content = new String(Files.readAllBytes(file.toPath()));
               usersArray = content.isEmpty() ? new JSONArray() : new JSONArray(content);
           }
           else{
               usersArray = new JSONArray();
           }

           if(isUserExist(usersArray,uName,mail)){
               lblMessage.setText("Bu Mail veya Kullanıcı Adı Sistemde Kayıtlı");
               lblMessage.setVisible(true);
               return false;
           }

           int newID=getNextID(usersArray);
           JSONObject user=createUserDataJson(newID,fName,lName,uName,mail,gender,pass);

           usersArray.put(user);

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(usersArray.toString(4));
            }

            return true;


        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
