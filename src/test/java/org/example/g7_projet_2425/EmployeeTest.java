package org.example.g7_projet_2425;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void addProjectToHistory() {
        // Arrange
        Employee employee = new Employee(1, "John Doe", "Developer");
        Project project = new Project(101, "Project A", LocalDate.now(), LocalDate.now().plusDays(10), LocalDate.now().plusDays(15));

        // Act
        employee.addProjectToHistory(project);

        // Assert
        assertEquals(1, employee.getProjectHistory().size());
        assertEquals(101, employee.getProjectHistory().get(0).getId());
        assertEquals("Project A", employee.getProjectHistory().get(0).getTitle());
    }

    @Test
    void displayDetails() {
        // Arrange
        Employee employee = new Employee(1, "John Doe", "Developer");

        // Act & Assert
        assertDoesNotThrow(employee::displayDetails);
    }

    @Test
    void displayProjectHistory() {
        // Arrange
        Employee employee = new Employee(1, "John Doe", "Developer");
        Project project1 = new Project(101, "Project A", LocalDate.now(), LocalDate.now().plusDays(10), LocalDate.now().plusDays(15));
        Project project2 = new Project(102, "Project B", LocalDate.now(), LocalDate.now().plusDays(20), LocalDate.now().plusDays(25));

        employee.addProjectToHistory(project1);
        employee.addProjectToHistory(project2);

        // Act & Assert
        assertDoesNotThrow(employee::displayProjectHistory);
    }
}
