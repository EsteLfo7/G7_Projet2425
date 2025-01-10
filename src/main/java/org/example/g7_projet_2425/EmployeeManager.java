package org.example.g7_projet_2425.models;

import java.util.HashMap;
import java.util.Map;

public class EmployeeManager {
    Map<Integer, Employee> employees;

    public EmployeeManager() {
        employees = new HashMap<>();
    }

    public void createEmployee(int id, String name, String role) {
        if (!employees.containsKey(id)) {
            employees.put(id, new Employee(id, name, role));
        } else {
            System.out.println("Un employé avec cet ID existe déjà !");
        }
    }

    public void deleteEmployee(int id) {
        employees.remove(id);
    }

    public void updateEmployeeRole(int id, String newRole) {
        Employee employee = employees.get(id);
        if (employee != null) {
            employee.updateRole(newRole);
        } else {
            System.out.println("Employé non trouvé !");
        }
    }

    public void displayEmployeeInfo(int id) {
        Employee employee = employees.get(id);
        if (employee != null) {
            System.out.println(employee.displayDetails());
        } else {
            System.out.println("Employé non trouvé !");
        }
    }
}
