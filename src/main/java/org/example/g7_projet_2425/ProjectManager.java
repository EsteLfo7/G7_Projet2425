package org.example.g7_projet_2425;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProjectManager {
    private Map<Integer, Project> projects;

    // Constructor
    public ProjectManager() {
        this.projects = new HashMap<>();
    }

    // Create a new project
    public void createProject(int id, String title, LocalDate startDate, LocalDate endDate, LocalDate deliverableDeadline) {
        if (!projects.containsKey(id)) {
            Project project = new Project(id, title, startDate, endDate, deliverableDeadline);
            projects.put(id, project);
            System.out.println("Projet ajouté avec succès : " + title);
        } else {
            System.out.println("Un projet avec cet ID existe déjà !");
        }
    }

    // Delete a project by its ID
    public void deleteProject(int id) {
        if (projects.containsKey(id)) {
            projects.remove(id);
            System.out.println("Projet supprimé avec succès.");
        } else {
            System.out.println("Projet introuvable !");
        }
    }

    // Display details of a specific project
    public void displayProjectDetails(int id) {
        Project project = projects.get(id);
        if (project != null) {
            System.out.println("Détails du projet :");
            System.out.println("ID : " + project.getId());
            System.out.println("Titre : " + project.getTitle());
            System.out.println("Date de début : " + project.getStartDate());
            System.out.println("Date de fin : " + project.getEndDate());
            System.out.println("Échéance des livrables : " + project.getDeliverableDeadline());
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

    // Add a task to a project
    public void addTaskToProject(int projectId, Task task) {
        Project project = projects.get(projectId);
        if (project != null) {
            project.addTask(task);
            System.out.println("Tâche ajoutée avec succès au projet : " + project.getTitle());
        } else {
            System.out.println("Projet introuvable !");
        }
    }

    // Add an employee to a project's team
    public void addEmployeeToProject(int projectId, Employee employee) {
        Project project = projects.get(projectId);
        if (project != null) {
            project.addTeamMember(employee);
            System.out.println("Employé ajouté à l'équipe du projet : " + project.getTitle());
        } else {
            System.out.println("Projet introuvable !");
        }
    }
}
