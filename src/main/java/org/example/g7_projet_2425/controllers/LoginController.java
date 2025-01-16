package org.example.g7_projet_2425.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.g7_projet_2425.Employee;
import org.example.g7_projet_2425.EmployeeManager;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessage;

    @FXML
    private void handleLogin() {
        String name = usernameField.getText();
        String password = passwordField.getText();

        if (isValidLogin(name, password)) {
            errorMessage.setText("Connexion réussie !");
            errorMessage.setStyle("-fx-text-fill: green;");

            // Charger la vue principale après une connexion réussie
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Accueil");
                stage.show();

                // Fermer la fenêtre de connexion actuelle
                Stage currentStage = (Stage) usernameField.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
                errorMessage.setText("Erreur lors du chargement de la vue principale.");
            }

        } else {
            errorMessage.setText("Nom d'utilisateur ou mot de passe incorrect.");
            errorMessage.setStyle("-fx-text-fill: red;");
        }
    }

    private boolean isValidLogin(String name, String password) {
        // Accéder à l'instance d'EmployeeManager pour vérifier les identifiants
        EmployeeManager employeeManager = EmployeeManager.getInstance();

        // Parcourir la liste des employés pour vérifier les correspondances
        for (Employee employee : employeeManager.getEmployees().values()) {
            if (employee.getName().equals(name) && employee.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
