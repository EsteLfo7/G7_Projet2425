package org.example.g7_projet_2425.models;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    // Initialization
    private int id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Employee> teamMembers;
    private List<Task> tasks;
    private LocalDate deliverableDeadline;

    // Constructor
    public Project(int id, String title, LocalDate startDate, LocalDate endDate, LocalDate deliverableDeadline) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deliverableDeadline = deliverableDeadline;
        this.teamMembers = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    // Methods
    public void addTeamMember(Employee employee) {
        teamMembers.add(employee);
    }

    public void removeTeamMember(Employee employee) {
        teamMembers.remove(employee);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public void displayTeam() {
        System.out.println("Team members for project " + title + ":");
        for (Employee employee : teamMembers) {
            System.out.println(" - " + employee.getName() + " (" + employee.getRole() + ")");
        }
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Employee> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<Employee> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public LocalDate getDeliverableDeadline() {
        return deliverableDeadline;
    }

    public void setDeliverableDeadline(LocalDate deliverableDeadline) {
        this.deliverableDeadline = deliverableDeadline;
    }


}
