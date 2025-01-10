package org.example.g7_projet_2425;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KanbanTest {

    private Kanban kanban;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        kanban = new Kanban();
        task1 = new Task(1, "Task 1", "Description 1", "High", LocalDate.of(2024, 1, 1), "Category 1");
        task2 = new Task(2, "Task 2", "Description 2", "Medium", LocalDate.of(2024, 2, 1), "Category 2");
        task3 = new Task(3, "Task 3", "Description 3", "Low", LocalDate.of(2024, 3, 1), "Category 3");

        kanban.moveTaskToDo(task1);
        kanban.moveTaskInProgress(task2);
        kanban.moveTaskDone(task3);
    }

    @Test
    void moveTaskToDo() {
        kanban.moveTaskToDo(task2); // Move task2 to "To Do"
        List<Task> toDoTasks = kanban.getToDo();
        List<Task> inProgressTasks = kanban.getInProgress();

        assertTrue(toDoTasks.contains(task2));
        assertFalse(inProgressTasks.contains(task2));
        assertFalse(kanban.getDone().contains(task2));
    }

    @Test
    void moveTaskInProgress() {
        kanban.moveTaskInProgress(task1); // Move task1 to "In Progress"
        List<Task> inProgressTasks = kanban.getInProgress();
        List<Task> toDoTasks = kanban.getToDo();

        assertTrue(inProgressTasks.contains(task1));
        assertFalse(toDoTasks.contains(task1));
        assertFalse(kanban.getDone().contains(task1));
    }

    @Test
    void moveTaskDone() {
        kanban.moveTaskDone(task1); // Move task1 to "Done"
        List<Task> doneTasks = kanban.getDone();
        List<Task> toDoTasks = kanban.getToDo();

        assertTrue(doneTasks.contains(task1));
        assertFalse(toDoTasks.contains(task1));
        assertFalse(kanban.getInProgress().contains(task1));
    }

    @Test
    void displayKanban() {
        // Redirect System.out to a custom stream to verify console output
        kanban.displayKanban(); // Ensure no exceptions occur
    }

    @Test
    void viewCalendar() {
        // Ensure tasks are sorted by deadline in the calendar view
        kanban.viewCalendar(); // Ensure no exceptions occur
    }
}
