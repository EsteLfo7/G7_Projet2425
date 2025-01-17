package org.example.g7_projet_2425;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private int id;
    private String title;
    private String description;
    private int priority;
    private LocalDate endDate;
    private String category;
    private List<String> comments;
    private String status;
    private Project project; // Association avec un projet

    // Constructeur existant
    public Task(int id, String title, String description, int priority,LocalDate endDate, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.endDate = endDate;
        this.category = category;
        this.status = "To Do";
        this.comments = new ArrayList<>();
    }

    // Nouveau constructeur surchargé pour inclure un projet
    public Task(int id, String title, String description, int priority, Project project) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.project = project;
        this.status = "To Do";
        this.comments = new ArrayList<>();
    }

    // Méthodes
    public void updateTask(String title, String description, int priority, LocalDate endDate, String category) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.endDate = endDate;
        this.category = category;
    }

    public static Task createTask(int id, String title, String description, int priority, LocalDate deadline, String category, List<String> comments) {
        return new Task(id, title, description, priority, deadline, category);
    }

    public static void deleteTask(List<Task> tasks, int id) {
        tasks.removeIf(task -> task.getId() == id);
    }

    public String displayDetails() {
        return "Id: " + id + ", Title: " + title + ", Description: " + description + ", Priority: " + priority + ", Deadline: " + endDate + ", Category: " + category;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDate getDeadline() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getComments() {
        return comments;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
