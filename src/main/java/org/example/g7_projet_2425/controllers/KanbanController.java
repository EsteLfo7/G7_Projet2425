package org.example.g7_projet_2425.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import org.example.g7_projet_2425.Kanban;
import org.example.g7_projet_2425.Task;

import java.io.IOException;

public class KanbanController {

    @FXML
    private VBox toDoColumn;

    @FXML
    private VBox inProgressColumn;

    @FXML
    private VBox doneColumn;

    private Kanban kanban;

    @FXML
    public void initialize() {
        kanban = new Kanban();
        setupDragAndDrop();
        refreshKanbanBoard();
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
                            break;
                        case "In Progress":
                            kanban.moveTaskInProgress(task);
                            break;
                        case "Done":
                            kanban.moveTaskDone(task);
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
        return kanban.getAllTasks().stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void refreshKanbanBoard() {
        toDoColumn.getChildren().clear();
        inProgressColumn.getChildren().clear();
        doneColumn.getChildren().clear();

        kanban.getToDo().forEach(task -> toDoColumn.getChildren().add(createDraggableTaskLabel(task)));
        kanban.getInProgress().forEach(task -> inProgressColumn.getChildren().add(createDraggableTaskLabel(task)));
        kanban.getDone().forEach(task -> doneColumn.getChildren().add(createDraggableTaskLabel(task)));
    }

    private VBox createDraggableTaskLabel(Task task) {
        VBox taskBox = new VBox();
        taskBox.getChildren().add(new Label(task.getTitle()));
        taskBox.setOnDragDetected(event -> {
            Dragboard db = taskBox.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(task.getId()));
            db.setContent(content);
            event.consume();
        });
        return taskBox;
    }

    @FXML
    public void backToTaskView() throws IOException {
        switchScene("task-view.fxml", "Liste des Tâches");
    }

    private void switchScene(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/g7_projet_2425/" + fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) toDoColumn.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
    }
}
