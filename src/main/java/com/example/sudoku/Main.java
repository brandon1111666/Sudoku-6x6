package com.example.sudoku;

import javafx.application.Application;
import javafx.stage.Stage;
import com.example.sudoku.view.SudokuFirstStage;

/**
 * Main class that starts the Sudoku application.
 */
public class Main extends Application {

    /**
     * Main method of the application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method called when starting the application.
     * @param primaryStage The primary stage of the application.
     * @throws Exception If any error occurs when starting the application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Abre la ventana de inicio
        new SudokuFirstStage(); // Cambia a SudokuFirstStage
    }
}
