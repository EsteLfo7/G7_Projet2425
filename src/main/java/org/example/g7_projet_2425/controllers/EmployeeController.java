package org.example.g7_projet_2425.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.g7_projet_2425.Employee;
import org.example.g7_projet_2425.EmployeeManager;

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

    @FXML
    public void addEmployee() {
        // Boîte de dialogue pour saisir l'ID
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Ajouter un employé");
        idDialog.setHeaderText("Ajouter un nouvel employé");
        idDialog.setContentText("Entrez l'ID de l'employé :");
        Optional<String> idResult = idDialog.showAndWait();

        if (!idResult.isPresent()) return;

        int id;
        try {
            id = Integer.parseInt(idResult.get().trim());
        } catch (NumberFormatException e) {
            showError("Erreur", "L'ID doit être un entier.");
            return;
        }

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

        // Ajout de l'employé via EmployeeManager
        EmployeeManager.getInstance().createEmployee(id, name, role);

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
}
