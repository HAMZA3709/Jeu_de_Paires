package org.example.demo.Modele;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TimerManager {
    private Timeline timer; // Chronomètre
    private int secondsElapsed; // Temps écoulé en secondes
    private List<Consumer<Integer>> timeListeners = new ArrayList<>(); // Listeners pour le temps

    public TimerManager() {
        // Initialise le chronomètre et le temps
        this.secondsElapsed = 0;
        this.timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            notifyTimeListeners();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
    }

    public void addTimeListener(Consumer<Integer> listener) {
        // Ajoute un listener pour les mises à jour du temps
        timeListeners.add(listener);
        listener.accept(secondsElapsed);
    }

    private void notifyTimeListeners() {
        // Notifie tous les listeners des changements de temps
        timeListeners.forEach(listener -> listener.accept(secondsElapsed));
    }

    public void startTimer() {
        // Démarre le chronomètre
        timer.play();
    }

    public void stopTimer() {
        // Arrête le chronomètre
        timer.stop();
    }

    public void resetTimer() {
        // Réinitialise le chronomètre
        stopTimer();
        secondsElapsed = 0;
        notifyTimeListeners();
    }

    public int getSecondsElapsed() {
        // Retourne le temps écoulé
        return secondsElapsed;
    }
}