package org.example.g7_projet_2425;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    private Project project;
    private Employee employee1;
    private Employee employee2;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        project = new Project(1, "Test Project", LocalDate.now(), LocalDate.now().plusDays(10), LocalDate.now().plusDays(5));
        employee1 = new Employee(101, "Alice", "Developer");
        employee2 = new Employee(102, "Bob", "Designer");
        task1 = new Task(201, "Task 1", "Description 1", "High", LocalDate.now().plusDays(3), "Development");
        task2 = new Task(202, "Task 2", "Description 2", "Medium", LocalDate.now().plusDays(5), "Testing");
    }

    @Test
    void addTeamMember() {
        project.addTeamMember(employee1);
        project.addTeamMember(employee2);

        List<Employee> teamMembers = project.getTeamMembers();
        assertEquals(2, teamMembers.size());
        assertTrue(teamMembers.contains(employee1));
        assertTrue(teamMembers.contains(employee2));
    }

    @Test
    void removeTeamMember() {
        project.addTeamMember(employee1);
        project.addTeamMember(employee2);
        project.removeTeamMember(employee1);

        List<Employee> teamMembers = project.getTeamMembers();
        assertEquals(1, teamMembers.size());
        assertFalse(teamMembers.contains(employee1));
        assertTrue(teamMembers.contains(employee2));
    }

    @Test
    void addTask() {
        project.addTask(task1);
        project.addTask(task2);

        List<Task> tasks = project.getTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void removeTask() {
        project.addTask(task1);
        project.addTask(task2);
        project.removeTask(task1);

        List<Task> tasks = project.getTasks();
        assertEquals(1, tasks.size());
        assertFalse(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void displayTeam() {
        project.addTeamMember(employee1);
        project.addTeamMember(employee2);

        project.displayTeam();

        // Verifies the output manually as displayTeam prints to console
    }
}
