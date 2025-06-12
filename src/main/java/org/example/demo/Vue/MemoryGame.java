package org.example.demo.Vue;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.demo.Modele.Card;
import org.example.demo.Controleur.GameManager;
import org.example.demo.Modele.FileManager;
import org.example.demo.Modele.User;
import org.example.demo.Modele.UserDAO;
import org.example.demo.Modele.TimerManager;

public class MemoryGame extends Application {
    private Label statusLabel; // Label pour le statut
    private Label attemptsLabel; // Label pour les essais restants
    private Label timerLabel; // Label pour le temps
    private Label bestTimeLabel; // Label pour les meilleurs temps
    private GridPane grid; // Grille pour les cartes
    private GameManager gameManager; // Gestionnaire du jeu
    private Stage primaryStage; // Scène principale

    @Override
    public void start(Stage primaryStage) {
        // Lance l'application et affiche l'écran de connexion
        this.primaryStage = primaryStage;
        UserDAO.initDB();
        showLoginScreen();
    }

    private void showLoginScreen() {
        // Affiche l'écran de connexion pour entrer le nom
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setStyle(
                "-fx-background-image: url('games-background.png');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-repeat: no-repeat;"
        );

        Label welcomeLabel = new Label("Bienvenue Au Jeu de Paires !");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
        Label promptLabel = new Label("Entrez votre nom :");
        promptLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white");
        TextField nameField = new TextField();
        Button startButton = new Button("Commencer le jeu");
        Button quitButton = new Button("Quitter");

        startButton.setOnAction(e -> {
            String playerName = nameField.getText().trim();
            if (!playerName.isEmpty()) {
                GameManager.currentUser = new User(playerName, 0, 0);
                gameManager = new GameManager(new TimerManager(), new FileManager());
                showGame();
            }
        });

        quitButton.setOnAction(e -> primaryStage.close());

        loginLayout.getChildren().addAll(welcomeLabel, promptLabel, nameField, startButton, quitButton);

        Scene loginScene = new Scene(loginLayout, 600, 600);
        primaryStage.setTitle("Connexion Joueur");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void showGame() {
        // Affiche l'écran de jeu avec la grille et les contrôles
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle(
                "-fx-background-image: url('background_start.png');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-repeat: no-repeat;"
        );

        statusLabel = new Label("Trouvez toutes les paires !");
        statusLabel.setStyle("-fx-text-fill: white;");
        attemptsLabel = new Label("Essais restants : " + gameManager.getCards().length);
        attemptsLabel.setStyle("-fx-text-fill: white;");
        timerLabel = new Label("Temps : 0s");
        timerLabel.setStyle("-fx-text-fill: white;");
        bestTimeLabel = new Label("Meilleur temps : N/A");
        bestTimeLabel.setStyle("-fx-text-fill: white;");

        HBox topBox = new HBox(20, statusLabel, attemptsLabel, timerLabel, bestTimeLabel);
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        root.setCenter(grid);

        gameManager.initializeGame();
        Card[][] cards = gameManager.getCards();
        for (int row = 0; row < cards.length; row++) {
            for (int col = 0; col < cards[row].length; col++) {
                CardView cardView = new CardView(cards[row][col]);
                grid.add(cardView.getButton(), col, row);
            }
        }

        Button restartButton = new Button("Recommencer");
        restartButton.setOnAction(e -> {
            grid.getChildren().clear();
            gameManager.resetGame();
            for (int row = 0; row < cards.length; row++) {
                for (int col = 0; col < cards[row].length; col++) {
                    CardView cardView = new CardView(cards[row][col]);
                    grid.add(cardView.getButton(), col, row);
                }
            }
            gameManager.getFileManager().refreshBestTimes();
        });
        Button quitButton = new Button("Quitter");
        quitButton.setOnAction(e -> primaryStage.close());
        HBox bottomBox = new HBox(10, restartButton, quitButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);

        gameManager.addStatusListener(statusLabel::setText);
        gameManager.addAttemptsListener(attempts -> attemptsLabel.setText("Essais restants : " + attempts));
        gameManager.addWinListener(() -> showMessage("Félicitations ! Vous avez gagné en " + gameManager.getTimerManager().getSecondsElapsed() + " secondes !"));
        gameManager.addLoseListener(() -> showMessage("Désolé, vous avez perdu. Essayez encore !"));
        gameManager.getTimerManager().addTimeListener(time -> timerLabel.setText("Temps : " + time + "s"));
        gameManager.getFileManager().addBestTimesListener(times -> {
            if (times.isEmpty()) {
                bestTimeLabel.setText("Meilleur temps : N/A");
            } else {
                StringBuilder message = new StringBuilder("Meilleurs temps :\n");
                for (int i = 0; i < times.size(); i++) {
                    message.append((i + 1)).append(". ").append(times.get(i)).append("s\n");
                }
                bestTimeLabel.setText(message.toString());
            }
        });

        Scene scene = new Scene(root, 600, 650);
        scene.getStylesheets().add("css/styles.css");
        primaryStage.setTitle("Jeu de Paires");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMessage(String message) {
        // Affiche un message dialog avec une option de fermeture ou de sortie
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.initStyle(StageStyle.UTILITY);

        BorderPane dialogPane = new BorderPane();
        dialogPane.setPadding(new Insets(10));
        Label messageLabel = new Label(message);
        dialogPane.setCenter(messageLabel);

        Button closeButton = new Button("Fermer");
        Button quitButton = new Button("Quitter");
        quitButton.setOnAction(e -> {
            dialog.close();
            primaryStage.close();
        });
        closeButton.setOnAction(e -> dialog.close());
        HBox buttonBox = new HBox(10, closeButton, quitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        dialogPane.setBottom(buttonBox);

        Scene dialogScene = new Scene(dialogPane, 300, 150);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public static void main(String[] args) {
        // Point d'entrée de l'application JavaFX
        launch(args);
    }
}