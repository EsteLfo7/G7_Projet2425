package org.example.g7_projet_2425.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.g7_projet_2425.Kanban;
import org.example.g7_projet_2425.Task;

import java.time.LocalDate;

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

    @FXML
    private VBox toDoColumn;

    @FXML
    private VBox inProgressColumn;

    @FXML
    private VBox doneColumn;

    @FXML
    private DatePicker calendarPicker;

    @FXML
    private TextArea calendarTasks;

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
        setupDragAndDrop();

        calendarPicker.setOnAction(event -> displayTasksForSelectedDate());
    }

    private void setupDragAndDrop() {
        setupColumnDrag(toDoColumn, "To Do");
        setupColumnDrag(inProgressColumn, "In Progress");
        setupColumnDrag(doneColumn, "Done");
    }

    private void setupColumnDrag(VBox column, String status) {
        column.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });

        column.setOnDragDropped(event -> handleDragDropped(event, status));
    }

    private void handleDragDropped(DragEvent event, String targetColumn) {
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            try {
                int taskId = Integer.parseInt(db.getString());
                Task task = findTaskById(taskId);

                if (task != null) {
                    switch (targetColumn) {
                        case "To Do":
                            kanban.moveTaskToDo(task);
                            task.setStatus("To Do");
                            break;
                        case "In Progress":
                            kanban.moveTaskInProgress(task);
                            task.setStatus("In Progress");
                            break;
                        case "Done":
                            kanban.moveTaskDone(task);
                            task.setStatus("Done");
                            break;
                    }
                    refreshKanbanBoard();
                }
            } catch (NumberFormatException e) {
                System.err.println("Erreur : ID de tâche invalide");
            }
        }
        event.setDropCompleted(true);
        event.consume();
    }

    private Task findTaskById(int id) {
        return taskList.stream().filter(task -> task.getId() == id).findFirst().orElse(null);
    }

    private void refreshKanbanBoard() {
        toDoColumn.getChildren().clear();
        inProgressColumn.getChildren().clear();
        doneColumn.getChildren().clear();

        kanban.getToDo().forEach(task -> toDoColumn.getChildren().add(createDraggableTaskLabel(task)));
        kanban.getInProgress().forEach(task -> inProgressColumn.getChildren().add(createDraggableTaskLabel(task)));
        kanban.getDone().forEach(task -> doneColumn.getChildren().add(createDraggableTaskLabel(task)));
    }

    private HBox createDraggableTaskLabel(Task task) {
        Label taskTitle = new Label(task.getTitle());
        taskTitle.setOnDragDetected(event -> {
            Dragboard db = taskTitle.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(task.getId()));
            db.setContent(content);
            event.consume();
        });

        HBox taskBox = new HBox(10, taskTitle);
        taskBox.setStyle("-fx-padding: 5; -fx-border-color: gray; -fx-background-color: lightgray;");
        return taskBox;
    }

    private void displayTasksForSelectedDate() {
        LocalDate selectedDate = calendarPicker.getValue();
        if (selectedDate != null) {
            StringBuilder tasksForDate = new StringBuilder();
            for (Task task : taskList) {
                if (task.getDeadline().equals(selectedDate)) {
                    tasksForDate.append(task.getTitle()).append("\n");
                }
            }
            calendarTasks.setText(tasksForDate.toString());
        }
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
        refreshKanbanBoard();
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
                refreshKanbanBoard();
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
            refreshKanbanBoard();
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
