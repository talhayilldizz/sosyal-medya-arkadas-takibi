# Arkadas Takibi Uygulaması

Bu doküman, proje üzerinde çalışan geliştiricilerin (bizim grubun) dosya yapısını, kod standartlarını ve kurulumu anlaması için hazırlanmıştır. Lütfen kod yazmaya başlamadan önce burayı okuyun.

---

## Projeyi Ayağa Kaldırma

Projeyi `git clone` ile indirdikten sonra **ilk yapmanız gerekenler**:

1.  **Maven'ı Yükle:** IntelliJ IDEA'da sağ taraftaki **Maven** sekmesine gidin ve sol üstteki **"Reload All Maven Projects"** (dönen ok) butonuna basın.
    * *Neden?* JavaFX ve GSON kütüphaneleri inmezse kodlar kırmızı yanar.
2.  **Çalıştır:** Sağ taraftaki maven logosuna basıp, united menüsünü açıp, javafx e basın ve javafx:run kısmına çift tıklayın

---

## Proje Haritası: Nereye Kod Yazacaksın?

### 1. Tasarım 
Görsel tasarımlar, butonlar, CSS dosyaları burada bulunur.
* **Konum:** `src/main/resources/com/arkadastakibi/`
* **Ne Yapılır:**
    * Yeni bir ekran tasarlayacaksan `.fxml` dosyasını buraya oluştur.
    * SceneBuilder ile açıp tasarımı yap.
    * CSS dosyalarını `styles/` klasörüne at.

### 2. Kod Yazacaklar İçin (Controller & Logic)
Tasarım dışındaki tüm mantıksal işlemler, buton tıklamaları burada bulunur.
* **Konum:** `src/main/java/com/arkadastakibi/`
* **Alt Klasörler:**
    * `controller/` **En çok burayı kullanacağız.** FXML dosyasındaki butonlara basılınca ne olacağını buradaki sınıflara yazarız. (Örn: `LoginController.java`)
    * `model/` Veri tipleri buradadır. (Örn: `User`, `Message` sınıfları).
    * `dao/` Veritabanı (JSON) okuma/yazma kodları buradadır.

---

##  Yeni Bir Ekran Eklerken İzlenecek Yol

Yeni bir sayfa (Örn: Ayarlar Sayfası) ekliyorsan şu sırayı takip et:

1.  **FXML Oluştur:** `resources/.../Settings.fxml` dosyasını oluştur ve tasarla.
2.  **Controller Oluştur:** `java/.../controller/SettingsController.java` sınıfını oluştur.
3.  **Bağlantıyı Kur:** FXML dosyasını SceneBuilder'da aç, sol alttaki **Controller** kısmına `com.arkadastakibi.controller.SettingsController` yaz.
4.  **Elemanları Tanımla:** Butonlara ve Text alanlarına `fx:id` vererek Controller içinde tanımla.

---

## Sık Karşılaşılan Sorunlar

| Sorun | Çözüm |
| :--- | :--- |
| Kodların altı kırmızı çizili | Sağdaki **Maven** panelinden **Reload** butonuna bas. |
| "Resource not found" hatası | FXML dosyasının ismini veya yolunu kodda yanlış yazdın. Kontrol et. |
| Proje açılmıyor | `pom.xml` içindeki Java sürümü ile bilgisayarındaki JDK sürümü aynı mı? (Java 17) |

--
