package org.example.g7_projet_2425;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int id;
    private String name;
    private String role;
    private String password;
    private List<Project> projectHistory;

    public Employee(int id, String name, String role, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.password = password;
        this.projectHistory = new ArrayList<>();
    }

    public void addProjectToHistory(Project project) {
        projectHistory.add(project);
    }

    public String displayDetails() {
        return "ID: " + id + ", Name: " + name + ", Role: " + role + ", Password: " + password;
    }

    public void displayProjectHistory() {
        System.out.println("Project History for " + name + ":");
        for (Project project : projectHistory) {
            System.out.println(" - " + project.getTitle());
        }
    }

    public void updateRole(String newRole) {
        this.role = newRole;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Project> getProjectHistory() {
        return projectHistory;
    }

    public void setProjectHistory(List<Project> projectHistory) {
        this.projectHistory = projectHistory;
    }
}
