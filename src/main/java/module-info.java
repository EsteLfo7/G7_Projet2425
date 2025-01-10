module org.example.g7_projet_2425 {
    requires javafx.controls;
    requires javafx.fxml;

    // Expose les packages contenant les classes nécessaires
    exports org.example.g7_projet_2425;
    exports org.example.g7_projet_2425.apps;
    exports org.example.g7_projet_2425.controllers;

    // Ouvre les packages contenant les fichiers FXML pour JavaFX
    opens org.example.g7_projet_2425 to javafx.fxml;
    opens org.example.g7_projet_2425.apps to javafx.fxml;
    opens org.example.g7_projet_2425.controllers to javafx.fxml;
    opens views to javafx.fxml;
}
