package org.example.g7_projet_2425;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    // Instance unique de TaskManager
    private static TaskManager instance;

    // Dictionnaire pour stocker les tâches par projet
    private final Map<Integer, ObservableList<Task>> projectTasks;

    // Constructeur privé pour empêcher l'instanciation externe
    private TaskManager() {
        this.projectTasks = new HashMap<>();
    }

    // Méthode pour obtenir l'instance unique de TaskManager
    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();  // Créer une instance si elle n'existe pas
        }
        return instance;  // Retourner l'instance unique
    }

    // Ajouter une tâche à un projet spécifique
    public void addTaskToProject(int projectId, Task task) {
        projectTasks.computeIfAbsent(projectId, k -> FXCollections.observableArrayList()).add(task);
    }

    // Obtenir la liste des tâches d'un projet
    public ObservableList<Task> getTasksForProject(int projectId) {
        return projectTasks.getOrDefault(projectId, FXCollections.observableArrayList());
    }
}
