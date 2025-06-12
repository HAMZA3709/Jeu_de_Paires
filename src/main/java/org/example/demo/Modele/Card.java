package org.example.demo.Modele;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Card extends GameElement {
    private String imagePath; // Chemin de l'image de la carte
    private boolean isMatched; // Indicateur si la carte est appariée
    private Runnable onClickHandler; // Gestionnaire d'événements pour le clic
    private List<Consumer<Card>> stateListeners = new ArrayList<>(); // Listeners pour les changements d'état

    public Card(String imagePath) {
        // Initialise la carte avec un chemin d'image
        this.imagePath = imagePath;
        this.isMatched = false;
    }

    public void addStateListener(Consumer<Card> listener) {
        // Ajoute un listener pour les changements d'état et notifie immédiatement
        stateListeners.add(listener);
        listener.accept(this);
    }

    public void setOnClickHandler(Runnable onClickHandler) {
        // Définit le gestionnaire d'événements pour le clic
        this.onClickHandler = onClickHandler;
    }

    public void onClick() {
        // Gère le clic sur la carte et exécute le gestionnaire si présent
        if (onClickHandler != null) {
            System.out.println("Card clicked: " + imagePath);
            onClickHandler.run();
        }
    }

    public String getImagePath() {
        // Retourne le chemin de l'image
        return imagePath;
    }

    public boolean isMatched() {
        // Vérifie si la carte est appariée
        return isMatched;
    }

    public void setMatched(boolean matched) {
        // Définit l'état d'appariement et notifie les listeners
        this.isMatched = matched;
        notifyStateListeners();
    }

    @Override
    public void setVisible(boolean visible) {
        // Définit la visibilité et notifie les listeners
        super.setVisible(visible);
        notifyStateListeners();
    }

    private void notifyStateListeners() {
        // Notifie tous les listeners des changements d'état
        stateListeners.forEach(listener -> listener.accept(this));
    }
}