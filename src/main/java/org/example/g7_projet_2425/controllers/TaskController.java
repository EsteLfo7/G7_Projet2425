package org.example.g7_projet_2425.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.g7_projet_2425.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class TaskController {

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> idColumn;

    @FXML
    private TableColumn<Task, String> titleColumn;

    @FXML
    private TableColumn<Task, Integer> priorityColumn;

    @FXML
    private TableColumn<Task, LocalDate> endDateColumn;

    @FXML
    private TableColumn<Task, String> statusColumn;

    private ObservableList<Task> taskList = FXCollections.observableArrayList();
    private Kanban kanban;

    @FXML
    public void initialize() {
        // Vérification et configuration des colonnes
        if (idColumn == null) {
            idColumn = new TableColumn<>("ID");
        }
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        if (titleColumn == null) {
            titleColumn = new TableColumn<>("Title");
        }
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        if (priorityColumn == null) {
            priorityColumn = new TableColumn<>("Priority");
        }
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));

        if (endDateColumn == null) {
            endDateColumn = new TableColumn<>("End Date");
        }
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        if (statusColumn == null) {
            statusColumn = new TableColumn<>("Status");
        }
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Configuration de la TableView
        if (taskTable == null) {
            taskTable = new TableView<>();
        }
        taskTable.setItems(taskList);

        // Initialisation de Kanban
        kanban = new Kanban();
        taskList.setAll(TaskManager.getInstance().getTasks().values());
    }

    @FXML
    public void openCalendarView() throws IOException {
        switchScene("CalendarView.fxml", "Calendrier des Tâches");
    }

    @FXML
    public void openKanbanView() throws IOException {
        switchScene("KanbanView.fxml", "Kanban des Tâches");
    }

    @FXML
    public void backToTaskView() throws IOException {
        switchScene("TaskView.fxml", "Liste des Tâches");
    }


    @FXML
    public void viewMain(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        }
        catch (IOException e)  {e.printStackTrace();}
    }

    private void switchScene(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxmlFile));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void viewAllTasks() {
        List<Task> allTasks = kanban.getAllTasks();

        // Création d'une nouvelle fenêtre
        Stage taskWindow = new Stage();
        VBox taskLayout = new VBox(10);
        taskLayout.setStyle("-fx-padding: 10;");

        for (Task task : allTasks) {
            Label taskLabel = new Label(task.toString());
            taskLayout.getChildren().add(taskLabel);
        }

        Scene scene = new Scene(taskLayout, 400, 400);
        taskWindow.setTitle("Toutes les Tâches");
        taskWindow.setScene(scene);
        taskWindow.show();
    }

    private int generateTaskId() {
        return (int) (Math.random() * 10000); // Génération d'un ID aléatoire
    }

    @FXML
    public void addTask() {
        try {
            int id = generateTaskId();

            // Saisie des informations
            String title = promptForInput("Ajouter une tâche", "Entrez le titre de la tâche :", "Le titre ne peut pas être vide.");
            if (title == null) return;

            String description = promptForInput("Ajouter une tâche", "Entrez la description de la tâche :", "La description ne peut pas être vide.");
            if (description == null) return;

            String priorityInput = promptForInput("Ajouter une tâche", "Entrez la priorité (1-5) :", "La priorité est obligatoire.");
            if (priorityInput == null) return;

            int priority;
            try {
                priority = Integer.parseInt(priorityInput.trim());
            } catch (NumberFormatException e) {
                showError("Erreur", "La priorité doit être un nombre.");
                return;
            }

            String deadlineInput = promptForInput("Ajouter une tâche", "Entrez la date limite (YYYY-MM-DD) :", "La date limite est obligatoire.");
            if (deadlineInput == null) return;

            LocalDate deadline;
            try {
                deadline = LocalDate.parse(deadlineInput.trim());
            } catch (DateTimeParseException e) {
                showError("Erreur", "Format de date invalide. Utilisez YYYY-MM-DD.");
                return;
            }

            String category = promptForInput("Ajouter une tâche", "Entrez la catégorie :", "La catégorie est obligatoire.");
            if (category == null) return;

            TaskManager.getInstance().createTask(id, title, description, priority, deadline, category);
            taskList.setAll(TaskManager.getInstance().getTasks().values());
            Task task = new Task(id, title, description, priority, deadline, category);
            kanban.addTask(task);
        } catch (Exception e) {
            showError("Erreur", "Échec de l'ajout de la tâche.");
        }

    }

    @FXML
    public void editTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showError("Erreur", "Veuillez sélectionner une tâche pour la modifier.");
            return;
        }

        try {
            String input = promptForInput("Modifier une tâche", "Entrez title,description,priority,deadline,category :", "Les informations sont obligatoires.");
            if (input == null) return;

            String[] data = input.split(",");
            String title = data[0].trim();
            String description = data[1].trim();
            int priority = Integer.parseInt(data[2].trim());
            LocalDate deadline = LocalDate.parse(data[3].trim());
            String category = data[4].trim();

            TaskManager.getInstance().updateTask(selectedTask.getId(), title, description, priority, deadline, category);
            taskList.setAll(TaskManager.getInstance().getTasks().values());
        } catch (Exception e) {
            showError("Erreur", "Échec de la modification de la tâche.");
        }
    }

    @FXML
    public void deleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showError("Erreur", "Veuillez sélectionner une tâche à supprimer.");
            return;
        }

        TaskManager.getInstance().deleteTask(selectedTask.getId());
        taskList.setAll(TaskManager.getInstance().getTasks().values());
    }

    @FXML
    public void assignTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            String assignee = promptForInput("Assigner une tâche", "Nom de l'utilisateur :", "Le nom de l'utilisateur est obligatoire.");
            if (assignee == null) return;

            selectedTask.setStatus(assignee);
            taskTable.refresh();
        } else {
            showError("Erreur", "Veuillez sélectionner une tâche à assigner.");
        }
    }

    private String promptForInput(String title, String content, String errorMessage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            return result.get().trim();
        } else {
            showError("Erreur", errorMessage);
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
}
