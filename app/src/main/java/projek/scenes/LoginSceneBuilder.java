package projek.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import projek.App;
import projek.data.DataStore; // Pastikan ini diimpor
import projek.model.Admin;
import projek.model.Guest;
import projek.model.User;

public class LoginSceneBuilder {

    private static final String COLOR_BACKGROUND = "#F8F3D9";
    private static final String COLOR_LIGHT_ACCENT = "#EBE5C2";
    private static final String COLOR_MID_TONE = "#B9B28A";
    private static final String COLOR_DARK_PRIMARY = "#504B38";
    private static final String COLOR_SUCCESS = "#7CC269";
    private static final String COLOR_ERROR = "#B85450";

    public static Scene createLoginScene(Stage primaryStage, App mainApp) {
        StackPane root = new StackPane();
        primaryStage.centerOnScreen();
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, " +
            COLOR_BACKGROUND + " 0%, " +
            COLOR_LIGHT_ACCENT + " 50%, " +
            COLOR_MID_TONE + " 100%);"
        );

        // Main card container
        VBox mainCard = new VBox();
        mainCard.setMaxWidth(380);
        mainCard.setMaxHeight(500);
        mainCard.setStyle(
            "-fx-background-color: " + COLOR_DARK_PRIMARY + "; " +
            "-fx-background-radius: 25; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0.3, 0, 5);"
        );

        // Header section
        VBox headerSection = createHeaderSection();

        // Content container (switches between login and signup)
        VBox contentContainer = new VBox();
        contentContainer.setPadding(new Insets(0, 30, 30, 30));

        // Create login and signup forms
        VBox loginForm = createLoginForm(mainApp);
        VBox signupForm = createSignupForm(mainApp);

        // Initially show login form
        contentContainer.getChildren().add(loginForm);

        // Toggle link at bottom
        HBox toggleSection = createToggleSection(contentContainer, loginForm, signupForm);

        mainCard.getChildren().addAll(headerSection, contentContainer, toggleSection);
        root.getChildren().add(mainCard);

        Scene scene = new Scene(root, 800, 600);

        // --- PENAMBAHAN PENTING UNTUK MENYIMPAN DATA SAAT KELUAR ---
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Saving all data before closing application...");
            DataStore.getInstance().saveAllDataToFiles(); // Panggil metode penyimpanan
            System.out.println("Data saved successfully.");

        });

        return scene;
    }

    private static VBox createHeaderSection() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(40, 30, 30, 30));

        Label space = new Label(" ");
        HBox spaceContainer=new HBox();
        spaceContainer.getChildren().add(space);

        // Title
        Label titleLabel = new Label("Welcome to the Hotel!");
        titleLabel.setStyle(
            "-fx-text-fill: white; " +
            "-fx-font-size: 26px; " +
            "-fx-font-weight: bold;"
        );

        header.getChildren().addAll(spaceContainer,titleLabel);
        return header;
    }

    public static VBox createLoginForm(App mainApp) {
        VBox form = new VBox(20);
        form.setAlignment(Pos.CENTER);

        // Username field
        TextField usernameField = createStyledTextField("Username");

        // Password field
        PasswordField passwordField = createStyledPasswordField("Password");


        HBox forgotContainer = new HBox();
        forgotContainer.setAlignment(Pos.CENTER_RIGHT);

        // Error label
        Label errorLabel = new Label();
        errorLabel.setStyle(
            "-fx-text-fill: " + COLOR_ERROR + "; " +
            "-fx-font-size: 12px; " +
            "-fx-font-weight: bold;"
        );
        errorLabel.setVisible(false);

        // Sign in button
        Button signInButton = createPrimaryButton("Sign In");

        // Sign in action
        signInButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showError(errorLabel, "Please fill in all fields.");
                return;
            }

            // Memanggil method authenticateUser() dari DataStore yang sudah disesuaikan
            User user = DataStore.getInstance().authenticateUser(username, password); // Cek nama method

            if (user != null) {
                hideError(errorLabel);
                if (user instanceof Admin) {
                    mainApp.showAdminScene((Admin) user);
                } else if (user instanceof Guest) {
                    mainApp.showGuestScene((Guest) user);
                }
            } else {
                showError(errorLabel, "Invalid username or password.");
            }
        });

        form.getChildren().addAll(
            usernameField,
            passwordField,
            forgotContainer,
            errorLabel,
            signInButton
        );

        return form;
    }

    public static VBox createSignupForm(App mainApp) {
        VBox form = new VBox(20);
        form.setAlignment(Pos.CENTER);

        // Username field
        TextField usernameField = createStyledTextField("Username");

        // Password field
        PasswordField passwordField = createStyledPasswordField("Password");

        // Confirm password field
        PasswordField confirmPasswordField = createStyledPasswordField("Confirm Password");

        // Role selection
        VBox roleSection = new VBox(10);
        Label roleLabel = new Label("Account Type:");
        roleLabel.setStyle(
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold;"
        );

        ToggleGroup roleGroup = new ToggleGroup();

        // Custom styled radio buttons
        HBox roleContainer = new HBox(20);
        roleContainer.setAlignment(Pos.CENTER);

        VBox guestOption = createRoleOption("Guest", roleGroup, true);
        VBox adminOption = createRoleOption("Admin", roleGroup, false);

        roleContainer.getChildren().addAll(guestOption, adminOption);
        roleSection.getChildren().addAll(roleLabel, roleContainer);

        // Error and success labels
        Label errorLabel = new Label();
        errorLabel.setStyle(
            "-fx-text-fill: " + COLOR_ERROR + "; " +
            "-fx-font-size: 11px; " +
            "-fx-font-weight: bold;"
        );
        errorLabel.setVisible(false);

        Label successLabel = new Label();
        successLabel.setStyle(
            "-fx-text-fill: " + COLOR_SUCCESS + "; " +
            "-fx-font-size: 11px; " +
            "-fx-font-weight: bold;"
        );
        successLabel.setVisible(false);

        // Sign up button
        Button signUpButton = createPrimaryButton("Create Account");

        // Sign up action
        signUpButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // Validation
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError(errorLabel, "Please fill in all fields.");
                hideSuccess(successLabel);
                return;
            }

            // Disarankan menggunakan Validator.isValidUsername() atau semacamnya
            if (username.length() < 3) {
                showError(errorLabel, "Username must be at least 3 characters.");
                hideSuccess(successLabel);
                return;
            }
            // Disarankan menggunakan Validator.isValidPassword()
            if (password.length() < 6) {
                showError(errorLabel, "Password must be at least 6 characters.");
                hideSuccess(successLabel);
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError(errorLabel, "Passwords do not match.");
                hideSuccess(successLabel);
                return;
            }

            // Get selected role
            RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
            boolean isAdmin = selectedRole.getText().equals("Admin");

            // Membuat objek User baru (Admin atau Guest)
            User newUser = isAdmin ? new Admin(username, password) : new Guest(username, password);

            // Memanggil method addUser() dari DataStore yang baru
            if (DataStore.getInstance().addUser(newUser)) { // Memanggil addUser()
                hideError(errorLabel);
                showSuccess(successLabel, "Account created successfully!");

                // Clear form
                usernameField.clear();
                passwordField.clear();
                confirmPasswordField.clear();
                // Reset radio button ke Guest
                ((RadioButton) guestOption.getChildren().get(0)).setSelected(true);
            } else {
                // Username sudah ada atau ada masalah lain saat menambahkan
                showError(errorLabel, "Username already exists or failed to create account.");
                hideSuccess(successLabel);
            }
        });

        form.getChildren().addAll(
            usernameField,
            passwordField,
            confirmPasswordField,
            roleSection,
            errorLabel,
            successLabel,
            signUpButton
        );

        return form;
    }


    private static VBox createRoleOption(String text, ToggleGroup group, boolean selected) {
        VBox option = new VBox(5);
        option.setAlignment(Pos.CENTER);

        RadioButton radio = new RadioButton();
        radio.setToggleGroup(group);
        radio.setSelected(selected);
        radio.setText(text);
        radio.setStyle(
            "-fx-text-fill: " + COLOR_LIGHT_ACCENT + "; " +
            "-fx-font-size: 12px;"
        );

        option.getChildren().add(radio);
        return option;
    }

    private static HBox createToggleSection(VBox container, VBox loginForm, VBox signupForm) {
        HBox toggleSection = new HBox();
        toggleSection.setAlignment(Pos.CENTER);
        toggleSection.setPadding(new Insets(0, 0, 30, 0));

        Label toggleLabel = new Label("New to Hotel System? Sign Up");
        toggleLabel.setStyle(
            "-fx-text-fill: " + COLOR_LIGHT_ACCENT + "; " +
            "-fx-font-size: 13px; " +
            "-fx-cursor: hand; " +
            "-fx-underline: true;"
        );

        final boolean[] isLoginMode = {true};

        toggleLabel.setOnMouseClicked(e -> {
            if (isLoginMode[0]) {
                // Switch to signup
                container.getChildren().clear();
                container.getChildren().add(signupForm);
                toggleLabel.setText("Already have an account? Sign In");
                // Update header title
                isLoginMode[0] = false;
            } else {
                // Switch to login
                container.getChildren().clear();
                container.getChildren().add(loginForm);
                toggleLabel.setText("New to Hotel System? Sign Up");
                isLoginMode[0] = true;
            }
        });

        // Hover effect
        toggleLabel.setOnMouseEntered(e -> {
            toggleLabel.setStyle(
                "-fx-text-fill: white; " +
                "-fx-font-size: 13px; " +
                "-fx-cursor: hand; " +
                "-fx-underline: true;"
            );
        });

        toggleLabel.setOnMouseExited(e -> {
            toggleLabel.setStyle(
                "-fx-text-fill: " + COLOR_LIGHT_ACCENT + "; " +
                "-fx-font-size: 13px; " +
                "-fx-cursor: hand; " +
                "-fx-underline: true;"
            );
        });

        toggleSection.getChildren().add(toggleLabel);
        return toggleSection;
    }

    private static TextField createStyledTextField(String placeholder) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefHeight(45);
        field.setStyle(
            "-fx-background-color: " + COLOR_LIGHT_ACCENT + "; " +
            "-fx-background-radius: 25; " +
            "-fx-border-radius: 25; " +
            "-fx-padding: 0 20 0 20; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: " + COLOR_DARK_PRIMARY + "; " +
            "-fx-prompt-text-fill: " + COLOR_MID_TONE + ";"
        );

        // Focus effect
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-background-radius: 25; " +
                    "-fx-border-radius: 25; " +
                    "-fx-border-color: " + COLOR_LIGHT_ACCENT + "; " +
                    "-fx-border-width: 2px; " +
                    "-fx-padding: 0 18 0 18; " +
                    "-fx-font-size: 14px; " +
                    "-fx-text-fill: " + COLOR_DARK_PRIMARY + "; " +
                    "-fx-prompt-text-fill: " + COLOR_MID_TONE + ";"
                );
            } else {
                field.setStyle(
                    "-fx-background-color: " + COLOR_LIGHT_ACCENT + "; " +
                    "-fx-background-radius: 25; " +
                    "-fx-border-radius: 25; " +
                    "-fx-padding: 0 20 0 20; " +
                    "-fx-font-size: 14px; " +
                    "-fx-text-fill: " + COLOR_DARK_PRIMARY + "; " +
                    "-fx-prompt-text-fill: " + COLOR_MID_TONE + ";"
                );
            }
        });

        return field;
    }

    private static PasswordField createStyledPasswordField(String placeholder) {
        PasswordField field = new PasswordField();
        field.setPromptText(placeholder);
        field.setPrefHeight(45);
        field.setStyle(
            "-fx-background-color: " + COLOR_LIGHT_ACCENT + "; " +
            "-fx-background-radius: 25; " +
            "-fx-border-radius: 25; " +
            "-fx-padding: 0 20 0 20; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: " + COLOR_DARK_PRIMARY + "; " +
            "-fx-prompt-text-fill: " + COLOR_MID_TONE + ";"
        );

        // Focus effect
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-background-radius: 25; " +
                    "-fx-border-radius: 25; " +
                    "-fx-border-color: " + COLOR_LIGHT_ACCENT + "; " +
                    "-fx-border-width: 2px; " +
                    "-fx-padding: 0 18 0 18; " +
                    "-fx-font-size: 14px; " +
                    "-fx-text-fill: " + COLOR_DARK_PRIMARY + "; " +
                    "-fx-prompt-text-fill: " + COLOR_MID_TONE + ";"
                );
            } else {
                field.setStyle(
                    "-fx-background-color: " + COLOR_LIGHT_ACCENT + "; " +
                    "-fx-background-radius: 25; " +
                    "-fx-border-radius: 25; " +
                    "-fx-padding: 0 20 0 20; " +
                    "-fx-font-size: 14px; " +
                    "-fx-text-fill: " + COLOR_DARK_PRIMARY + "; " +
                    "-fx-prompt-text-fill: " + COLOR_MID_TONE + ";"
                );
            }
        });

        return field;
    }

    private static Button createPrimaryButton(String text) {

        Button button = new Button(text);
        button.setPrefSize(280, 45);

        // --- Warna (bisa ambil dari konstanta yang sudah kamu punya) ---
        String c1 = "#B9B28A"; //mid tone
        String c2 = "#8B7E56";  //brown olive
        String c3 = "#EBE5C2";  // light accent

        String base = """
            -fx-background-color: linear-gradient(from 0%% 0%% to 0%% 100%%, %s 0%%, %s 100%%);
            -fx-background-radius: 25;
            -fx-background-insets: 0;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-cursor: hand;
            """.formatted(c3, c1);

        String hover = """
            -fx-background-color: linear-gradient(from 0%% 0%% to 0%% 100%%, %s 0%%, %s 100%%);
            -fx-background-radius: 25;
            -fx-background-insets: 0;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-cursor: hand;
            """.formatted(c1, c2);

        button.setStyle(base);

        // ── Efek hover (animasi scale halus)
        ScaleTransition in  = new ScaleTransition(Duration.millis(150), button);
        in.setToX(1.05); in.setToY(1.05);

        ScaleTransition out = new ScaleTransition(Duration.millis(150), button);
        out.setToX(1.0);  out.setToY(1.0);

        button.setOnMouseEntered(e -> { button.setStyle(hover); in.playFromStart(); });
        button.setOnMouseExited (e -> { button.setStyle(base);  out.playFromStart(); });

        return button;
    }


    private static void showError(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private static void hideError(Label errorLabel) {
        errorLabel.setVisible(false);
        errorLabel.setText(""); // Clear text when hidden
    }

    private static void showSuccess(Label successLabel, String message) {
        successLabel.setText(message);
        successLabel.setVisible(true);
    }

    private static void hideSuccess(Label successLabel) {
        successLabel.setVisible(false);
        successLabel.setText(""); // Clear text when hidden
    }
}