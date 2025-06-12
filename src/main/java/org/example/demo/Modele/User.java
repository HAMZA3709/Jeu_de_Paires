package org.example.demo.Modele;

public class User {
    private String name; // Nom de l'utilisateur
    private int time; // Temps du jeu

    public User(String name, int score, int time) {
        // Initialise l'utilisateur avec un nom et un temps
        this.name = name;
        this.time = time;
    }

    public String getName() {
        // Retourne le nom de l'utilisateur
        return name;
    }

    public int getTime() {
        // Retourne le temps de l'utilisateur
        return time;
    }
}