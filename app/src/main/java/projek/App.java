package projek; 
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projek.data.*;
import projek.model.*;

import projek.scenes.*;


public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        showLoginScene();
        primaryStage.centerOnScreen();

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Saving all data before closing application...");
            DataStore.getInstance().saveAllDataToFiles(); 
            System.out.println("Data saved successfully.");
        });  
    }

    // Tampilkan Login Scene 
    public void showLoginScene() {
        Scene loginScene = LoginSceneBuilder.createLoginScene(primaryStage, this);
        
        primaryStage.setTitle("Aplikasi Manajemen Hotel - Login");
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false); 
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    // Tampilkan Guest Scene 
    public void showGuestScene(Guest guest) {
        Scene guestScene = GuestSceneBuilder.createGuestScene(primaryStage, this, guest);
        primaryStage.setTitle("Guest Dashboard - " + guest.getUsername());
        primaryStage.setScene(guestScene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    // Tampilkan Admin Scene
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