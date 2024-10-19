module com.example.sudoku {
    // Required dependencies
    requires javafx.controls;
    requires javafx.fxml;

    // Apertura de paquetes para el cargador de FXML
    opens com.example.sudoku to javafx.fxml;
    opens com.example.sudoku.controller to javafx.fxml;
    // Exportación del paquete principal
    exports com.example.sudoku;
}