package org.example.g7_projet_2425.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.g7_projet_2425.Kanban;
import org.example.g7_projet_2425.Task;

import java.time.LocalDate;
import java.util.List;

public class TaskController {

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> idColumn;

    @FXML
    private TableColumn<Task, String> titleColumn;

    @FXML
    private TableColumn<Task, String> priorityColumn;

    @FXML
    private TableColumn<Task, LocalDate> endDateColumn;

    @FXML
    private TableColumn<Task, String> statusColumn;

    private ObservableList<Task> taskList = FXCollections.observableArrayList();
    private Kanban kanban;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        taskTable.setItems(taskList);

        kanban = new Kanban();
    }

    @FXML
    public void viewAllTasks() {
        List<Task> allTasks = kanban.getAllTasks();

        // Fenêtre dédiée
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

    @FXML
    public void addTask() {
        Task newTask = new Task(
                taskList.size() + 1,
                "Nouvelle Tâche",
                "Description par défaut",
                "Moyenne",
                LocalDate.now().plusDays(7),
                "To Do"
        );
        kanban.moveTaskToDo(newTask);
        taskList.add(newTask);
    }


private Task findTaskById(int id) {
        return taskList.stream().filter(task -> task.getId() == id).findFirst().orElse(null);
    }





    @FXML
    public void editTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            TextInputDialog dialog = new TextInputDialog(selectedTask.getTitle());
            dialog.setTitle("Modifier Tâche");
            dialog.setHeaderText("Modifier le titre de la tâche");
            dialog.setContentText("Titre :");

            dialog.showAndWait().ifPresent(newTitle -> {
                selectedTask.setTitle(newTitle);
                kanban.moveTaskDone(selectedTask);
                taskList.remove(selectedTask);
                taskTable.refresh();
            });
        } else {
            showAlert("Erreur", "La tâche sélectionnée est invalide.");
        }
    }


    @FXML
    public void deleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskList.remove(selectedTask);
            kanban.getToDo().remove(selectedTask);
            kanban.getInProgress().remove(selectedTask);
            kanban.getDone().remove(selectedTask);

        } else {
            showAlert("Erreur", "Veuillez sélectionner une tâche à supprimer.");
        }
    }


    @FXML
    public void assignTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Assigner Tâche");
            dialog.setHeaderText("Assigner une tâche");
            dialog.setContentText("Nom de l'utilisateur :");

            dialog.showAndWait().ifPresent(assignee -> {
                selectedTask.setStatus(assignee);
                refreshKanbanBoard();
                taskTable.refresh();
            });
        } else {
            showAlert("Erreur", "Veuillez sélectionner une tâche à assigner.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
