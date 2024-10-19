package com.example.sudoku.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import com.example.sudoku.model.ISudoku;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Controller for managing the Sudoku game logic and UI interactions.
 */
public class SudokuGameController {
    @FXML
    private GridPane tableSudokuGridPane;

    @FXML
    private Button btnAyuda;

    private ISudoku sudoku;
    private int ayudasRestantes = 20; // Contador de ayudas restantes

    /**
     * Sets the Sudoku model for the controller.
     *
     * @param sudoku The Sudoku model to set.
     */
    public void setSudoku(ISudoku sudoku) {
        this.sudoku = sudoku;
        dibujarTablero();  // Dibuja el tablero con el Sudoku generado
        actualizarBotonAyuda(); // Actualiza el texto del botón de ayuda
    }

    /**
     * Initializes the controller. This method is called after the FXML file
     * has been loaded.
     */
    @FXML
    public void initialize() {
        configurarGridPane(); // Configura el GridPane para el tablero
    }

    /**
     * Configures the GridPane to display the Sudoku board.
     */
    private void configurarGridPane() {
        tableSudokuGridPane.getColumnConstraints().clear();
        tableSudokuGridPane.getRowConstraints().clear();
        for (int i = 0; i < 6; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / 6); // Configura el ancho de las columnas
            tableSudokuGridPane.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / 6); // Configura la altura de las filas
            tableSudokuGridPane.getRowConstraints().add(row);
        }
    }

    /**
     * Draws the Sudoku board based on the current state of the Sudoku model.
     */
    private void dibujarTablero() {
        tableSudokuGridPane.getChildren().clear(); // Limpia el tablero antes de dibujarlo
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                TextField txt = new TextField();
                txt.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Maximiza el tamaño del TextField
                txt.setAlignment(Pos.CENTER);
                txt.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");
                txt.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

                // Define el grosor del borde según la posición
                BorderWidths borderWidths = new BorderWidths(
                        (i % 2 == 0 && i != 0) ? 4 : 1,
                        (j % 3 == 2) ? 4 : 1,
                        (i == 5) ? 4 : 1,
                        (j == 0) ? 4 : 1
                );

                txt.setBorder(new Border(new BorderStroke(Color.MEDIUMPURPLE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, borderWidths)));

                int value = sudoku.getTableSudoku().get(i).get(j);
                if (value > 0) {
                    txt.setText(String.valueOf(value)); // Muestra el valor en la celda
                    txt.setEditable(false); // La celda no se puede editar
                    txt.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");
                    txt.setBackground(new Background(new BackgroundFill(Color.rgb(34, 34, 34), null, null)));
                } else {
                    txt.setText(""); // Celda vacía
                    txt.setEditable(true);
                    onKeyPressed(txt, i, j); // Configura el evento de teclado para la celda
                }

                tableSudokuGridPane.add(txt, j, i); // Agrega la celda al tablero
            }
        }
    }

    /**
     * Sets up the key event for the TextField to handle user input.
     *
     * @param textField The TextField to configure.
     * @param row The row index of the TextField.
     * @param col The column index of the TextField.
     */
    private void onKeyPressed(TextField textField, int row, int col) {
        textField.setOnKeyTyped(event -> {
            String character = event.getCharacter(); // Obtiene el carácter ingresado

            // Verifica si el carácter es un número válido
            if (!character.matches("[1-6]")) {
                event.consume(); // Evita la entrada de caracteres no válidos
                sudoku.mostrarAlertaError("Entrada inválida", "Solo se permiten números del 1 al 6."); // Muestra alerta de error
            } else {
                int newValue = Character.getNumericValue(character.charAt(0)); // Convierte el carácter a número
                sudoku.setNumberInTableSodoku(newValue, row, col); // Establece el número en la tabla
                textField.setText(character); // Muestra el nuevo valor en la celda

                // Verifica si el movimiento es válido
                if (!esMovimientoValido(row, col, newValue)) {
                    textField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                    sudoku.mostrarAlertaError("Número inválido", "El número ingresado viola las reglas del Sudoku."); // Muestra alerta de error
                } else {
                    textField.setBorder(new Border(new BorderStroke(Color.LIMEGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                }
            }
        });
    }

    /**
     * Checks if the move made in the specified cell is valid according to Sudoku rules.
     *
     * @param row The row index of the move.
     * @param col The column index of the move.
     * @param number The number being placed in the cell.
     * @return True if the move is valid; otherwise, false.
     */
    private boolean esMovimientoValido(int row, int col, int number) {
        // Verifica si el número ya existe en la fila
        for (int j = 0; j < 6; j++) {
            if (j != col && sudoku.getTableSudoku().get(row).get(j) == number) {
                return false; // Número no válido
            }
        }

        // Verifica si el número ya existe en la columna
        for (int i = 0; i < 6; i++) {
            if (i != row && sudoku.getTableSudoku().get(i).get(col) == number) {
                return false; // Número no válido
            }
        }

        // Verifica si el número ya existe en la subcuadrícula
        int subgridRowStart = (row / 2) * 2; // Inicializa la fila de inicio de la subcuadrícula
        int subgridColStart = (col / 3) * 3; // Inicializa la columna de inicio de la subcuadrícula

        for (int i = subgridRowStart; i < subgridRowStart + 2; i++) {
            for (int j = subgridColStart; j < subgridColStart + 3; j++) {
                if ((i != row || j != col) && sudoku.getTableSudoku().get(i).get(j) == number) {
                    return false; // Número no válido
                }
            }
        }

        return true; // Movimiento válido
    }

    /**
     * Starts a new game by resetting the Sudoku model and redrawing the board.
     */
    @FXML
    public void NuevoJuego() {
        sudoku.iniciarNuevoJuego(); // Reinicia el juego
        dibujarTablero(); // Dibuja el nuevo tablero
        ayudasRestantes = 20; // Reinicia el conteo de ayudas
        actualizarBotonAyuda(); // Actualiza el botón de ayuda
    }

    /**
     * Provides a hint for an empty cell in the Sudoku board.
     */
    @FXML
    public void mostrarAyuda() {
        if (ayudasRestantes > 0) {
            boolean ayudaDada = false; // Indica si se ha proporcionado ayuda
            List<int[]> posicionesAyuda = sudoku.getPosicionesAyuda(); // Obtiene las posiciones donde se puede dar ayuda

            for (int[] pos : posicionesAyuda) {
                int row = pos[0];
                int col = pos[1];
                int num = pos[2];

                // Verifica si la celda está vacía
                if (sudoku.getTableSudoku().get(row).get(col) == 0) {
                    TextField txt = (TextField) getNodeFromGridPane(row, col); // Obtiene el TextField correspondiente
                    if (txt != null) {
                        txt.setText(String.valueOf(num)); // Muestra el número sugerido
                        txt.setBackground(new Background(new BackgroundFill(Color.rgb(34, 34, 34), null, null)));
                        txt.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");
                        txt.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                        sudoku.setNumberInTableSodoku(num, row, col); // Establece el número en la tabla
                    }
                    ayudasRestantes--; // Disminuye el contador de ayudas
                    actualizarBotonAyuda(); // Actualiza el botón de ayuda
                    ayudaDada = true; // Indica que se ha proporcionado ayuda
                    break; // Sale del bucle
                }
            }

            // Verifica si no se pudo dar ayuda
            if (!ayudaDada) {
                sudoku.mostrarAlertaError("Sin ayudas", "No hay más celdas vacías disponibles para sugerir."); // Muestra alerta de error
            }
        } else {
            sudoku.mostrarAlertaError("Sin ayudas", "No te quedan más ayudas disponibles."); // Muestra alerta de error
        }
    }

    /**
     * Retrieves a TextField from the GridPane based on its row and column indices.
     *
     * @param row The row index of the TextField.
     * @param col The column index of the TextField.
     * @return The TextField at the specified location, or null if not found.
     */
    private TextField getNodeFromGridPane(int row, int col) {
        for (var node : tableSudokuGridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (TextField) node; // Retorna el TextField encontrado
            }
        }
        return null; // Retorna null si no se encuentra
    }

    /**
     * Updates the help button text to show the remaining number of hints.
     */
    private void actualizarBotonAyuda() {
        btnAyuda.setText("Ayuda = " + ayudasRestantes); // Actualiza el texto del botón
    }
}
