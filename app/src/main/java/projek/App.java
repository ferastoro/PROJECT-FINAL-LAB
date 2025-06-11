package projek; // Sesuaikan dengan nama paket Anda

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projek.data.*;
import projek.model.*;
// Pastikan Anda mengimpor LoginSceneBuilder jika berada di paket yang berbeda
// Misalnya: import projek.view.LoginSceneBuilder;
// Juga import untuk Admin dan Guest jika diperlukan untuk parameter (meskipun tidak untuk showLoginScene)
// import projek.model.Admin;
// import projek.model.Guest;
// import projek.view.GuestSceneBuilder; // Jika sudah dibuat
// import projek.view.AdminSceneBuilder; // Jika sudah dibuat
import projek.scenes.*;


public class App extends Application {

    private Stage primaryStage; // Stage utama aplikasi

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // Saat aplikasi pertama kali dimulai, tampilkan scene login
        showLoginScene();
        primaryStage.centerOnScreen();

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Saving all data before closing application...");
            DataStore.getInstance().saveAllDataToFiles(); // Panggil metode penyimpanan
            System.out.println("Data saved successfully.");
        });

        
    }

    public void showLoginScene() {
        Scene loginScene = LoginSceneBuilder.createLoginScene(primaryStage, this);
        
        primaryStage.setTitle("Aplikasi Manajemen Hotel - Login");
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false); 
        primaryStage.centerOnScreen();
        primaryStage.show(); // Tampilkan stage (jika belum terlihat) atau update scene
    }

    // Metode untuk menampilkan Guest Scene (contoh dari sebelumnya)
    public void showGuestScene(Guest guest) {
        Scene guestScene = GuestSceneBuilder.createGuestScene(primaryStage, this, guest);
        primaryStage.setTitle("Guest Dashboard - " + guest.getUsername());
        primaryStage.setScene(guestScene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    // Metode untuk menampilkan Admin Scene (contoh dari sebelumnya)
    public void showAdminScene(Admin admin) {
        Scene adminScene = AdminSceneBuilder.createAdminScene(primaryStage, this, admin);
        primaryStage.setTitle("Admin Dashboard - " + admin.getUsername());
        primaryStage.setScene(adminScene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}