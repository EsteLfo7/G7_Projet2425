package org.example.g7_projet_2425;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int id;
    private String name;
    private String role;
    private List<Project> projectHistory;

    public Employee(int id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.projectHistory = new ArrayList<>();
    }

    // Principal Methods
    public void addProjectToHistory(Project project) {
        projectHistory.add(project);
    }

    public String displayDetails() {
        return "ID: " + id + ", Name: " + name + ", Role: " + role;
    }

    public void displayProjectHistory() {
        System.out.println("Project History for " + name + ":");
        for (Project project : projectHistory) {
            System.out.println(" - " + project.getTitle());
        }
    }

    // Methods to Manage Employees

    public void updateRole(String newRole) {
        this.role = newRole;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Project> getProjectHistory() {
        return projectHistory;
    }

    public void setProjectHistory(List<Project> projectHistory) {
        this.projectHistory = projectHistory;
    }
}


