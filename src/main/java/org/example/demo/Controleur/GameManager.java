package org.example.demo.Controleur;

import org.example.demo.Modele.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class GameManager implements GameState {
    private static final int GRID_SIZE = 4; // Taille fixe de la grille (4x4)
    private static final int TOTAL_PAIRS = (GRID_SIZE * GRID_SIZE) / 2; // Nombre total de paires
    private static final int MAX_ATTEMPTS = 3; // Nombre maximal d'essais
    private Card[][] cards = new Card[GRID_SIZE][GRID_SIZE]; // Grille de cartes
    private Card firstCard = null; // Première carte sélectionnée
    private Card secondCard = null; // Deuxième carte sélectionnée
    private int pairsFound = 0; // Nombre de paires trouvées
    private int attemptsLeft = MAX_ATTEMPTS; // Essais restants
    private boolean isProcessing = false; // Indicateur de traitement en cours
    private TimerManager timerManager; // Gestionnaire de temps
    private FileManager fileManager; // Gestionnaire de fichiers
    private List<Consumer<String>> statusListeners = new ArrayList<>(); // Listeners pour le statut
    private List<Consumer<Integer>> attemptsListeners = new ArrayList<>(); // Listeners pour les essais
    private List<Runnable> winListeners = new ArrayList<>(); // Listeners pour une victoire
    private List<Runnable> loseListeners = new ArrayList<>(); // Listeners pour une défaite

    public static User currentUser; // Utilisateur actuel (statique)

    public GameManager(TimerManager timerManager, FileManager fileManager) {
        // Initialise le gestionnaire avec les gestionnaires de temps et de fichiers
        this.timerManager = timerManager;
        this.fileManager = fileManager;
    }

    public void addStatusListener(Consumer<String> listener) {
        // Ajoute un listener pour les mises à jour du statut et notifie immédiatement
        statusListeners.add(listener);
        listener.accept("Trouvez toutes les paires !");
    }

    public void addAttemptsListener(Consumer<Integer> listener) {
        // Ajoute un listener pour les mises à jour des essais restants et notifie immédiatement
        attemptsListeners.add(listener);
        listener.accept(attemptsLeft);
    }

    public void addWinListener(Runnable listener) {
        // Ajoute un listener pour notifier une victoire
        winListeners.add(listener);
    }

    public void addLoseListener(Runnable listener) {
        // Ajoute un listener pour notifier une défaite
        loseListeners.add(listener);
    }

    public void initializeGame() {
        // Initialise une nouvelle partie en générant et mélangeant les cartes
        List<String> imagePaths = generateImagePaths(TOTAL_PAIRS);
        List<String> imagePairs = new ArrayList<>();
        for (String path : imagePaths) {
            imagePairs.add(path);
            imagePairs.add(path);
        }
        Collections.shuffle(imagePairs);

        int imageIndex = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                String imagePath = imagePairs.get(imageIndex++);
                Card card = new Card(imagePath);
                card.setOnClickHandler(() -> handleCardClick(card));
                cards[row][col] = card;
            }
        }
        resetGame();
    }

    private List<String> generateImagePaths(int count) {
        // Génère une liste de chemins d'images pour les cartes
        List<String> paths = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            paths.add("/image" + i + ".png");
        }
        return paths;
    }

    public Card[][] getCards() {
        // Retourne la grille de cartes
        return cards;
    }

    public TimerManager getTimerManager() {
        // Retourne le gestionnaire de temps
        return timerManager;
    }

    public FileManager getFileManager() {
        // Retourne le gestionnaire de fichiers
        return fileManager;
    }

    public void resetGame() {
        // Réinitialise la partie avec des valeurs par défaut
        pairsFound = 0;
        attemptsLeft = MAX_ATTEMPTS;
        firstCard = null;
        secondCard = null;
        isProcessing = false;
        statusListeners.forEach(listener -> listener.accept("Trouvez toutes les paires !"));
        attemptsListeners.forEach(listener -> listener.accept(attemptsLeft));
        timerManager.resetTimer();
        for (Card[] row : cards) {
            for (Card card : row) {
                card.setVisible(false);
                card.setMatched(false);
            }
        }
        shuffleCards();
        timerManager.startTimer();
    }

    private void shuffleCards() {
        // Mélange les cartes dans la grille
        List<Card> cardList = new ArrayList<>();
        for (Card[] row : cards) {
            for (Card card : row) {
                cardList.add(card);
            }
        }
        Collections.shuffle(cardList);
        int index = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cards[row][col] = cardList.get(index++);
            }
        }
    }

    private void handleCardClick(Card card) {
        // Gère le clic sur une carte et vérifie les paires ou décrémente les essais
        if (isProcessing || card.isVisible() || card.isMatched() || attemptsLeft <= 0) {
            return;
        }

        card.setVisible(true);

        if (firstCard == null) {
            firstCard = card;
        } else if (secondCard == null) {
            secondCard = card;
            isProcessing = true;

            if (firstCard.getImagePath().equals(secondCard.getImagePath())) {
                firstCard.setMatched(true);
                secondCard.setMatched(true);
                pairsFound++;
                firstCard = null;
                secondCard = null;
                isProcessing = false;
                updatePairsFound(pairsFound);
            } else {
                attemptsLeft--;
                updateAttempts(attemptsLeft);
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    javafx.application.Platform.runLater(() -> {
                        firstCard.setVisible(false);
                        secondCard.setVisible(false);
                        firstCard = null;
                        secondCard = null;
                        isProcessing = false;

                        if (attemptsLeft == 0 && pairsFound != TOTAL_PAIRS) {
                            onLose();
                        }
                    });
                }).start();
            }
        }
    }

    @Override
    public void onWin() {
        // Gère la victoire en arrêtant le timer et sauvegardant les résultats
        timerManager.stopTimer();
        fileManager.saveBestTime(timerManager.getSecondsElapsed());
        if (currentUser != null) {
            currentUser = new User(currentUser.getName(), pairsFound * 10, timerManager.getSecondsElapsed());
            UserDAO.saveUser(currentUser);
        }
        winListeners.forEach(Runnable::run);
    }

    @Override
    public void onLose() {
        // Gère la défaite en arrêtant le timer et notifiant les listeners
        timerManager.stopTimer();
        loseListeners.forEach(Runnable::run);
    }

    @Override
    public void updateAttempts(int attempts) {
        // Met à jour le nombre d'essais restants et notifie les listeners
        attemptsLeft = attempts;
        attemptsListeners.forEach(listener -> listener.accept(attempts));
    }

    @Override
    public void updatePairsFound(int pairs) {
        // Met à jour le nombre de paires trouvées et vérifie la victoire
        if (pairs == TOTAL_PAIRS) {
            onWin();
        }
    }
}