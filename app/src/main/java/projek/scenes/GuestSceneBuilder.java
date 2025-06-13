package projek.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import projek.App;
import projek.data.DataStore;
import projek.model.Guest;
import projek.model.Kamar;
import projek.model.Reservasi;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class GuestSceneBuilder {
    private static final String COLOR_BACKGROUND = "#F8F3D9";
    private static final String COLOR_MID_TONE_ACCENT = "#B9B28A";
    private static final String COLOR_DARK_PRIMARY = "#504B38";
    private static final String COLOR_CARD_BG = "#F5F0E8";
    private static final String COLOR_ACCENT_GOLD = "#D4AF37";
    
    private static final String TEXT_COLOR_ON_LIGHT_BG = COLOR_DARK_PRIMARY;
    private static final String SUCCESS_TEXT_COLOR = "#7CC269";
    private static final String ERROR_TEXT_COLOR = "#C62828";


    public static Scene createGuestScene(Stage primaryStage, App mainApp, Guest currentGuest) {
        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + COLOR_BACKGROUND + ";");
        rootLayout.setPadding(new Insets(15));

        // Header Section
        VBox headerSection = createHeaderSection(mainApp, currentGuest);
        rootLayout.setTop(headerSection);

        // Main Content - Split into left and right sections
        HBox mainContent = new HBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(15, 0, 0, 0));

        // Left Section - Room Gallery and Available Rooms
        VBox leftSection = createLeftSection();
        
        // Right Section - Booking Form
        VBox rightSection = createRightSection(currentGuest, leftSection);
        mainContent.getChildren().addAll(leftSection, rightSection);
        rootLayout.setCenter(mainContent);

        Scene scene = new Scene(rootLayout, 1000, 700);
        
        return scene;
    }

    private static VBox createHeaderSection(App mainApp, Guest currentGuest) {
        VBox headerSection = new VBox(10);
        headerSection.setPadding(new Insets(0, 0, 15, 0));

        // Title
        Label titleLabel = new Label("Hotel Booking System");
        titleLabel.setStyle(
            "-fx-font-size: 28px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + COLOR_DARK_PRIMARY + ";"
        );

        // Subtitle
        Label subtitleLabel = new Label("Please complete the form to make your reservation");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px; -fx-text-fill: " + COLOR_MID_TONE_ACCENT + ";"
        );

        // User info and logout
        HBox userInfoBox = new HBox(20);
        userInfoBox.setAlignment(Pos.CENTER_RIGHT);
        
        Label welcomeLabel = new Label("Welcome, " + currentGuest.getUsername() + "!");
        welcomeLabel.setStyle(
            "-fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR_ON_LIGHT_BG + ";"
        );

        Button logoutButton = createStyledButton("Logout", COLOR_DARK_PRIMARY);
        logoutButton.setOnAction(e -> mainApp.showLoginScene());

        userInfoBox.getChildren().addAll(welcomeLabel, logoutButton);

        VBox titleBox = new VBox(5, titleLabel, subtitleLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        HBox headerBox = new HBox();
        headerBox.getChildren().addAll(titleBox, userInfoBox);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        headerSection.getChildren().add(headerBox);
        return headerSection;
    }

    private static VBox createLeftSection() {
        VBox leftSection = new VBox(15);
        leftSection.setPrefWidth(350);

        // Room Gallery Section
        VBox gallerySection = new VBox(12);
        Label galleryTitle = new Label("Our Rooms");
        galleryTitle.setStyle(
            "-fx-font-size: 20px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + COLOR_DARK_PRIMARY + ";"
        );

        // Room Cards
        VBox roomCardsContainer = new VBox(12);
        
        // Standard Room Card
        VBox standardCard = createRoomCard(
            "Standard Room", 
            "Rp 500,000 / night",
            "/image/kamarstandar.jpg",
            "Comfortable room with essential amenities"
        );
        
        // Deluxe Room Card
        VBox deluxeCard = createRoomCard(
            "Deluxe Room", 
            "Rp 750,000 / night",
            "/image/kamardeluxe.jpg",
            "Spacious room with premium amenities"
        );

        roomCardsContainer.getChildren().addAll(standardCard, deluxeCard);
        gallerySection.getChildren().addAll(galleryTitle, roomCardsContainer);

        leftSection.getChildren().add(gallerySection);
        return leftSection;
    }

    private static VBox createRoomCard(String roomType, String price, String imagePath, String description) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        card.setStyle(
            "-fx-background-color: " + COLOR_CARD_BG + ";" +
            "-fx-background-radius: 8px;" +
            "-fx-border-color: " + COLOR_MID_TONE_ACCENT + ";" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 8px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);"
        );

        // Room Image - Reduced size significantly
            ImageView roomImage = new ImageView();
        try {
            Image image = new Image(GuestSceneBuilder.class.getResource(imagePath).toExternalForm());
            roomImage.setImage(image);
        } catch (Exception e) {
            roomImage.setImage(createPlaceholderImage());
        }
        
        // Significantly reduced image size
        roomImage.setFitWidth(320);
        roomImage.setFitHeight(140);
        roomImage.setPreserveRatio(false);
        roomImage.setSmooth(true);
        roomImage.setStyle("-fx-background-radius: 6px;");

        // Room Info
        Label typeLabel = new Label(roomType);
        typeLabel.setStyle(
            "-fx-font-size: 16px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + COLOR_DARK_PRIMARY + ";"
        );

        Label priceLabel = new Label(price);
        priceLabel.setStyle(
            "-fx-font-size: 14px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + COLOR_ACCENT_GOLD + ";"
        );

        Label descLabel = new Label(description);
        descLabel.setStyle(
            "-fx-font-size: 12px; -fx-text-fill: " + TEXT_COLOR_ON_LIGHT_BG + ";"
        );
        descLabel.setWrapText(true);

        card.getChildren().addAll(roomImage, typeLabel, priceLabel, descLabel);
        return card;
    }

    private static Image createPlaceholderImage() {
        // Create a simple colored rectangle as placeholder
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==");
    }

    private static VBox createRightSection(Guest currentGuest, VBox leftSection) {
        VBox rightSection = new VBox(15);
        rightSection.setPrefWidth(400);

        // Reservation Information Card
        VBox reservationCard = new VBox(18);
        reservationCard.setPadding(new Insets(20));
        reservationCard.setStyle(
            "-fx-background-color: " + COLOR_DARK_PRIMARY + ";" +
            "-fx-background-radius: 12px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 3);"
        );

        Label cardTitle = new Label("Reservation Information");
        cardTitle.setStyle(
            "-fx-font-size: 20px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + COLOR_ACCENT_GOLD + ";"
        );

        // Form Fields
        GridPane formGrid = new GridPane();
        formGrid.setVgap(12);
        formGrid.setHgap(12);

        // Room Type Selection
        Label roomTypeLabel = new Label("Room type");
        roomTypeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_ACCENT_GOLD + ";");
        ComboBox<Kamar> roomComboBox = new ComboBox<>();
        roomComboBox.setPrefWidth(260);
        roomComboBox.setStyle(
            "-fx-background-color: white; -fx-border-radius: 4px; " +
            "-fx-background-radius: 4px; -fx-font-size: 13px;"
        );
        roomComboBox.setCellFactory(lv -> createKamarListCell());
        roomComboBox.setButtonCell(createKamarListCell());

        // Date Fields
        HBox dateBox = new HBox(12);
        VBox checkInBox = new VBox(4);
        Label checkInLabel = new Label("Check-in date");
        checkInLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_ACCENT_GOLD + ";");
        DatePicker checkInDate = new DatePicker(LocalDate.now());
        checkInDate.setPrefWidth(120);
        checkInDate.setStyle("-fx-background-color: white; -fx-background-radius: 4px;");
        checkInBox.getChildren().addAll(checkInLabel, checkInDate);

        VBox checkOutBox = new VBox(4);
        Label checkOutLabel = new Label("Check-out date");
        checkOutLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_ACCENT_GOLD + ";");
        DatePicker checkOutDate = new DatePicker(LocalDate.now().plusDays(1));
        checkOutDate.setPrefWidth(120);
        checkOutDate.setStyle("-fx-background-color: white; -fx-background-radius: 4px;");
        checkOutBox.getChildren().addAll(checkOutLabel, checkOutDate);

        dateBox.getChildren().addAll(checkInBox, checkOutBox);

        // Guest Information
        HBox guestInfoBox = new HBox(12);
        VBox firstNameBox = new VBox(4);
        Label firstNameLabel = new Label("First name");
        firstNameLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_ACCENT_GOLD + ";");
        TextField firstNameField = new TextField(currentGuest.getUsername());
        firstNameField.setPrefWidth(120);
        firstNameField.setStyle("-fx-background-color: white; -fx-background-radius: 4px;");
        firstNameField.setEditable(true);
        firstNameBox.getChildren().addAll(firstNameLabel, firstNameField);

        VBox lastNameBox = new VBox(4);
        Label lastNameLabel = new Label("Last name");
        lastNameLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_ACCENT_GOLD + ";");
        TextField lastNameField = new TextField();
        lastNameField.setPrefWidth(120);
        lastNameField.setStyle("-fx-background-color: white; -fx-background-radius: 4px;");
        lastNameBox.getChildren().addAll(lastNameLabel, lastNameField);

        guestInfoBox.getChildren().addAll(firstNameBox, lastNameBox);

        // Price Calculation Section
        VBox priceSection = new VBox(8);
        priceSection.setPadding(new Insets(12));
        priceSection.setStyle(
            "-fx-background-color: rgba(255,255,255,0.1); " +
            "-fx-background-radius: 6px;"
        );

        Label priceTitle = new Label("Price Calculation");
        priceTitle.setStyle(
            "-fx-font-size: 14px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + COLOR_ACCENT_GOLD + ";"
        );

        Label nightsLabel = new Label("Number of nights: 1");
        nightsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

        Label pricePerNightLabel = new Label("Price per night: Rp 0");
        pricePerNightLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: " + COLOR_ACCENT_GOLD + ";");

        Label totalPriceLabel = new Label("Total Price: Rp 0");
        totalPriceLabel.setStyle(
            "-fx-font-size: 16px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + COLOR_ACCENT_GOLD + ";"
        );

        priceSection.getChildren().addAll(
            priceTitle, nightsLabel, pricePerNightLabel, separator, totalPriceLabel
        );

        // Request Booking Button
        Button bookingButton = createStyledButton("Request Booking", COLOR_ACCENT_GOLD);
        bookingButton.setPrefWidth(260);
        bookingButton.setPrefHeight(40);

        // Status Label
        Label statusLabel = new Label();
        statusLabel.setWrapText(true);
        statusLabel.setStyle("-fx-font-size: 12px;");

        // Add all components to the card
        VBox formContainer = new VBox(20);
        formContainer.getChildren().addAll(
            new VBox(4, roomTypeLabel, roomComboBox),
            dateBox,
            guestInfoBox,
            priceSection,
            bookingButton,
            statusLabel
        );

        reservationCard.getChildren().addAll(cardTitle, formContainer);
        rightSection.getChildren().add(reservationCard);

        // Price calculation logic
        Runnable updatePriceCalculation = () -> {
            Kamar selectedRoom = roomComboBox.getValue();
            LocalDate checkIn = checkInDate.getValue();
            LocalDate checkOut = checkOutDate.getValue();

            if (selectedRoom != null && checkIn != null && checkOut != null && checkOut.isAfter(checkIn)) {
                long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
                double pricePerNight = selectedRoom.getHarga();
                double totalPrice = pricePerNight * nights;

                nightsLabel.setText("Number of nights: " + nights);
                pricePerNightLabel.setText("Price per night: Rp " + String.format("%,.0f", pricePerNight));
                totalPriceLabel.setText("Total Price: Rp " + String.format("%,.0f", totalPrice));
            } else {
                nightsLabel.setText("Number of nights: 0");
                pricePerNightLabel.setText("Price per night: Rp 0");
                totalPriceLabel.setText("Total Price: Rp 0");
            }
        };

        // Setup data and event handlers
        setupFormLogic(roomComboBox, checkInDate, checkOutDate, bookingButton, statusLabel, 
                      currentGuest, updatePriceCalculation, firstNameField, lastNameField);

        return rightSection;
    }

    private static void setupFormLogic(ComboBox<Kamar> roomComboBox, DatePicker checkInDate, 
                                     DatePicker checkOutDate, Button bookingButton, 
                                     Label statusLabel, Guest currentGuest, 
                                     Runnable updatePriceCalculation, TextField firstNameField, TextField lastNameField) {
        
        ObservableList<Kamar> kamarObservableList = FXCollections.observableArrayList();
        
        Runnable refreshRoomList = () -> {
            List<Kamar> availableRooms = DataStore.getInstance().getAvailableRooms();
            kamarObservableList.setAll(availableRooms);
            roomComboBox.setItems(kamarObservableList);
            if (!roomComboBox.getItems().isEmpty()) {
                if (roomComboBox.getValue() == null || !availableRooms.contains(roomComboBox.getValue())) {
                    roomComboBox.getSelectionModel().selectFirst();
                }
            } else {
                roomComboBox.getSelectionModel().clearSelection();
            }
            updatePriceCalculation.run();
        };

        refreshRoomList.run();

        // Event listeners for price calculation
        roomComboBox.setOnAction(e -> updatePriceCalculation.run());
        checkInDate.setOnAction(e -> updatePriceCalculation.run());
        checkOutDate.setOnAction(e -> updatePriceCalculation.run());

        // Booking button action
        bookingButton.setOnAction(e -> {
            Kamar selectedRoom = roomComboBox.getValue();
            LocalDate checkIn = checkInDate.getValue();
            LocalDate checkOut = checkOutDate.getValue();

            // Validation
            if (selectedRoom == null) {
                statusLabel.setText("Please select a room.");
                statusLabel.setStyle("-fx-text-fill: " + ERROR_TEXT_COLOR + ";");
                return;
            }
            if (checkIn == null || checkOut == null) {
                statusLabel.setText("Please fill in check-in and check-out dates.");
                statusLabel.setStyle("-fx-text-fill: " + ERROR_TEXT_COLOR + ";");
                return;
            }
            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                statusLabel.setText("Check-out date must be after check-in date.");
                statusLabel.setStyle("-fx-text-fill: " + ERROR_TEXT_COLOR + ";");
                return;
            }
            if (!DataStore.getInstance().isRoomAvailable(selectedRoom.getNomor())) {
                statusLabel.setText("Sorry, room " + selectedRoom.getNomor() + " is no longer available.");
                statusLabel.setStyle("-fx-text-fill: " + ERROR_TEXT_COLOR + ";");
                refreshRoomList.run();
                return;
            }
            String combinedFullName = firstNameField.getText().trim() + " " + lastNameField.getText().trim();
            currentGuest.setFullName(combinedFullName.trim());

            // Create reservation
            Reservasi newReservation = new Reservasi(currentGuest, selectedRoom, checkIn, checkOut);

            newReservation.setIdReservasi("R-" + currentGuest.getUsername().substring(0, Math.min(3, currentGuest.getUsername().length())).toUpperCase() + System.currentTimeMillis() % 10000);

            boolean success = DataStore.getInstance().addReservation(newReservation);
            if (success) {
                DataStore.getInstance().updateRoomAvailability(selectedRoom.getNomor(), false);
                long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
                double totalPrice = selectedRoom.getHarga() * nights;
                statusLabel.setText("Reservation successful for room " + selectedRoom.getNomor() + 
                                  "! Total: Rp " + String.format("%,.0f", totalPrice));
                statusLabel.setStyle("-fx-text-fill: " + SUCCESS_TEXT_COLOR + ";");
                refreshRoomList.run();
            } else {
                statusLabel.setText("Failed to create reservation. Please try again.");
                statusLabel.setStyle("-fx-text-fill: " + ERROR_TEXT_COLOR + ";");
            }
        });
    }

    private static Button createStyledButton(String text, String backgroundColor) {
        Button button = new Button(text);
        String defaultStyle = 
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 6px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0, 0, 1);";
        
        String hoverStyle = 
            "-fx-background-color: derive(" + backgroundColor + ", -20%);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 6px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);";

        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));

        return button;
    }

    private static ListCell<Kamar> createKamarListCell() {
        return new ListCell<Kamar>() {
            @Override
            protected void updateItem(Kamar kamar, boolean empty) {
                super.updateItem(kamar, empty);
                if (empty || kamar == null) {
                    setText(null);
                } else {
                    setText(kamar.getNomor() + " (" + kamar.getTipe() + ") - Rp " + 
                           String.format("%,.0f", kamar.getHarga()));
                }
            }
        };
    }

}
