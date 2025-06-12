package org.example.demo.Modele;

public abstract class GameElement {
    private boolean isVisible; // Indicateur de visibilité

    public GameElement() {
        // Initialise l'élément avec une visibilité par défaut
        this.isVisible = false;
    }

    public boolean isVisible() {
        // Vérifie si l'élément est visible
        return isVisible;
    }

    public void setVisible(boolean visible) {
        // Définit la visibilité de l'élément
        this.isVisible = visible;
    }
}