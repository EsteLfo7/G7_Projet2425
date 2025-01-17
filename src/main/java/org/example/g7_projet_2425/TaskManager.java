package org.example.g7_projet_2425;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Consumer;

public class TaskManager {
    private static TaskManager instance; // Singleton instance

    private String csvFilePath;
    public Map<Integer, Task> tasks;
    private final List<Consumer<List<Task>>> taskChangeListeners = new ArrayList<>();

    protected TaskManager() {
        tasks = new HashMap<>();

        // Resolve the path to the resources folder
        try {
            URL resourceUrl = getClass().getClassLoader().getResource("tasks.csv");
            if (resourceUrl != null) {
                Path resourcePath = Paths.get(resourceUrl.toURI());
                csvFilePath = resourcePath.toString();
                System.out.println("Fichier trouvé : " + csvFilePath);
            } else {
                throw new FileNotFoundException("Le fichier tasks.csv est introuvable dans le dossier resources.");
            }
        } catch (URISyntaxException | IOException e) {
            System.err.println("Erreur : " + e.getMessage());
            csvFilePath = "resources/data/tasks.csv"; // Fallback to a default path
        }

        loadTasksFromCSV();
        notifyTaskChange();
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public void createTask(int id, String title, String description, int priority, LocalDate deadline, String category) {
        if (!tasks.containsKey(id)) {
            tasks.put(id, new Task(id, title, description, priority, deadline, category));
            saveTasksToCSV();
            notifyTaskChange();
        } else {
            System.out.println("Une tâche avec cet ID existe déjà !");
        }
    }

    public void deleteTask(int id) {
        if (tasks.remove(id) != null) {
            saveTasksToCSV();
            notifyTaskChange();
        }
    }

    public void updateTask(int id, String title, String description, int priority, LocalDate deadline, String category) {
        Task task = tasks.get(id);
        if (task != null) {
            task.updateTask(title, description, priority, deadline, category);
            saveTasksToCSV();
            notifyTaskChange();
        } else {
            System.out.println("Tâche non trouvée !");
        }
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    private void saveTasksToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            writer.write("ID,Title,Description,Priority,Deadline,Category");
            writer.newLine();
            for (Task task : tasks.values()) {
                String line = task.getId() + "," + task.getTitle() + "," + task.getDescription() + "," +
                        task.getPriority() + "," + task.getDeadline() + "," + task.getCategory();
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Le fichier tasks.csv a été modifié avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des tâches : " + e.getMessage());
        }
    }

    public void loadTasksFromCSV() {
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
                if (fields.length < 6) {
                    System.err.println("Ligne ignorée : format incorrect -> " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(fields[0].trim());
                    String title = fields[1].trim();
                    String description = fields[2].trim();
                    int priority = Integer.parseInt(fields[3].trim());
                    LocalDate deadline;
                    try {
                        deadline = LocalDate.parse(fields[4].trim()); // Utilisation de LocalDate.parse
                    } catch (DateTimeParseException e) {
                        System.err.println("Erreur de parsing pour la date : " + fields[4]);
                        deadline = null; // Ou une valeur par défaut, par exemple LocalDate.now()
                    }
                    String category = fields[5].trim();


                    tasks.put(id, new Task(id, title, description, priority, deadline, category));
                } catch (NumberFormatException e) {
                    System.err.println("Ligne ignorée : ID ou priorité invalide -> " + line);
                }
            }
            System.out.println("Les tâches ont été chargées depuis le fichier tasks.csv");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des tâches : " + e.getMessage());
        }
    }


    private void notifyTaskChange() {
        List<Task> taskList = new ArrayList<>(tasks.values());
        for (Consumer<List<Task>> listener : taskChangeListeners) {
            listener.accept(taskList);
        }
    }
}
