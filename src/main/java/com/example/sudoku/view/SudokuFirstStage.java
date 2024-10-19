package com.example.sudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the stage for the initial Sudoku game view.
 */
public class SudokuFirstStage extends Stage {
    /**
     * Initializes the Sudoku game stage by loading the FXML file.
     *
     * @throws IOException if the FXML file cannot be found or loaded.
     */
    public SudokuFirstStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sudoku/first-view.fxml"));

        // Verifica si el archivo FXML se encuentra disponible
        if (loader.getLocation() == null) {
            throw new IOException("No se pudo encontrar el archivo FXML: /com/example/sudoku/first-view.fxml");
        }

        Parent root = loader.load();
        Scene scene = new Scene(root);
        setTitle("Sudoku");
        // Set the icon for the stage
        getIcons().add(new Image(String.valueOf(getClass().getResource("/com/example/sudoku/images/IcSudoku.png"))));
        setScene(scene);
        setResizable(false);
        show();
    }
}
