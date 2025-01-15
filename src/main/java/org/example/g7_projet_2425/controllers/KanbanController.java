package org.example.g7_projet_2425.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import org.example.g7_projet_2425.Kanban;
import org.example.g7_projet_2425.Task;

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
}
