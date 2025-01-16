package org.example.g7_projet_2425;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProjectManager {
    private static ProjectManager instance; // Instance unique

    private String csvFilePath; // Chemin relatif vers le fichier CSV
    private Map<Integer, Project> projects;
    private String tasksCsvFilePath = "../../resources/tasks.csv";
    private String teamMembersCsvFilePath = "../../resources/team_members.csv";


    // Constructeur
    private ProjectManager() {
        projects = new HashMap<>();

        // Définir le chemin relatif vers le fichier dans le dossier resources
        csvFilePath = "../../resources/project.csv"; // Remplacez par votre chemin exact

        Path absolutePath = Paths.get(csvFilePath);

        if (Files.exists(absolutePath)) {
            System.out.println("Fichier trouvé : " + absolutePath);
        } else {
            System.err.println("Erreur : le fichier projects.csv est introuvable au chemin absolu : " + absolutePath);
            // Optionnel : Par défaut, utiliser un fichier dans le répertoire courant
            csvFilePath = "employees.csv";
        }
        // Charger les données depuis les fichiers CSV au démarrage
        loadProjectsFromCSV();
        loadTasksFromCSV();
        loadTeamMembersFromCSV();
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
            saveTasksToCSV(); // Sauvegarde automatique
            System.out.println("Tâche ajoutée avec succès au projet : " + project.getTitle());
        } else {
            System.out.println("Projet introuvable !");
        }
    }

    // Ajouter un employé à un projet
    public void addEmployeeToProject(int projectId, Employee employee, String role) {
        Project project = projects.get(projectId);
        if (project != null) {
            project.addTeamMember(employee);
            saveTeamMembersToCSV(role); // Sauvegarde automatique
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

    // Sauvegarder les tâches dans un fichier CSV
    private void saveTasksToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tasksCsvFilePath))) {
            writer.write("ProjectID,TaskID,Title,Description,Priority,Deadline,Category");
            writer.newLine();
            for (Project project : projects.values()) {
                for (Task task : project.getTasks()) {
                    String line = project.getId() + "," + task.getId() + "," + task.getTitle() + "," + task.getDescription() + "," + task.getPriority() + "," + task.getDeadline() + "," + task.getCategory();
                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("Le fichier tasks.csv a été modifié avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des tâches : " + e.getMessage());
        }
    }

    // Sauvegarder les membres d'équipe dans un fichier CSV
    private void saveTeamMembersToCSV(String role) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(teamMembersCsvFilePath))) {
            writer.write("ProjectID,employeeId, name, role");
            writer.newLine();
            for (Project project : projects.values()) {
                for (Employee member : project.getTeamMembers()) {
                    String line = project.getId() + "," + member.getId() + "," + member.getName() + "," + role;
                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("Le fichier team_members.csv a été modifié avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des membres d'équipe : " + e.getMessage());
        }
    }

    // Charger les projets depuis un fichier CSV
    private void loadProjectsFromCSV() {
        csvFilePath="projects.csv";
        File file = new File(csvFilePath);
        System.out.println(csvFilePath);
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
            System.out.println("Les projets ont été chargés depuis le fichier projects.csv" );
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des projets : " + e.getMessage());
        }
    }

    // Charger les tâches depuis un fichier CSV
    private void loadTasksFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(tasksCsvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) { // ProjectID, TaskID, Title, Description, Priority, Deadline, Category
                    try {
                        int projectId = Integer.parseInt(parts[0]); // ID du projet
                        int taskId = Integer.parseInt(parts[1]); // ID de la tâche
                        String title = parts[2]; // Titre de la tâche
                        String description = parts[3]; // Description
                        String priority = parts[4]; // Priorité
                        LocalDate deadline = LocalDate.parse(parts[5]); // Deadline
                        String category = parts[6]; // Catégorie

                        // Ajoutez la tâche au projet correspondant
                        Task task = new Task(taskId, title, description, priority, deadline, category);
                        addTaskToProject(projectId, task);
                    } catch (NumberFormatException e) {
                        System.err.println("Erreur de format pour la ligne : " + line);
                    }
                } else {
                    System.err.println("Ligne mal formatée : " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier tasks.csv : " + e.getMessage());
        }
    }



    // Charger les membres d'équipe depuis un fichier CSV

    private void loadTeamMembersFromCSV() {
        File file = new File(teamMembersCsvFilePath);
        if (!file.exists()) {
            System.out.println("Fichier team_members.csv non trouvé. Création d'une base vide.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(teamMembersCsvFilePath))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Ignore the header line
                    continue;
                }
                String[] fields = line.split(",");
                if (fields.length < 4) {
                    System.err.println("Ligne invalide dans le fichier CSV : " + line);
                    continue;
                }

                int projectId = Integer.parseInt(fields[0].trim());
                int employeeId = Integer.parseInt(fields[1].trim());
                String name = fields[2].trim();
                String role = fields[3].trim();

                Project project = projects.get(projectId);
                if (project != null) {
                    Employee employee = new Employee(employeeId, name, role);
                    project.addTeamMember(employee);
                } else {
                    System.err.println("Projet ID non trouvé pour l'employé : " + name);
                }
            }
            System.out.println("Les membres d'équipe ont été chargés depuis le fichier team_members.csv");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erreur lors du chargement des membres d'équipe : " + e.getMessage());
        }
    }





    // Retourner la liste des projets
    public Map<Integer, Project> getProjects() {
        return projects;
    }

}
