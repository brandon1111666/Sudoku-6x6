package com.example.sudoku.view;

import com.example.sudoku.controller.SudokuGameController;
import com.example.sudoku.model.ISudoku;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the stage for the Sudoku game view.
 */
public class SudokuGameStage extends Stage {
    private SudokuGameController controller; // Agregar controlador

    /**
     * Initializes the Sudoku game stage by loading the FXML file.
     *
     * @throws IOException if the FXML file cannot be found or loaded.
     */
    public SudokuGameStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sudoku/sudoku-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController(); // Obtener el controlador
        Scene scene = new Scene(root);
        setTitle("Sudoku");
        // Set the icon for the stage
        getIcons().add(new Image(String.valueOf(getClass().getResource("/com/example/sudoku/images/IcSudoku.png"))));
        setScene(scene);
        setResizable(false);
        show();
    }

    /**
     * Sets the Sudoku object to be used in the game.
     *
     * @param sudoku the Sudoku instance to set in the controller.
     */
    public void setSudoku(ISudoku sudoku) {
        controller.setSudoku(sudoku); // Llamar al método en el controlador
    }
}
