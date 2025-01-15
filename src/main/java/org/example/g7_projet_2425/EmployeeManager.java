package org.example.g7_projet_2425;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EmployeeManager {
    private static EmployeeManager instance; // Instance unique

    private String csvFilePath; // Chemin relatif vers le fichier CSV
    public Map<Integer, Employee> employees;
    private final List<Consumer<List<Employee>>> employeeChangeListeners = new ArrayList<>();

    protected EmployeeManager() {
        employees = new HashMap<>();

        // Utiliser un chemin absolu pour le fichier CSV
        csvFilePath = "C:/Users/Esteban/OneDrive - ISEP/Documents/COURS/PROG/FICHIERS/IntelliJ/G7_Projet_2425/src/main/resources/data/employees.csv"; // Remplacez par votre chemin exact

        Path absolutePath = Paths.get(csvFilePath);

        if (Files.exists(absolutePath)) {
            System.out.println("Fichier trouvé : " + absolutePath);
        } else {
            System.err.println("Erreur : le fichier employees.csv est introuvable au chemin absolu : " + absolutePath);
            // Optionnel : Par défaut, utiliser un fichier dans le répertoire courant
            csvFilePath = "employees.csv";
        }

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
                int id = Integer.parseInt(fields[0].trim());
                String name = fields[1].trim();
                String role = fields[2].trim();
                employees.put(id, new Employee(id, name, role));
            }
            System.out.println("Les employés ont été chargés depuis le fichier employees.csv");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des employés : " + e.getMessage());
        }
    }

    // Méthode pour enregistrer un écouteur
    public void addEmployeeChangeListener(Consumer<List<Employee>> listener) {
        employeeChangeListeners.add(listener);

    }
    // Méthode pour notifier les changements
    private void notifyEmployeeChange() {
        List<Employee> employeeList = new ArrayList<>(employees.values());
        for (Consumer<List<Employee>> listener : employeeChangeListeners) {
            listener.accept(employeeList);
        }
    }







}
