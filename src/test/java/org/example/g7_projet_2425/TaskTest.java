package org.example.g7_projet_2425;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void updateTask() {
        Task task = new Task(1, "Initial Title", "Initial Description", "High", LocalDate.of(2024, 12, 31), "Work");
        List<String> comments = new ArrayList<>();
        comments.add("First comment");
        task.updateTask("Updated Title", "Updated Description", "Medium", LocalDate.of(2025, 1, 15), "Personal", comments);

        assertEquals("Updated Title", task.getTitle());
        assertEquals("Updated Description", task.getDescription());
        assertEquals("Medium", task.getPriority());
        assertEquals(LocalDate.of(2025, 1, 15), task.getDeadline());
        assertEquals("Personal", task.getCategory());
        assertEquals(comments, task.getComments());
    }

    @Test
    void createTask() {
        Task task = Task.createTask(2, "New Task", "Task Description", "Low", LocalDate.of(2024, 12, 25), "Urgent", new ArrayList<>());

        assertNotNull(task);
        assertEquals(2, task.getId());
        assertEquals("New Task", task.getTitle());
        assertEquals("Task Description", task.getDescription());
        assertEquals("Low", task.getPriority());
        assertEquals(LocalDate.of(2024, 12, 25), task.getDeadline());
        assertEquals("Urgent", task.getCategory());
    }

    @Test
    void deleteTask() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task(1, "Task 1", "Description 1", "High", LocalDate.of(2024, 12, 31), "Work");
        Task task2 = new Task(2, "Task 2", "Description 2", "Medium", LocalDate.of(2024, 12, 20), "Personal");
        tasks.add(task1);
        tasks.add(task2);

        Task.deleteTask(tasks, 1);

        assertEquals(1, tasks.size());
        assertEquals(2, tasks.get(0).getId());
    }

    @Test
    void displayDetails() {
        Task task = new Task(3, "Display Test", "Testing display details", "Low", LocalDate.of(2024, 12, 15), "Misc");
        task.displayDetails();
        // No assertion needed; this is for manual verification or redirection of output in testing.
    }

    @Test
    void addComment() {
        Task task = new Task(4, "Comment Test", "Testing comments", "High", LocalDate.of(2024, 12, 10), "Feedback");
        task.addComment("This is a new comment.");

        assertTrue(task.getComments().contains("This is a new comment."));
    }
}