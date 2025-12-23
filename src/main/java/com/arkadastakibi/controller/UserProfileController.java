package com.arkadastakibi.controller;

import com.arkadastakibi.interfaces.IFormKontrolu;
import com.arkadastakibi.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Optional;

public class UserProfileController extends BaseController implements IFormKontrolu {

    @FXML private Button btnBack;
    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblBio;
    @FXML private Label lblFollowing;
    @FXML private Label lblFollowers;
    @FXML private ImageView imgProfile;

    @FXML private Button btnEditProfile;
    @FXML private Button btnDeleteAccount;
    @FXML private Button btnInstagram;
    @FXML private Button btnTwitter;
    @FXML private Button btnTiktok;

    @FXML private HBox boxProfileView;
    @FXML private VBox boxListView;
    @FXML private Label lblListTitle;
    @FXML private VBox vboxListContent;

    private String currentUsername;
    private User currentUserObj;

    @FXML private VBox boxEditView;
    @FXML private TextField txtEditName;
    @FXML private TextField txtEditSurname;
    @FXML private TextField txtEditUsername;
    @FXML private TextField txtEditEmail;
    @FXML private TextField txtEditBio;
    @FXML private Label lblEditMessage;

    @Override
    public boolean validateForm() {
        return !txtEditName.getText().trim().isEmpty() &&
                !txtEditSurname.getText().trim().isEmpty() &&
                !txtEditUsername.getText().trim().isEmpty() &&
                !txtEditEmail.getText().trim().isEmpty();
    }

    public void setKullaniciAdi(String username) {
        this.currentUsername = username;
        kullaniciVerileriniYukle();
    }

    private void kullaniciVerileriniYukle() {
        if(boxListView != null) boxListView.setVisible(false);
        if(boxProfileView != null) boxProfileView.setVisible(true);

        if (this.app == null || this.app.Users == null) return;

        //App içindeki listeden kullanıcıyı bul
        this.currentUserObj = null;
        for(User u : this.app.Users){
            if(u.getUsername().equals(this.currentUsername)){
                this.currentUserObj = u;
                break;
            }
        }

        if (this.currentUserObj == null) return;

        //Verileri UI'a bas (User sınıfındaki getter'ları kullanarak)
        String fullName = this.currentUserObj.getFirstName() + " " + this.currentUserObj.getLastName();
        lblName.setText(fullName);
        lblEmail.setText("@" + this.currentUserObj.getUsername());

        String bio = this.currentUserObj.getBio();
        lblBio.setText((bio == null || bio.isEmpty()) ? "Henüz bir biyografi eklenmemiş." : bio);

        //Takipçi sayılarını User arraylist boyutundan alıyoruz
        lblFollowers.setText(String.valueOf(this.currentUserObj.getFollowerUser().size()));
        lblFollowing.setText(String.valueOf(this.currentUserObj.getFollowingUser().size()));

        //Sosyal Medya Butonları
        checkSocialLink(this.currentUserObj.getInstagramLink(), btnInstagram);
        checkSocialLink(this.currentUserObj.getTwitterLink(), btnTwitter);
        checkSocialLink(this.currentUserObj.getTiktokLink(), btnTiktok);

        //Avatar Ayarlama
        String gender = this.currentUserObj.getGender();
        String imagePath = (gender != null && gender.equalsIgnoreCase("Kadin"))
                ? "/images/woman.png" : "/images/man.png";
        try {
            if (getClass().getResourceAsStream(imagePath) != null) {
                imgProfile.setImage(new Image(getClass().getResourceAsStream(imagePath)));
            }
        } catch (Exception e) { }
    }

    //String kontrolü (JSONObject yerine doğrudan string alıyor)
    private void checkSocialLink(String link, Button btn) {
        if (link == null || link.isEmpty()) {
            btn.setDisable(true);
            btn.setOpacity(0.5);
        } else {
            btn.setDisable(false);
            btn.setOpacity(1.0);
        }
    }

    @FXML
    public void showFollowersList(MouseEvent event) {
        lblListTitle.setText("Takipçiler");
        loadUsersToList("followers");
        switchView(true);
    }

    @FXML
    public void showFollowingList(MouseEvent event) {
        lblListTitle.setText("Takip Edilenler");
        loadUsersToList("following");
        switchView(true);
    }

    @FXML
    public void closeListView(ActionEvent event) {
        switchView(false);
    }

    private void switchView(boolean showList) {
        boxListView.setVisible(showList);
        boxProfileView.setVisible(!showList);
    }

    //User Modeli ve App Listesini kullanan liste doldurma metodu
    private void loadUsersToList(String type) {
        vboxListContent.getChildren().clear();

        if (this.currentUserObj == null || this.app.Users == null) return;

        //Hedef ID listesini belirle
        ArrayList<Integer> targetIds;
        if (type.equals("followers")) {
            targetIds = this.currentUserObj.getFollowerUser();
        } else {
            targetIds = this.currentUserObj.getFollowingUser();
        }

        //Ana kullanıcı listesini dön ve ID eşleşmesi yap
        for (User u : this.app.Users) {
            //ID listesi bu kullanıcının ID'sini içeriyor mu?
            if (targetIds.contains(u.getId())) {
                vboxListContent.getChildren().add(createUserRow(u));
            }
        }
    }

    //User nesnesi alan satır tasarım metodu
    private HBox createUserRow(User user) {
        String uName = user.getUsername();
        String fullName = user.getFirstName() + " " + user.getLastName();
        String gender = user.getGender();

        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setSpacing(15);
        row.setStyle("-fx-padding: 10; -fx-border-color: #f0f0f0; -fx-border-width: 0 0 1 0; -fx-background-color: white;");

        Circle avatar = new Circle(20);
        String imgPath = (gender != null && gender.equalsIgnoreCase("Kadin"))
                ? "/images/woman.png" : "/images/man.png";
        try {
            avatar.setFill(new ImagePattern(new Image(getClass().getResource(imgPath).toExternalForm())));
        } catch (Exception e) { }

        // İsimler
        VBox nameBox = new VBox(2);
        Label lblName = new Label(fullName);
        lblName.setStyle("-fx-font-weight: bold; -fx-text-fill: #212529;");
        Label lblUser = new Label("@" + uName);
        lblUser.setStyle("-fx-text-fill: #868686; -fx-font-size: 12px;");
        nameBox.getChildren().addAll(lblName, lblUser);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnVisit = new Button("Profili Gör");
        btnVisit.setStyle("-fx-background-color: transparent; -fx-text-fill: #1e88e5; -fx-cursor: hand; -fx-font-weight: bold;");

        btnVisit.setOnAction(e -> {
            //FriendsProfile sayfasına geçiş yap
            FriendsProfileController friendCtrl = changeScene(e, "/com.arkadastakibi/friends-profile.fxml", "Arkadaş Profili");

            //Controller başarıyla yüklendiyse bilgileri gönder
            if (friendCtrl != null) {
                friendCtrl.setArkadasBilgileri(uName, this.currentUsername);
            }
        });

        row.getChildren().addAll(avatar, nameBox, spacer, btnVisit);
        return row;
    }

    @FXML
    void AnaSayfayaGeriDon(ActionEvent event) {
        MainPageController mainCtrl = changeScene(event, "/com.arkadastakibi/main-page.fxml", "Ana Sayfa");

        if (mainCtrl != null && this.app.Users != null) {
            mainCtrl.setKullaniciBilgileri(
                    this.currentUserObj.getFirstName() + " " + this.currentUserObj.getLastName(),
                    this.currentUserObj.getUsername(),
                    this.currentUserObj.getGender()
            );
        }
    }

    @FXML
    public void showEditProfile(ActionEvent event) {
        if(currentUserObj == null) return;

        // Mevcut bilgileri Text Field'lara doldur
        txtEditName.setText(currentUserObj.getFirstName());
        txtEditSurname.setText(currentUserObj.getLastName());
        txtEditUsername.setText(currentUserObj.getUsername());
        txtEditEmail.setText(currentUserObj.getEmail());
        txtEditBio.setText(currentUserObj.getBio());

        lblEditMessage.setVisible(false);

        // Görünümü değiştir
        boxProfileView.setVisible(false);
        boxListView.setVisible(false);
        boxEditView.setVisible(true);
    }

    @FXML
    public void closeEditProfile(ActionEvent event) {
        // İptal edince profile geri dön
        boxEditView.setVisible(false);
        boxProfileView.setVisible(true);
    }

    @FXML
    public void saveProfileChanges(ActionEvent event) {
        String newName = txtEditName.getText().trim();
        String newSurname = txtEditSurname.getText().trim();
        String newUsername = txtEditUsername.getText().trim();
        String newEmail = txtEditEmail.getText().trim();
        String newBio = txtEditBio.getText().trim();

        //boş Alan Kontrolü
        if (!validateForm()) {
            showEditError("Lütfen zorunlu alanları (Ad, Soyad, Kullanıcı Adı, Email) doldurunuz.");
            return;
        }

        //Benzersizlik Kontrolü
        for (User u : this.app.Users) {
            //Kendim hariç diğer kullanıcılara bak
            if (u.getId() != currentUserObj.getId()) {
                if (u.getUsername().equalsIgnoreCase(newUsername)) {
                    showEditError("Bu kullanıcı adı başkası tarafından kullanılıyor!");
                    return;
                }
                if (u.getEmail().equalsIgnoreCase(newEmail)) {
                    showEditError("Bu e-posta adresi başkası tarafından kullanılıyor!");
                    return;
                }
            }
        }

        //Güncelleme İşlemi
        currentUserObj.setFirstName(newName);
        currentUserObj.setLastName(newSurname);
        currentUserObj.setUsername(newUsername);
        currentUserObj.setEmail(newEmail);
        currentUserObj.setBio(newBio);

        //Kayıt ve Arayüz Yenileme
        saveAllData(); //JSON'a yaz

        this.currentUsername = newUsername;

        kullaniciVerileriniYukle(); // Profildeki etiketleri güncelle
        closeEditProfile(null);
        showMessage("Başarılı", "Profil bilgileriniz güncellendi.", Alert.AlertType.INFORMATION);
    }

    private void showEditError(String msg) {
        lblEditMessage.setText(msg);
        lblEditMessage.setVisible(true);
    }

    @FXML
    public void deleteAccount(ActionEvent event) {
        if (currentUserObj == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Hesabı Sil");
        alert.setHeaderText("Hesabınızı silmek üzeresiniz!");
        alert.setContentText("Bu işlem geri alınamaz. Devam etmek istiyor musunuz?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            //Kullanıcıyı listeden sil
            this.app.Users.remove(currentUserObj);

            //Diğer kullanıcıların takipçi/takip edilen listelerinden de bu ID'yi silmek gerekir.
            int deletedUserId = currentUserObj.getId();
            for(User u : this.app.Users) {
                u.getFollowingUser().remove(Integer.valueOf(deletedUserId));
                u.getFollowerUser().remove(Integer.valueOf(deletedUserId));
            }

            //Dosyayı güncelle
            saveAllData();

            //Login ekranına at
            changeScene(event, "/com.arkadastakibi/login.fxml", "Giriş Yap");
        }
    }
}
