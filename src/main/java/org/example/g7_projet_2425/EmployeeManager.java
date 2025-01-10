package org.example.g7_projet_2425;

import java.util.HashMap;
import java.util.Map;

public class EmployeeManager {
    private static EmployeeManager instance; // Instance unique

    public Map<Integer, Employee> employees;

    protected EmployeeManager() {
        employees = new HashMap<>();
        loadEmployeesFromCSV(); // Charger les employés depuis le fichier CSV au démarrage
    }

    public static EmployeeManager getInstance() {
        if (instance == null) {
            instance = new EmployeeManager();
        }
        return instance;
    }

    public void createEmployee(int id, String name, String role) {
        if (!employees.containsKey(id)) {
            employees.put(id, new Employee(id, name, role));
            saveEmployeesToCSV(); // Sauvegarde automatique
        } else {
            System.out.println("Un employé avec cet ID existe déjà !");
        }
    }

    public void deleteEmployee(int id) {
        if (employees.remove(id) != null) {
            saveEmployeesToCSV(); // Sauvegarde automatique
        }
    }

    public void updateEmployeeRole(int id, String newRole) {
        Employee employee = employees.get(id);
        if (employee != null) {
            employee.updateRole(newRole);
            saveEmployeesToCSV(); // Sauvegarde automatique
        } else {
            System.out.println("Employé non trouvé !");
        }
    }

    public Map<Integer, Employee> getEmployees() {
        return employees;
    }

    private void loadEmployeesFromCSV() {
        // Charger les employés depuis le fichier CSV
        // Implémentation existante
    }

    private void saveEmployeesToCSV() {
        // Sauvegarder les employés dans le fichier CSV
        // Implémentation existante
    }
}
