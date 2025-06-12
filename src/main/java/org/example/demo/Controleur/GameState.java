package org.example.demo.Controleur;

public interface GameState {
    void onWin(); // Gère la logique lorsqu'une victoire est détectée
    void onLose(); // Gère la logique lorsqu'une défaite est détectée
    void updateAttempts(int attempts); // Met à jour le nombre d'essais restants
    void updatePairsFound(int pairs); // Met à jour le nombre de paires trouvées
}