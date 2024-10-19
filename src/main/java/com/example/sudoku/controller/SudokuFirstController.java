package com.example.sudoku.controller;

import com.example.sudoku.model.Sudoku;
import com.example.sudoku.view.SudokuGameStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the initial stage of the Sudoku application.
 */
public class SudokuFirstController {
    @FXML
    private Button IniciarJuego;

    private Stage primaryStage;

    /**
     * Method that is called when the "Iniciar Juego" button is pressed.
     * @param event The action event triggered by the button press.
     */
    @FXML
    private void handleIniciarJuego(ActionEvent event) {
        // Crear una instancia de Sudoku y generar el tablero
        Sudoku sudoku = new Sudoku();
        sudoku.iniciarNuevoJuego();  // Genera el tablero al iniciar el juego

        // Cerrar la ventana actual (SudokuFirstStage)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        // Abrir la ventana del juego (SudokuGameStage) con el Sudoku generado
        try {
            SudokuGameStage gameStage = new SudokuGameStage(); // Crear la instancia de la ventana
            gameStage.setSudoku(sudoku); // Pasar el objeto Sudoku al controlador
            gameStage.show(); // Mostrar la ventana del juego
        } catch (IOException e) {
            e.printStackTrace();
            // Puedes mostrar un mensaje de error aquí si lo deseas
        }
    }

    /**
     * Method to set the primary stage.
     * @param stage The primary stage of the application.
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
