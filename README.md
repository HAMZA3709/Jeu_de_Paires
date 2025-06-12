🎮 Jeu de Paires - Mini Projet Java S6

Projet réalisé dans le cadre du module Java (GI S6 - Année universitaire 2024/2025).
Ce jeu de mémoire met au défi les utilisateurs de retrouver toutes les paires d’images identiques dans un temps limité, 
avec un nombre d’essais restreint. L’objectif est de tester la concentration et la rapidité du joueur tout en offrant une interface graphique conviviale.

⚙️ Technologies utilisées
.JavaFX (interface graphique moderne)

.SQLite (base de données locale pour la gestion des scores)

.Modèle MVC (architecture logicielle)

.Interface graphique desktop

.Gestion du Top 3 scores via sauvegarde locale dans un fichier

🎯 Objectif du jeu
Ce jeu a pour objectif de stimuler la mémoire du joueur en l'invitant à retrouver toutes les paires d'images dissimulées dans une grille,
dans un temps limité et avec un nombre restreint d'erreurs autorisées.

.La grille est composée de 16 cartes (format 4x4) retournées aléatoirement

.Chaque image est présente en double, formant des paires

.Le joueur doit identifier toutes les paires en évitant de dépasser 3 erreurs

.Le jeu est chronométré afin de mesurer la rapidité du joueur

.En cas de réussite, le score est sauvegardé localement dans un fichier .txt s’il figure parmi le Top 3
📁 Structure du projet
Modifier
JeuDePaires/
├── .idea/                 # Fichiers de configuration IntelliJ IDEA
├── .mvn/wrapper/          # Wrapper Maven pour exécuter le projet sans installation globale
├── lib/                   # Librairies externes éventuelles
├── src/main/              # Code source principal de l’application
│   └── java/...           # Classes Java (MVC, logique du jeu, etc.)
├── .gitignore             # Fichiers/dossiers à ignorer par Git
├── best_times.txt         # Sauvegarde des meilleurs scores (Top 3)
├── mvnw                   # Script Unix pour lancer Maven Wrapper
├── mvnw.cmd               # Script Windows pour lancer Maven Wrapper
├── pom.xml                # Fichier de configuration Maven (dépendances, build, etc.)
├── test.db                # Base de données SQLite locale


📦 Installation & ▶️ Lancement du projet
✅ Prérequis
Java JDK 17+ installé

Maven installé (ou utiliser mvnw / mvnw.cmd fournis)

Un IDE compatible (comme IntelliJ IDEA, Eclipse, VS Code)

🔧 Étapes d’installation
Cloner le dépôt :

git clone https://github.com/ton-utilisateur/JeuDePaires.git
cd JeuDePaires
Compiler le projet avec Maven :
./mvnw clean install       # (Linux/macOS)
mvnw.cmd clean install     # (Windows)
Lancer l'application :
./mvnw javafx:run

🛠️ Dépendances utilisées (dans pom.xml)
JavaFX (controls, FXML)

SQLite JDBC driver

Maven JavaFX plugin
 🖼️ Capture d’écran - Écran de jeu
Voici l’interface principale du jeu, affichant une grille 4x4 de cartes retournées.
![Screenshot 2025-06-12 170541](https://github.com/user-attachments/assets/7d59b010-73d5-437d-8027-6190d3a474f6)
![Screenshot 2025-06-12 170602](https://github.com/user-attachments/assets/e886aaec-7a49-4b07-af73-8e31d33519b4)
![Screenshot 2025-06-12 171105](https://github.com/user-attachments/assets/17446f0b-70da-4686-b2c1-072fe433a315)





