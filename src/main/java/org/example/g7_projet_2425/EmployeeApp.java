package org.example.g7_projet_2425.models;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeApp extends Application {
    private EmployeeManager employeeManager = new EmployeeManager();

    @Override
    public void start(Stage stage) {
        VBox layout = new VBox(10);
        TextField nameField = new TextField();
        TextField roleField = new TextField();
        Button addButton = new Button("Ajouter Employé");

        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String role = roleField.getText();
            int id = employeeManager.employees.size() + 1;
            employeeManager.createEmployee(id, name, role);
        });

        layout.getChildren().addAll(new Label("Nom:"), nameField, new Label("Rôle:"), roleField, addButton);
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Gestion des Employés");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
