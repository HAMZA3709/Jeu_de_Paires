package org.example.demo.Vue;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.demo.Modele.Card;

public class CardView {
    private static final String BACK_IMAGE_PATH = "/back.png"; // Chemin de l'image de dos
    private Button button; // Bouton représentant la carte
    private Card card; // Carte associée

    public CardView(Card card) {
        // Initialise la vue de la carte avec une carte modèle
        this.card = card;
        this.button = new Button();
        this.button.setPrefSize(100, 100);
        this.button.setStyle("-fx-background-color: #808080;");
        this.button.setOnAction(e -> card.onClick());
        card.addStateListener(this::updateView);
        updateView(card);
    }

    public Button getButton() {
        // Retourne le bouton de la carte
        return button;
    }

    private void updateView(Card card) {
        // Met à jour l'affichage de la carte selon son état
        if (card.isVisible() || card.isMatched()) {
            System.out.println("Loading image from: " + card.getImagePath());
            try {
                Image image = new Image(card.getImagePath(), 85, 85, true, true);
                if (image.isError()) {
                    System.out.println("Error loading image: " + image.getException());
                }
                ImageView imageView = new ImageView(image);
                button.setGraphic(imageView);
                button.setStyle("-fx-background-color: #F0F0F0;");
            } catch (Exception e) {
                System.out.println("Failed to load image " + card.getImagePath() + ": " + e.getMessage());
                button.setGraphic(null);
            }
        } else {
            System.out.println("Loading back image from: " + BACK_IMAGE_PATH);
            try {
                Image backImage = new Image(BACK_IMAGE_PATH, 85, 85, true, true);
                if (backImage.isError()) {
                    System.out.println("Error loading back image: " + backImage.getException());
                }
                ImageView backImageView = new ImageView(backImage);
                button.setGraphic(backImageView);
                button.setStyle("-fx-background-color: #F0F0F0;");
            } catch (Exception e) {
                System.out.println("Failed to load back image " + BACK_IMAGE_PATH + ": " + e.getMessage());
                button.setGraphic(null);
            }
        }
    }
}