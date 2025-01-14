package org.example.g7_projet_2425;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProjectManager {
    private static ProjectManager instance; // Instance unique

    private final String csvFilePath; // Chemin relatif vers le fichier CSV
    private Map<Integer, Project> projects;

    // Constructeur
    private ProjectManager() {
        projects = new HashMap<>();

        // Définir le chemin relatif vers le fichier dans le dossier resources
        URL resource = getClass().getResource("/data/projects.csv");
        if (resource != null) {
            csvFilePath = Paths.get(resource.getPath()).toString();
        } else {
            System.err.println("Erreur : le fichier projects.csv est introuvable dans le dossier resources.");
            csvFilePath = "projects.csv"; // Par défaut, créer dans le répertoire courant
        }

        loadProjectsFromCSV(); // Charger les projets depuis le fichier CSV au démarrage
    }

    public static ProjectManager getInstance() {
        if (instance == null) {
            instance = new ProjectManager();
        }
        return instance;
    }

    // Créer un nouveau projet
    public void createProject(int id, String title, LocalDate startDate, LocalDate endDate) {
        if (!projects.containsKey(id)) {
            Project project = new Project(id, title, startDate, endDate);
            projects.put(id, project);
            saveProjectsToCSV(); // Sauvegarde automatique
            System.out.println("Projet ajouté avec succès : " + title);
        } else {
            System.out.println("Un projet avec cet ID existe déjà !");
        }
    }

    public void updateProject(int id, String newTitle, LocalDate newStartDate, LocalDate newEndDate) {
        if (projects.containsKey(id)) {
            Project project = projects.get(id);
            project.setTitle(newTitle);
            project.setStartDate(newStartDate);
            project.setEndDate(newEndDate);
            saveProjectsToCSV(); // Sauvegarde automatique
            System.out.println("Projet mis à jour avec succès : " + newTitle);
        } else {
            System.out.println("Projet introuvable !");
        }
    }


    // Supprimer un projet
    public void deleteProject(int id) {
        if (projects.containsKey(id)) {
            projects.remove(id);
            saveProjectsToCSV(); // Sauvegarde automatique
            System.out.println("Projet supprimé avec succès.");
        } else {
            System.out.println("Projet introuvable !");
        }
    }

    // Afficher les détails d'un projet
    public void displayProjectDetails(int id) {
        Project project = projects.get(id);
        if (project != null) {
            System.out.println("Détails du projet :");
            System.out.println("ID : " + project.getId());
            System.out.println("Titre : " + project.getTitle());
            System.out.println("Date de début : " + project.getStartDate());
            System.out.println("Date de fin : " + project.getEndDate());
            System.out.println("Équipe :");
            project.displayTeam();
            System.out.println("Tâches :");
            for (Task task : project.getTasks()) {
                System.out.println(" - " + task.getTitle() + " (priorité : " + task.getPriority() + ")");
            }
        } else {
            System.out.println("Projet introuvable !");
        }
    }

    // Ajouter une tâche à un projet
    public void addTaskToProject(int projectId, Task task) {
        Project project = projects.get(projectId);
        if (project != null) {
            project.addTask(task);
            saveProjectsToCSV(); // Sauvegarde automatique
            System.out.println("Tâche ajoutée avec succès au projet : " + project.getTitle());
        } else {
            System.out.println("Projet introuvable !");
        }
    }

    // Ajouter un employé à un projet
    public void addEmployeeToProject(int projectId, Employee employee) {
        Project project = projects.get(projectId);
        if (project != null) {
            project.addTeamMember(employee);
            saveProjectsToCSV(); // Sauvegarde automatique
            System.out.println("Employé ajouté à l'équipe du projet : " + project.getTitle());
        } else {
            System.out.println("Projet introuvable !");
        }
    }

    // Sauvegarder les projets dans un fichier CSV
    private void saveProjectsToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            writer.write("ID,Title,StartDate,EndDate");
            writer.newLine();
            for (Project project : projects.values()) {
                String line = project.getId() + "," + project.getTitle() + "," +
                        project.getStartDate() + "," + project.getEndDate();
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Le fichier projects.csv a été modifié avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des projets : " + e.getMessage());
        }
    }

    // Charger les projets depuis un fichier CSV
    private void loadProjectsFromCSV() {
        File file = new File(csvFilePath);
        if (!file.exists()) {
            System.out.println("Fichier CSV non trouvé. Création d'une base vide.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0].trim());
                String title = fields[1].trim();
                LocalDate startDate = LocalDate.parse(fields[2].trim());
                LocalDate endDate = LocalDate.parse(fields[3].trim());
                projects.put(id, new Project(id, title, startDate, endDate));
            }
            System.out.println("Les projets ont été chargés depuis " + csvFilePath);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des projets : " + e.getMessage());
        }
    }

    // Retourner la liste des projets
    public Map<Integer, Project> getProjects() {
        return projects;
    }

}
