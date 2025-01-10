package org.example.g7_projet_2425.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.g7_projet_2425.Employee;
import org.example.g7_projet_2425.Project;
import org.example.g7_projet_2425.Task;

import java.time.LocalDate;
import java.util.Optional;

public class ProjectController {

    @FXML
    private TableView<Project> projectTable;

    @FXML
    private TableColumn<Project, Integer> idColumn;

    @FXML
    private TableColumn<Project, String> titleColumn;

    @FXML
    private TableColumn<Project, LocalDate> startDateColumn;

    @FXML
    private TableColumn<Project, LocalDate> endDateColumn;

    @FXML
    private TableColumn<Project, LocalDate> deadlineColumn;

    private ObservableList<Employee> availableEmployees = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés des objets Project
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        // Ajouter des données fictives pour tester (à supprimer ou adapter en production)
        projectTable.getItems().add(new Project(1, "Projet Alpha", LocalDate.now(), LocalDate.now().plusDays(30), LocalDate.now().plusDays(15)));

        // Ajouter des employés fictifs (à remplacer par les données réelles)
        availableEmployees.addAll(
                new Employee(1, "Alice", "Développeur"),
                new Employee(2, "Bob", "Designer"),
                new Employee(3, "Charlie", "Testeur")
        );
    }

    @FXML
    public void assignTeamMembers() {
        Project selectedProject = projectTable.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            showError("Erreur", "Aucun projet sélectionné.");
            return;
        }

        ChoiceDialog<Employee> employeeDialog = new ChoiceDialog<>(null, availableEmployees);
        employeeDialog.setTitle("Attribuer un membre de l'équipe");
        employeeDialog.setHeaderText("Sélectionnez un employé à ajouter au projet : " + selectedProject.getTitle());
        employeeDialog.setContentText("Employés disponibles :");

        Optional<Employee> selectedEmployee = employeeDialog.showAndWait();
        selectedEmployee.ifPresent(employee -> {
            String role = promptUser("Attribuer un rôle", "Entrez le rôle pour " + employee.getName() + ":");
            if (role == null || role.isBlank()) {
                showError("Erreur", "Le rôle ne peut pas être vide.");
                return;
            }

            employee.setRole(role);
            selectedProject.addTeamMember(employee);
            availableEmployees.remove(employee);

            System.out.println("Employé ajouté : " + employee.getName() + " avec le rôle : " + role);
        });
    }

    @FXML
    public void addProject() {
        // Saisie des informations du projet
        int id = generateProjectId();
        String title = promptUser("Titre du projet", "Entrez le titre du projet :");
        if (title == null || title.isBlank()) {
            showError("Erreur", "Le titre du projet ne peut pas être vide.");
            return;
        }

        LocalDate startDate = showDateDialog("Entrez la date de début (AAAA-MM-JJ) :");
        if (startDate == null) return;

        LocalDate endDate = showDateDialog("Entrez la date de fin (AAAA-MM-JJ) :");
        if (endDate == null || endDate.isBefore(startDate)) {
            showError("Erreur", "La date de fin doit être postérieure à la date de début.");
            return;
        }

        LocalDate deadline = showDateDialog("Entrez la deadline (AAAA-MM-JJ) :");
        if (deadline == null) return;

        // Création et ajout du projet
        Project newProject = new Project(id, title, startDate, endDate, deadline);
        projectTable.getItems().add(newProject);
        System.out.println("Projet ajouté : " + title);
    }

    @FXML
    public void editProject() {
        Project selectedProject = projectTable.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            showError("Erreur", "Aucun projet sélectionné.");
            return;
        }

        // Modification des informations
        String newTitle = promptUser("Modifier le titre", "Titre actuel : " + selectedProject.getTitle());
        if (newTitle != null && !newTitle.isBlank()) {
            selectedProject.setTitle(newTitle);
        }

        LocalDate newDeadline = showDateDialog("Modifier la deadline (Actuelle : " + selectedProject.getEndDate() + ")");
        if (newDeadline != null) {
            selectedProject.setEndDate(newDeadline);
        }

        projectTable.refresh(); // Actualiser la table
        System.out.println("Projet modifié : " + selectedProject.getTitle());
    }

    @FXML
    public void deleteProject() {
        Project selectedProject = projectTable.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            showError("Erreur", "Aucun projet sélectionné.");
            return;
        }

        projectTable.getItems().remove(selectedProject);
        System.out.println("Projet supprimé : " + selectedProject.getTitle());
    }

    @FXML
    public void viewTeam() {
        Project selectedProject = projectTable.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            showError("Erreur", "Aucun projet sélectionné.");
            return;
        }

        System.out.println("Équipe du projet : " + selectedProject.getTitle());
        for (Employee member : selectedProject.getTeamMembers()) {
            System.out.println(member.getName() + " - " + member.getRole());
        }
    }

    @FXML
    public void addTask() {
        Project selectedProject = projectTable.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            showError("Erreur", "Aucun projet sélectionné.");
            return;
        }

        // Création d'une nouvelle tâche liée au projet
        int taskId = generateTaskId();
        String title = promptUser("Titre de la tâche", "Entrez le titre de la tâche :");
        if (title == null || title.isBlank()) {
            showError("Erreur", "Le titre de la tâche ne peut pas être vide.");
            return;
        }

        String description = promptUser("Description", "Entrez une description de la tâche :");
        if (description == null || description.isBlank()) {
            showError("Erreur", "La description ne peut pas être vide.");
            return;
        }

        LocalDate deadline = showDateDialog("Entrez la deadline de la tâche (AAAA-MM-JJ)");
        if (deadline == null) return;

        Task newTask = new Task(taskId, title, description, "Moyenne", deadline, "Générale");
        selectedProject.addTask(newTask);
        System.out.println("Tâche ajoutée au projet : " + selectedProject.getTitle());
    }

    @FXML
    public void viewTasks() {
        Project selectedProject = projectTable.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            showError("Erreur", "Aucun projet sélectionné.");
            return;
        }

        System.out.println("Liste des tâches pour le projet : " + selectedProject.getTitle());
        for (Task task : selectedProject.getTasks()) {
            System.out.println(task.displayDetails());
        }
    }

    private String promptUser(String title, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(content);
        dialog.setContentText(null);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private LocalDate showDateDialog(String message) {
        TextInputDialog dateDialog = new TextInputDialog();
        dateDialog.setTitle("Date");
        dateDialog.setHeaderText(message);
        dateDialog.setContentText("Format : AAAA-MM-JJ");
        Optional<String> result = dateDialog.showAndWait();

        if (!result.isPresent()) return null;

        try {
            return LocalDate.parse(result.get().trim());
        } catch (Exception e) {
            showError("Erreur", "Date invalide.");
            return null;
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int generateProjectId() {
        return (int) (Math.random() * 10000); // Génération simple d'un ID
    }

    private int generateTaskId() {
        return (int) (Math.random() * 10000); // Génération simple d'un ID
    }
}
