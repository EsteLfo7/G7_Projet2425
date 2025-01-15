package org.example.g7_projet_2425;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private final Map<Integer, ObservableList<Task>> projectTasks;

    public TaskManager() {
        this.projectTasks = new HashMap<>();
    }

    public void addTaskToProject(int projectId, Task task) {
        projectTasks.computeIfAbsent(projectId, k -> FXCollections.observableArrayList()).add(task);
    }

    public ObservableList<Task> getTasksForProject(int projectId) {
        return projectTasks.getOrDefault(projectId, FXCollections.observableArrayList());
    }
}
