package org.example.g7_projet_2425.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.g7_projet_2425.Employee;
import org.example.g7_projet_2425.EmployeeManager;

import java.io.IOException;
import java.util.Optional;

public class EmployeeController {

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Integer> idColumn;

    @FXML
    private TableColumn<Employee, String> nameColumn;

    @FXML
    private TableColumn<Employee, String> roleColumn;

    private ObservableList<Employee> employeeList;

    @FXML
    public void initialize() {
        // Initialisation des colonnes de la table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Charger les employés depuis EmployeeManager
        employeeList = FXCollections.observableArrayList(EmployeeManager.getInstance().getEmployees().values());
        employeeTable.setItems(employeeList);
    }

    private int generateEmployeeId() {
        return (int) (Math.random() * 10000); // Génération simple d'un ID
    }

    @FXML
    public void addEmployee() {
        // Boîte de dialogue pour saisir l'ID
        int id = generateEmployeeId();


        // Boîte de dialogue pour saisir le nom
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Ajouter un employé");
        nameDialog.setHeaderText("Ajouter un nouvel employé");
        nameDialog.setContentText("Entrez le nom de l'employé :");
        Optional<String> nameResult = nameDialog.showAndWait();

        if (!nameResult.isPresent() || nameResult.get().trim().isEmpty()) {
            showError("Erreur", "Le nom ne peut pas être vide.");
            return;
        }
        String name = nameResult.get().trim();

        // Boîte de dialogue pour saisir le rôle
        TextInputDialog roleDialog = new TextInputDialog();
        roleDialog.setTitle("Ajouter un employé");
        roleDialog.setHeaderText("Ajouter un nouvel employé");
        roleDialog.setContentText("Entrez le rôle de l'employé :");
        Optional<String> roleResult = roleDialog.showAndWait();

        if (!roleResult.isPresent() || roleResult.get().trim().isEmpty()) {
            showError("Erreur", "Le rôle ne peut pas être vide.");
            return;
        }
        String role = roleResult.get().trim();

        // Boîte de dialogue pour saisir le mot de passe
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Ajouter un employé");
        passwordDialog.setHeaderText("Ajouter un nouvel employé");
        passwordDialog.setContentText("Entrez le mot de passe de l'employé :");
        Optional<String> passwordResult = passwordDialog.showAndWait();

        if (!passwordResult.isPresent() || passwordResult.get().trim().isEmpty()) {
            showError("Erreur", "Le mot de passe ne peut pas être vide.");
            return;
        }
        String password = passwordResult.get().trim();

        // Ajout de l'employé via EmployeeManager
        EmployeeManager.getInstance().createEmployee(id, name, role, password);

        // Mise à jour de la table
        employeeList.setAll(EmployeeManager.getInstance().getEmployees().values());
    }


    @FXML
    public void editEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showError("Aucun employé sélectionné", "Veuillez sélectionner un employé pour le modifier.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedEmployee.getName() + "," + selectedEmployee.getRole());
        dialog.setTitle("Modifier un employé");
        dialog.setHeaderText("Modifier les informations de l'employé");
        dialog.setContentText("Entrez les nouvelles informations sous la forme : name,role");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            try {
                String[] data = input.split(",");
                String name = data[0].trim();
                String role = data[1].trim();
                selectedEmployee.setName(name);
                selectedEmployee.setRole(role);
                employeeTable.refresh();
                // Edit de l'employé via EmployeeManager
                EmployeeManager.getInstance().updateEmployeeRole(selectedEmployee.getId(),role);

                // Mise à jour de la table
                employeeList.setAll(EmployeeManager.getInstance().getEmployees().values());
            } catch (Exception e) {
                showError("Erreur", "Format incorrect. Utilisez : name,role.");
            }
        });

    }

    @FXML
    public void deleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showError("Aucun employé sélectionné", "Veuillez sélectionner un employé à supprimer.");
            return;
        }

        // Suppression de l'employé via EmployeeManager
        EmployeeManager.getInstance().deleteEmployee(selectedEmployee.getId());

        // Mise à jour de la table
        employeeList.setAll(EmployeeManager.getInstance().getEmployees().values());
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void viewMain(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        }
        catch (IOException e)  {e.printStackTrace();}
    }
}
