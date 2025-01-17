package org.example.g7_projet_2425.apps;

import org.example.g7_projet_2425.Employee;
import org.example.g7_projet_2425.Project;
import org.example.g7_projet_2425.Task;

import java.util.ArrayList;
import java.util.List;

public class Application {
    // Liste globale des employés
    private List<Employee> employees;

    // Liste globale des projets
    private List<Project> projects;

    // Liste globale des tâches
    private List<Task> tasks;

    // Instance unique (singleton)
    private static Application instance;

    // Constructeur privé (singleton)
    private Application() {
        employees = new ArrayList<>();
        projects = new ArrayList<>();
        tasks = new ArrayList<>();
        initializeDefaultData(); // Charger des données par défaut si nécessaire
    }

    // Méthode pour obtenir l'instance unique
    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    // Méthodes pour gérer les employés
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    // Méthodes pour gérer les projets
    public void addProject(Project project) {
        projects.add(project);
    }

    public void removeProject(Project project) {
        projects.remove(project);
    }

    public List<Project> getProjects() {
        return projects;
    }

    // Méthodes pour gérer les tâches
    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    // Méthode pour initialiser des données par défaut (si nécessaire)
    private void initializeDefaultData() {

        // Ajouter un projet par défaut
        Project defaultProject = new Project(1, "Migration système", null, null);
        projects.add(defaultProject);

        // Ajouter quelques tâches par défaut
        tasks.add(new Task(1, "Configurer serveur", null, 0, defaultProject));
        tasks.add(new Task(2, "Développer module client", null, 0, defaultProject));
    }

    // Méthode pour afficher un résumé de l'état actuel de l'application
    public void displaySummary() {
        System.out.println("=== Résumé de l'Application ===");
        System.out.println("Nombre d'employés : " + employees.size());
        System.out.println("Nombre de projets : " + projects.size());
        System.out.println("Nombre de tâches : " + tasks.size());
    }

    // Méthode pour sauvegarder les données dans un fichier (optionnel)
    public void saveDataToFile(String filePath) {
        // Implémenter une logique pour sauvegarder les listes (employees, projects, tasks) dans un fichier
        // Par exemple, utiliser une bibliothèque comme Gson ou Jackson pour la sérialisation JSON
    }

    // Méthode pour charger les données depuis un fichier (optionnel)
    public void loadDataFromFile(String filePath) {
        // Implémenter une logique pour charger les listes (employees, projects, tasks) depuis un fichier
    }

    // Méthode pour réinitialiser toutes les données
    public void resetApplication() {
        employees.clear();
        projects.clear();
        tasks.clear();
        initializeDefaultData();
    }
}
