package com.example.sudoku.model;

import javafx.scene.control.Alert;
import java.util.*;

/**
 * Class representing the Sudoku game logic.
 */
public class Sudoku implements ISudoku {
    private List<List<Integer>> tableSudoku;
    private List<int[]> posicionesAyuda; // Almacena pares [fila, columna, valor]

    /**
     * Initializes a new Sudoku game with an empty board.
     */
    public Sudoku() {
        this.tableSudoku = new ArrayList<>();
        resetGame();
    }

    /**
     * Checks if the Sudoku puzzle is complete.
     *
     * @return true if the Sudoku is complete, false otherwise.
     */
    @Override
    public boolean isSudokuComplete() {
        return validarFilas() && validarColumnas() && validarBloques();
    }

    /**
     * Validates if a number can be placed in a specific position.
     *
     * @param row the row index
     * @param col the column index
     * @param num the number to check
     * @return true if the number is valid, false otherwise.
     */
    public boolean esNumeroValido(int row, int col, int num) {
        // Verificar fila
        for (int i = 0; i < 6; i++) {
            if (tableSudoku.get(row).get(i) == num && i != col) {
                return false;
            }
        }
        // Verificar columna
        for (int i = 0; i < 6; i++) {
            if (tableSudoku.get(i).get(col) == num && i != row) {
                return false;
            }
        }
        // Verificar bloque
        int blockRowStart = (row / 2) * 2;
        int blockColStart = (col / 3) * 3;
        for (int i = blockRowStart; i < blockRowStart + 2; i++) {
            for (int j = blockColStart; j < blockColStart + 3; j++) {
                if (tableSudoku.get(i).get(j) == num && (i != row || j != col)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Starts a new game by resetting the board and generating a Sudoku puzzle.
     */
    public void iniciarJuego() {
        resetGame();               // Reinicia el tablero
        generarSudokuCompleto();    // Genera un Sudoku completo
        eliminarNumeros();          // Elimina algunos números para crear el puzzle
    }

    /**
     * Initializes a new game.
     */
    @Override
    public void iniciarNuevoJuego() {
        resetGame();
        generarSudokuCompleto(); // Generar un Sudoku válido completo
        eliminarNumeros(); // Ajustar para dejar exactamente 2 celdas llenas por subcuadrícula
    }

    /**
     * Generates a complete Sudoku board and stores help positions.
     *
     * @return true if the Sudoku was generated successfully, false otherwise.
     */
    private boolean generarSudokuCompleto() {
        posicionesAyuda = new ArrayList<>(); // Inicializa la lista de posiciones de ayuda
        boolean resultado = resolverSudoku(0, 0); // Resolver el Sudoku

        // Almacenar las posiciones y valores
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                if (tableSudoku.get(row).get(col) != 0) {
                    posicionesAyuda.add(new int[]{row, col, tableSudoku.get(row).get(col)});
                }
            }
        }
        return resultado;
    }

    /**
     * Recursive method to solve the Sudoku puzzle.
     *
     * @param row the current row
     * @param col the current column
     * @return true if a solution is found, false otherwise.
     */
    private boolean resolverSudoku(int row, int col) {
        if (row == 6) {
            return true; // Se encontró una solución
        }

        if (col == 6) {
            return resolverSudoku(row + 1, 0); // Mover a la siguiente fila
        }

        if (tableSudoku.get(row).get(col) != 0) {
            return resolverSudoku(row, col + 1); // Saltar celdas llenas
        }

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers); // Barajar números

        for (int num : numbers) {
            if (esNumeroValido(row, col, num)) {
                tableSudoku.get(row).set(col, num); // Coloca el número
                if (resolverSudoku(row, col + 1)) { // Llama recursivamente
                    return true;
                }
                tableSudoku.get(row).set(col, 0); // Deshacer si no funciona
            }
        }
        return false; // No se pudo colocar ningún número
    }

    /**
     * Removes numbers from the Sudoku board to create a puzzle.
     */
    private void eliminarNumeros() {
        for (int blockRow = 0; blockRow < 6; blockRow += 2) {
            for (int blockCol = 0; blockCol < 6; blockCol += 3) {
                int filledCells = 0; // Contador para las celdas llenas en la subcuadrícula
                List<int[]> positions = new ArrayList<>(); // Lista para almacenar las posiciones de celdas llenas

                // Recolectar celdas llenas en la subcuadrícula
                for (int row = blockRow; row < blockRow + 2; row++) {
                    for (int col = blockCol; col < blockCol + 3; col++) {
                        if (tableSudoku.get(row).get(col) != 0) {
                            positions.add(new int[]{row, col});
                            filledCells++;
                        }
                    }
                }

                // Si hay más de 2 celdas llenas, eliminar celdas hasta que queden 2
                while (filledCells > 2) {
                    int randomIndex = new Random().nextInt(positions.size());
                    int[] pos = positions.get(randomIndex);
                    tableSudoku.get(pos[0]).set(pos[1], 0); // Eliminar el número
                    positions.remove(randomIndex); // Quitar la posición eliminada
                    filledCells--; // Decrementar el contador
                }
            }
        }
    }

    /**
     * Checks if the Sudoku has a unique solution.
     *
     * @return true if there is a unique solution, false otherwise.
     */
    private boolean tieneSolucionUnica() {
        int soluciones = contarSoluciones();
        return soluciones == 1; // Solo debe haber una solución
    }

    /**
     * Counts the number of solutions for the current Sudoku configuration.
     *
     * @return the number of solutions found.
     */
    private int contarSoluciones() {
        return contarSolucionesRecursivo(0, 0);
    }

    /**
     * Recursive method to count the number of solutions.
     *
     * @param row the current row
     * @param col the current column
     * @return the number of solutions found.
     */
    private int contarSolucionesRecursivo(int row, int col) {
        if (row == 6) {
            return 1; // Se encontró una solución
        }

        if (col == 6) {
            return contarSolucionesRecursivo(row + 1, 0); // Mover a la siguiente fila
        }

        if (tableSudoku.get(row).get(col) != 0) {
            return contarSolucionesRecursivo(row, col + 1); // Saltar celdas llenas
        }

        int totalSoluciones = 0;
        for (int num = 1; num <= 6; num++) {
            if (esNumeroValido(row, col, num)) {
                tableSudoku.get(row).set(col, num); // Coloca el número
                totalSoluciones += contarSolucionesRecursivo(row, col + 1); // Llama recursivamente
                tableSudoku.get(row).set(col, 0); // Deshacer
            }
        }
        return totalSoluciones; // Devuelve el total de soluciones encontradas
    }

    /**
     * Sets a number in the Sudoku table at the specified position.
     *
     * @param number the number to set
     * @param row the row index
     * @param col the column index
     */
    @Override
    public void setNumberInTableSodoku(int number, int row, int col) {
        // Asegúrate de que `number` es un valor válido (1-6) y que la posición (row, col) es válida.
        if (row >= 0 && row < 6 && col >= 0 && col < 6 && number >= 1 && number <= 6) {
            tableSudoku.get(row).set(col, number);

            // Comprobar si el Sudoku está completo tras cada número ingresado
            if (isSudokuComplete()) {
                mostrarAlertaVictoria();
            }
        } else {
            mostrarAlertaError("Error", "Número o posición no válida.");
        }
    }

    /**
     * Gets the current Sudoku table.
     *
     * @return the current table as a list of lists of integers.
     */
    @Override
    public List<List<Integer>> getTableSudoku() {
        return tableSudoku;
    }

    /**
     * Resets the game board to an empty state.
     */
    @Override
    public void resetGame() {
        tableSudoku.clear();
        for (int i = 0; i < 6; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                row.add(0);
            }
            tableSudoku.add(row);
        }
    }

    /**
     * Validates all rows in the Sudoku table.
     *
     * @return true if all rows are valid, false otherwise.
     */
    private boolean validarFilas() {
        for (int row = 0; row < 6; row++) {
            Set<Integer> seen = new HashSet<>();
            for (int col = 0; col < 6; col++) {
                int num = tableSudoku.get(row).get(col);
                if (num == 0 || !seen.add(num)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Validates all columns in the Sudoku table.
     *
     * @return true if all columns are valid, false otherwise.
     */
    private boolean validarColumnas() {
        for (int col = 0; col < 6; col++) {
            Set<Integer> seen = new HashSet<>();
            for (int row = 0; row < 6; row++) {
                int num = tableSudoku.get(row).get(col);
                if (num == 0 || !seen.add(num)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Validates all blocks in the Sudoku table.
     *
     * @return true if all blocks are valid, false otherwise.
     */
    private boolean validarBloques() {
        for (int blockRow = 0; blockRow < 6; blockRow += 2) {
            for (int blockCol = 0; blockCol < 6; blockCol += 3) {
                Set<Integer> seen = new HashSet<>();
                for (int row = blockRow; row < blockRow + 2; row++) {
                    for (int col = blockCol; col < blockCol + 3; col++) {
                        int num = tableSudoku.get(row).get(col);
                        if (num == 0 || !seen.add(num)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Displays a victory alert when the Sudoku is completed.
     */
    @Override
    public void mostrarAlertaVictoria() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Victoria!");
        alert.setHeaderText("¡Felicidades! Has completado el Sudoku correctamente.");
        alert.showAndWait();
    }

    /**
     * Gets the help positions stored in the Sudoku instance.
     *
     * @return the list of help positions.
     */
    public List<int[]> getPosicionesAyuda() {
        return posicionesAyuda;
    }

    /**
     * Displays an error alert with a specific title and message.
     *
     * @param titulo the title of the alert
     * @param mensaje the message to display
     */
    @Override
    public void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.showAndWait();
    }
}
