package org.example.g7_projet_2425;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Kanban {
    private List<Task> toDo;
    private List<Task> inProgress;
    private List<Task> done;

    public Kanban() {
        this.toDo = new ArrayList<>();
        this.inProgress = new ArrayList<>();
        this.done = new ArrayList<>();
    }

    public void addTask(Task task) {
        // Ajout de la tâche à la liste To Do
        if (!toDo.contains(task)) {
            toDo.add(task);
        }
    }

    public void moveTaskToDo(Task task) {
        inProgress.remove(task);
        done.remove(task);
        if (!toDo.contains(task)) {
            toDo.add(task);
        }
    }

    public void moveTaskInProgress(Task task) {
        toDo.remove(task);
        done.remove(task);
        if (!inProgress.contains(task)) {
            inProgress.add(task);
        }
    }

    public void moveTaskDone(Task task) {
        toDo.remove(task);
        inProgress.remove(task);
        if (!done.contains(task)) {
            done.add(task);
        }
    }

    public void displayKanban() {
        System.out.println("To Do Tasks:");
        for (Task task : toDo) {
            System.out.println(task);
        }
        System.out.println("\nIn Progress Tasks:");
        for (Task task : inProgress) {
            System.out.println(task);
        }
        System.out.println("\nDone Tasks:");
        for (Task task : done) {
            System.out.println(task);
        }
    }

    public void viewCalendar() {
        System.out.println("Calendar View:");
        List<Task> allTasks = getAllTasks();
        allTasks.sort((t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()));

        for (Task task : allTasks) {
            System.out.println(task.getTitle() + " - Deadline: " + task.getDeadline());
        }
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(toDo);
        allTasks.addAll(inProgress);
        allTasks.addAll(done);
        return allTasks;
    }

    public List<Task> getToDo() {
        return toDo;
    }

    public List<Task> getInProgress() {
        return inProgress;
    }

    public List<Task> getDone() {
        return done;
    }
}
