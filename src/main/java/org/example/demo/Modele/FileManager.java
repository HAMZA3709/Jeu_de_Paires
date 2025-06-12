package org.example.demo.Modele;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FileManager {
    private static final String BEST_TIME_FILE = System.getProperty("user.dir") + "/best_times.txt"; // Chemin du fichier des meilleurs temps
    private static final int MAX_SCORES = 3; // Nombre maximum de scores enregistrés
    private List<Integer> bestTimes = new ArrayList<>(); // Liste des meilleurs temps
    private List<Consumer<List<Integer>>> bestTimesListeners = new ArrayList<>(); // Listeners pour les meilleurs temps

    public FileManager() {
        // Initialise le gestionnaire et charge les meilleurs temps
        System.out.println("FileManager initialized. File path: " + BEST_TIME_FILE);
        loadBestTimes();
    }

    public void addBestTimesListener(Consumer<List<Integer>> listener) {
        // Ajoute un listener pour les mises à jour des meilleurs temps
        bestTimesListeners.add(listener);
        listener.accept(new ArrayList<>(bestTimes));
    }

    private void loadBestTimes() {
        // Charge les meilleurs temps à partir du fichier
        bestTimes.clear();
        File file = new File(BEST_TIME_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created new file: " + BEST_TIME_FILE);
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(BEST_TIME_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    int time = Integer.parseInt(line.trim());
                    bestTimes.add(time);
                    System.out.println("Loaded time: " + time);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading best times: " + e.getMessage());
        }
        Collections.sort(bestTimes);
        if (bestTimes.size() > MAX_SCORES) {
            bestTimes = bestTimes.subList(0, MAX_SCORES);
        }
        notifyBestTimesListeners();
    }

    public void saveBestTime(int newTime) {
        // Enregistre un nouveau meilleur temps dans le fichier
        System.out.println("Saving new time: " + newTime);
        bestTimes.add(newTime);
        Collections.sort(bestTimes);
        if (bestTimes.size() > MAX_SCORES) {
            bestTimes = bestTimes.subList(0, MAX_SCORES);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BEST_TIME_FILE, false))) {
            for (Integer time : bestTimes) {
                writer.write(String.valueOf(time));
                writer.newLine();
            }
            System.out.println("Successfully saved best times to file: " + bestTimes);
        } catch (IOException e) {
            System.out.println("Error saving best times: " + e.getMessage());
        }
        notifyBestTimesListeners();
    }

    private void notifyBestTimesListeners() {
        // Notifie tous les listeners des mises à jour des meilleurs temps
        bestTimesListeners.forEach(listener -> listener.accept(new ArrayList<>(bestTimes)));
    }

    public void refreshBestTimes() {
        // Rafraîchit la liste des meilleurs temps
        System.out.println("Refreshing best times...");
        loadBestTimes();
    }

    public List<Integer> getBestTimes() {
        // Retourne une copie de la liste des meilleurs temps
        return new ArrayList<>(bestTimes);
    }
}