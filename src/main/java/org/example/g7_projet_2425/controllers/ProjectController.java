package org.example.g7_projet_2425.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.g7_projet_2425.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.ChoiceDialog;



public class ProjectController {

    @FXML
    private VBox rootVBox;

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
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> taskIdColumn;

    @FXML
    private TableColumn<Task, String> taskTitleColumn;

    @FXML
    private TableColumn<Task, String> taskDescriptionColumn;

    @FXML
    private TableColumn<Task, LocalDate> taskEndDateColumn;

    private ObservableList<Employee> availableEmployees = FXCollections.observableArrayList();

    private ObservableList<Project> projectList;

    @FXML
    public void initialize() {
        // Lier les colonnes des projets
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        // Lier les colonnes des tâches
        taskIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        taskTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        taskEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));



        projectList = FXCollections.observableArrayList(ProjectManager.getInstance().getProjects().values());
        projectTable.setItems(projectList);

        // Cacher la table des tâches au démarrage
        taskTable.setVisible(false);
        EmployeeManager.getInstance().addEmployeeChangeListener(updatedEmployees -> {
            availableEmployees.setAll(updatedEmployees);
        });

        // Charger les employés au démarrage
        availableEmployees.setAll(EmployeeManager.getInstance().getEmployees().values());

    }

    public void assignTeamMembers() {
        Project selectedProject = projectTable.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            showError("Erreur", "Aucun projet sélectionné.");
            return;
        }

        // Filtrer les employés disponibles
        ObservableList<Employee> filteredEmployees = FXCollections.observableArrayList(
                availableEmployees.stream()
                        .filter(e -> !selectedProject.getTeamMembers().contains(e))
                        .toList()
        );

        if (filteredEmployees.isEmpty()) {
            showError("Aucun employé disponible", "Tous les employés sont déjà assignés.");
            return;
        }

        // Création d'une liste de noms d'employés pour l'affichage
        List<String> employeeNames = filteredEmployees.stream()
                .map(Employee::getName)
                .toList();

        // Affichage des noms dans le ChoiceDialog
        ChoiceDialog<String> employeeDialog = new ChoiceDialog<>(null, FXCollections.observableArrayList(employeeNames));
        employeeDialog.setTitle("Attribuer un membre de l'équipe");
        employeeDialog.setHeaderText("Sélectionnez un employé à ajouter au projet : " + selectedProject.getTitle());
        employeeDialog.setContentText("Employés disponibles :");

        // Récupération du nom sélectionné
        Optional<String> selectedName = employeeDialog.showAndWait();

        selectedName.ifPresent(name -> {
            // Trouver l'employé correspondant au nom sélectionné
            Employee selectedEmployee = filteredEmployees.stream()
                    .filter(e -> e.getName().equals(name))
                    .findFirst()
                    .orElse(null);

            if (selectedEmployee == null) {
                showError("Erreur", "Employé introuvable.");
                return;
            }

            // Demander le rôle pour cet employé
            String role = promptUser("Attribuer un rôle", "Entrez le rôle pour " + selectedEmployee.getName() + ":");
            if (role == null || role.isBlank()) {
                showError("Erreur", "Le rôle ne peut pas être vide.");
                return;
            }

            // Ajouter l'employé au projet avec le rôle
            selectedEmployee.setRole(role);
            selectedProject.addTeamMember(selectedEmployee);
            availableEmployees.remove(selectedEmployee);

            System.out.println("Employé ajouté : " + selectedEmployee.getName() + " avec le rôle : " + role);
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

        // Création et ajout du projet
        Project newProject = new Project(id, title, startDate, endDate);
        projectTable.getItems().add(newProject);
        System.out.println("Projet ajouté : " + title);

        ProjectManager.getInstance().createProject(id, title, startDate, endDate);

        // Mise à jour de la table
        projectList.setAll(ProjectManager.getInstance().getProjects().values());
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

        LocalDate newEndDate = showDateDialog("Modifier la date de fin (Actuelle : " + selectedProject.getEndDate() + ")");
        if (newEndDate != null) {
            selectedProject.setEndDate(newEndDate);
        }

        projectTable.refresh(); // Actualiser la table
        System.out.println("Projet modifié : " + selectedProject.getTitle());

        ProjectManager.getInstance().updateProject(selectedProject.getId(), selectedProject.getTitle(), selectedProject.getStartDate(), selectedProject.getEndDate());

        // Mise à jour de la table
        projectList.setAll(ProjectManager.getInstance().getProjects().values());
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

        // Créer une nouvelle tâche
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

        LocalDate endDate = showDateDialog("Entrez la date de fin de la tâche (AAAA-MM-JJ)");
        if (endDate == null) return;

        Task newTask = new Task(taskId, title, description, "Moyenne", endDate, "Générale");
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

        // Lier les tâches au tableau des tâches
        ObservableList<Task> tasks = FXCollections.observableArrayList(selectedProject.getTasks());
        taskTable.setItems(tasks);

        // Afficher la table des tâches
        taskTable.setVisible(true);
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
