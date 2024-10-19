package com.example.sudoku.model;

import java.util.List;

/**
 * Interface representing the Sudoku model.
 */
public interface ISudoku {
    /**
     * Resets the Sudoku game to its initial state.
     */
    void resetGame();

    /**
     * Gets the positions where hints can be given.
     * @return List of int arrays representing positions for hints.
     */
    List<int[]> getPosicionesAyuda(); // Nuevo método agregado

    /**
     * Checks if the Sudoku puzzle is complete.
     * @return True if complete, otherwise false.
     */
    boolean isSudokuComplete();

    /**
     * Sets a number in the Sudoku puzzle table at the specified row and column.
     * @param number The number to set.
     * @param row The row index.
     * @param col The column index.
     */
    void setNumberInTableSodoku(int number, int row, int col);

    /**
     * Gets the current state of the Sudoku board.
     * @return List of lists representing the Sudoku board.
     */
    List<List<Integer>> getTableSudoku();

    /**
     * Initializes a new game.
     */
    void iniciarNuevoJuego();

    /**
     * Shows an alert for victory.
     */
    void mostrarAlertaVictoria();

    /**
     * Shows an alert for error.
     * @param titulo The title of the alert.
     * @param mensaje The message of the alert.
     */
    void mostrarAlertaError(String titulo, String mensaje);
}
