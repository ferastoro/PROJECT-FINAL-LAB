package projek.scenes;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import projek.App;
import projek.data.DataStore;
import projek.model.Admin;
import projek.model.Reservasi;

public class AdminSceneBuilder {

    // Color palette to match login and guest scenes
    private static final String COLOR_BACKGROUND = "#F8F3D9";
    private static final String COLOR_CARD_BG = "#504B38";
    private static final String COLOR_ACCENT = "#D4AF37";
    private static final String COLOR_SECONDARY = "#8B7355";
    private static final String COLOR_INPUT_BG = "#FFFFFF";
    private static final String COLOR_TEXT_LIGHT = "#FFFFFF";
    private static final String COLOR_TEXT_DARK = "#504B38";

    public static Scene createAdminScene(Stage primaryStage, App mainApp, Admin currentAdmin) {
        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + COLOR_BACKGROUND + ";");
        rootLayout.setPadding(new Insets(40));

        // --- Header Section ---
        VBox headerSection = createHeaderSection(currentAdmin, mainApp);
        rootLayout.setTop(headerSection);

        // --- Main Content Card ---
        VBox mainCard = createMainContentCard();
        
        // Filter section
        HBox filterSection = createFilterSection();
        
        // Table section
        TableView<Reservasi> reservasiTable = createReservationTable();
        
        // Button section
        HBox buttonSection = createButtonSection();
        
        mainCard.getChildren().addAll(filterSection, reservasiTable, buttonSection);
        rootLayout.setCenter(mainCard);

        // --- Data Logic ---
        setupDataLogic(reservasiTable, filterSection, buttonSection);

        Scene scene = new Scene(rootLayout, 1000, 700);
        return scene;
    }

    private static VBox createHeaderSection(Admin currentAdmin, App mainApp) {
        // Main title
        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setStyle(
            "-fx-font-size: 36px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + COLOR_TEXT_DARK + ";"
        );

        // Subtitle
        Label subtitleLabel = new Label("Manage hotel reservations and monitor guest bookings");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: " + COLOR_SECONDARY + ";"
        );

        // Welcome and logout section
        Label welcomeLabel = new Label("Welcome, " + currentAdmin.getUsername() + "!");
        welcomeLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + COLOR_TEXT_DARK + ";"
        );

        Button logoutButton = new Button("Logout");
        styleButton(logoutButton, true);
        logoutButton.setOnAction(e -> mainApp.showLoginScene());

        HBox topRightSection = new HBox(20, welcomeLabel, logoutButton);
        topRightSection.setAlignment(Pos.CENTER_RIGHT);

        VBox headerSection = new VBox(10);
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        headerSection.setPadding(new Insets(0, 0, 30, 0));

        // Create a container for title and top-right section
        BorderPane headerContainer = new BorderPane();
        headerContainer.setLeft(headerSection);
        headerContainer.setRight(topRightSection);

        VBox fullHeader = new VBox();
        fullHeader.getChildren().add(headerContainer);
        
        return fullHeader;
    }

    private static VBox createMainContentCard() {
        VBox mainCard = new VBox(25);
        mainCard.setPadding(new Insets(30));
        mainCard.setStyle(
            "-fx-background-color: " + COLOR_CARD_BG + ";" +
            "-fx-background-radius: 15px;"
        );

        // Add drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(5.0);
        dropShadow.setColor(Color.color(0, 0, 0, 0.3));
        mainCard.setEffect(dropShadow);

        return mainCard;
    }

    private static HBox createFilterSection() {
        Label filterTitle = new Label("Reservation Management");
        filterTitle.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + COLOR_ACCENT + ";"
        );

        Label filterLabel = new Label("Search reservations:");
        filterLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + COLOR_TEXT_LIGHT + ";"
        );

        TextField filterField = new TextField();
        filterField.setPromptText("Search by ID, Guest Name, or Room Number...");
        filterField.setPrefWidth(400);
        filterField.setPrefHeight(40);
        filterField.setStyle(
            "-fx-background-color: " + COLOR_INPUT_BG + ";" +
            "-fx-background-radius: 20px;" +
            "-fx-border-radius: 20px;" +
            "-fx-padding: 0 15 0 15;" +
            "-fx-font-size: 14px;"
        );

        VBox filterLeft = new VBox(8, filterTitle, filterLabel);
        
        HBox filterSection = new HBox();
        filterSection.setAlignment(Pos.CENTER_LEFT);
        filterSection.getChildren().addAll(filterLeft, createSpacer(), filterField);
        
        return filterSection;
    }

    private static TableView<Reservasi> createReservationTable() {
        TableView<Reservasi> table = new TableView<>();
        table.setPrefHeight(400);
        
        // Style the table to match the design
        table.setStyle(
            "-fx-background-color: " + COLOR_INPUT_BG + ";" +
            "-fx-background-radius: 10px;" +
            "-fx-border-radius: 10px;"
        );

        // Create columns
        TableColumn<Reservasi, String> idCol = new TableColumn<>("Reservation ID");
        idCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getIdReservasi()));
        idCol.setPrefWidth(150);

        TableColumn<Reservasi, String> guestCol = new TableColumn<>("Guest Name");
        guestCol.setCellValueFactory(cellData ->
            cellData.getValue().getTamu() != null ?
            new SimpleStringProperty(cellData.getValue().getTamu().getFullName()) :
            new SimpleStringProperty("N/A"));
        guestCol.setPrefWidth(150);

        TableColumn<Reservasi, String> roomNumCol = new TableColumn<>("Room No.");
        roomNumCol.setCellValueFactory(cellData ->
            cellData.getValue().getKamar() != null ?
            new SimpleStringProperty(cellData.getValue().getKamar().getNomor()) :
            new SimpleStringProperty("N/A"));
        roomNumCol.setPrefWidth(100);

        TableColumn<Reservasi, String> roomTypeCol = new TableColumn<>("Room Type");
        roomTypeCol.setCellValueFactory(cellData ->
            cellData.getValue().getKamar() != null ?
            new SimpleStringProperty(cellData.getValue().getKamar().getTipe()) :
            new SimpleStringProperty("N/A"));
        roomTypeCol.setPrefWidth(120);

        TableColumn<Reservasi, String> checkInCol = new TableColumn<>("Check-In");
        checkInCol.setCellValueFactory(cellData ->
            cellData.getValue().getCheckIn() != null ?
            new SimpleStringProperty(cellData.getValue().getCheckIn().toString()) :
            new SimpleStringProperty("N/A"));
        checkInCol.setPrefWidth(120);

        TableColumn<Reservasi, String> checkOutCol = new TableColumn<>("Check-Out");
        checkOutCol.setCellValueFactory(cellData ->
            cellData.getValue().getCheckOut() != null ?
            new SimpleStringProperty(cellData.getValue().getCheckOut().toString()) :
            new SimpleStringProperty("N/A"));
        checkOutCol.setPrefWidth(120);

        table.getColumns().addAll(idCol, guestCol, roomNumCol, roomTypeCol, checkInCol, checkOutCol);

        // Style table headers and cells
        table.setRowFactory(tv -> {
            TableRow<Reservasi> row = new TableRow<>();
            row.setStyle("-fx-background-color: transparent;");
            return row;
        });

        return table;
    }

    private static HBox createButtonSection() {
        Button refreshButton = new Button("Refresh Data");
        styleButton(refreshButton, false);
        
        Button deleteButton = new Button("Delete Data");
        styleDeleteButton(deleteButton);

        HBox buttonSection = new HBox(15, refreshButton, deleteButton);
        buttonSection.setAlignment(Pos.CENTER_RIGHT);
        
        return buttonSection;
    }

    private static void styleButton(Button button, boolean isSecondary) {
        String backgroundColor = isSecondary ? COLOR_SECONDARY : COLOR_ACCENT;
        String hoverColor = isSecondary ? "#6B5B47" : "#B8941F";
        
        button.setPrefHeight(40);
        button.setPrefWidth(140);
        button.setStyle(
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-text-fill: " + COLOR_TEXT_LIGHT + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 20px;" +
            "-fx-border-radius: 20px;"
        );

        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: " + hoverColor + ";" +
            "-fx-text-fill: " + COLOR_TEXT_LIGHT + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 20px;" +
            "-fx-border-radius: 20px;" +
            "-fx-cursor: hand;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-text-fill: " + COLOR_TEXT_LIGHT + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 20px;" +
            "-fx-border-radius: 20px;"
        ));
    }

    private static void styleDeleteButton(Button button) {
        String backgroundColor = "#DC3545"; // Red color for delete
        String hoverColor = "#C82333";
        
        button.setPrefHeight(40);
        button.setPrefWidth(140);
        button.setStyle(
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-text-fill: " + COLOR_TEXT_LIGHT + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 20px;" +
            "-fx-border-radius: 20px;"
        );

        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: " + hoverColor + ";" +
            "-fx-text-fill: " + COLOR_TEXT_LIGHT + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 20px;" +
            "-fx-border-radius: 20px;" +
            "-fx-cursor: hand;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-text-fill: " + COLOR_TEXT_LIGHT + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 20px;" +
            "-fx-border-radius: 20px;"
        ));
    }

    private static Region createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        return spacer;
    }

    private static void setupDataLogic(TableView<Reservasi> table, HBox filterSection, HBox buttonSection) {
        // Get filter field from filterSection
        TextField filterField = (TextField) filterSection.getChildren().get(2);
        
        // Get buttons from buttonSection
        Button refreshButton = (Button) buttonSection.getChildren().get(0);
        Button deleteButton = (Button) buttonSection.getChildren().get(1);

        ObservableList<Reservasi> masterReservasiList = FXCollections.observableArrayList();
        
        Runnable loadAndRefreshTable = () -> {
            masterReservasiList.setAll(DataStore.getInstance().getAllReservations());
        };
        
        // Initialize data
        loadAndRefreshTable.run();

        // Setup filtering
        FilteredList<Reservasi> filteredData = new FilteredList<>(masterReservasiList, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(reservasi -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                boolean idMatch = reservasi.getIdReservasi() != null && 
                    reservasi.getIdReservasi().toLowerCase().contains(lowerCaseFilter);
                boolean tamuMatch = reservasi.getTamu() != null && 
                    reservasi.getTamu().getFullName().toLowerCase().contains(lowerCaseFilter);
                boolean kamarMatch = reservasi.getKamar() != null && 
                    reservasi.getKamar().getNomor().toLowerCase().contains(lowerCaseFilter);
                
                return idMatch || tamuMatch || kamarMatch;
            });
        });

        SortedList<Reservasi> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        // Button actions
        refreshButton.setOnAction(e -> {
            loadAndRefreshTable.run();
            filterField.clear();
            System.out.println("Reservation data refreshed.");
        });

        deleteButton.setOnAction(e -> {
            Reservasi selectedReservasi = table.getSelectionModel().getSelectedItem();
            
            if (selectedReservasi == null) {
                // No data selected
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setTitle("No Selection");
                warningAlert.setHeaderText("No Reservation Selected");
                warningAlert.setContentText("Please select a reservation from the table to delete.");
                warningAlert.showAndWait();
                return;
            }
            
            // Confirmation before delete
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Delete Confirmation");
            confirmAlert.setHeaderText("Delete Reservation");
            confirmAlert.setContentText(
                "Are you sure you want to delete this reservation?\n\n" +
                "Reservation ID: " + selectedReservasi.getIdReservasi() + "\n" +
                "Guest: " + (selectedReservasi.getTamu() != null ? 
                            selectedReservasi.getTamu().getFullName() : "N/A") + "\n" +
                "Room: " + (selectedReservasi.getKamar() != null ? 
                           selectedReservasi.getKamar().getNomor() : "N/A") + "\n\n" +
                "This action cannot be undone!"
            );
            
            ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmAlert.getButtonTypes().setAll(deleteButtonType, cancelButtonType);
            
            // Style the delete button in alert to be red
            confirmAlert.getDialogPane().lookupButton(deleteButtonType).setStyle(
                "-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-weight: bold;"
            );
            
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == deleteButtonType) {
                    try {
                        // Delete from DataStore
                        boolean deleteSuccess = DataStore.getInstance().deleteReservation(selectedReservasi.getIdReservasi());
                        
                        if (deleteSuccess) {
                            // Refresh table after successful deletion
                            loadAndRefreshTable.run();
                            
                            // Show success message
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Delete Successful");
                            successAlert.setHeaderText("Reservation Deleted");
                            successAlert.setContentText("The reservation has been successfully deleted from the system.");
                            successAlert.showAndWait();
                            
                            System.out.println("Reservation deleted: " + selectedReservasi.getIdReservasi());
                        } else {
                            // Show error message if delete failed
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Delete Failed");
                            errorAlert.setHeaderText("Unable to Delete Reservation");
                            errorAlert.setContentText("An error occurred while trying to delete the reservation. Please try again.");
                            errorAlert.showAndWait();
                        }
                    } catch (Exception ex) {
                        // Handle exception
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Delete Error");
                        errorAlert.setHeaderText("System Error");
                        errorAlert.setContentText("An unexpected error occurred: " + ex.getMessage());
                        errorAlert.showAndWait();
                        
                        ex.printStackTrace();
                    }
                }
            });
        });
    }
}