package org.example.g7_projet_2425;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;

public class EmployeeManager {
    private static EmployeeManager instance; // Singleton instance

    private String csvFilePath;
    public Map<Integer, Employee> employees;
    private final List<Consumer<List<Employee>>> employeeChangeListeners = new ArrayList<>();

    protected EmployeeManager() {
        employees = new HashMap<>();

        // Resolve the path to the resources folder
        try {
            URL resourceUrl = getClass().getClassLoader().getResource("employees.csv");
            if (resourceUrl != null) {
                Path resourcePath = Paths.get(resourceUrl.toURI());
                csvFilePath = resourcePath.toString();
                System.out.println("Fichier trouvé : " + csvFilePath);
            } else {
                throw new FileNotFoundException("Le fichier employees.csv est introuvable dans le dossier resources.");
            }
        } catch (URISyntaxException | IOException e) {
            System.err.println("Erreur : " + e.getMessage());
            csvFilePath = "resources/data/employees.csv"; // Fallback to a default path
        }

        loadEmployeesFromCSV(); // Load employees from the CSV file at startup
    }

    public static EmployeeManager getInstance() {
        if (instance == null) {
            instance = new EmployeeManager();
        }
        return instance;
    }

    public void createEmployee(int id, String name, String role, String password) {
        if (!employees.containsKey(id)) {
            employees.put(id, new Employee(id, name, role, password));
            saveEmployeesToCSV();
            notifyEmployeeChange();
        } else {
            System.out.println("Un employé avec cet ID existe déjà !");
        }
    }


    public void deleteEmployee(int id) {
        if (employees.remove(id) != null) {
            saveEmployeesToCSV();
            notifyEmployeeChange();
        }
    }

    public void updateEmployeeRole(int id, String newRole) {
        Employee employee = employees.get(id);
        if (employee != null) {
            employee.updateRole(newRole);
            saveEmployeesToCSV();
            notifyEmployeeChange();
        } else {
            System.out.println("Employé non trouvé !");
        }
    }

    public Map<Integer, Employee> getEmployees() {
        return employees;
    }

    private void saveEmployeesToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            writer.write("ID,Name,Role");
            writer.newLine();
            for (Employee employee : employees.values()) {
                String line = employee.getId() + "," + employee.getName() + "," + employee.getRole();
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Le fichier employees.csv a été modifié avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des employés : " + e.getMessage());
        }
    }

    private void loadEmployeesFromCSV() {
        File file = new File(csvFilePath);
        if (!file.exists()) {
            System.out.println("Fichier CSV non trouvé. Création d'une base vide.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(",");

                // Validation du format CSV
                if (fields.length < 4) {
                    System.err.println("Ligne ignorée : format incorrect -> " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(fields[0].trim());
                    String name = fields[1].trim();
                    String role = fields[2].trim();
                    String password = fields[3].trim();

                    employees.put(id, new Employee(id, name, role, password));
                } catch (NumberFormatException e) {
                    System.err.println("Ligne ignorée : ID invalide -> " + line);
                }
            }
            System.out.println("Les employés ont été chargés depuis le fichier employees.csv");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des employés : " + e.getMessage());
        }
    }


    // Method to register a listener
    public void addEmployeeChangeListener(Consumer<List<Employee>> listener) {
        employeeChangeListeners.add(listener);
    }

    // Method to notify listeners of changes
    private void notifyEmployeeChange() {
        List<Employee> employeeList = new ArrayList<>(employees.values());
        for (Consumer<List<Employee>> listener : employeeChangeListeners) {
            listener.accept(employeeList);
        }
    }
}
