package sprint3;

import java.awt.*;
import javax.swing.*;
import java.util.List;


public class Board {
    private String currentPlayer; // "Blue" or "Red"
    private int boardSize;
    private GUI gui;
    private boolean isSimpleMode;
    private GameMode gameMode;

    public Board(GUI gui) {
        this.gui = gui;

        // Action listener for New Game button to generate the game board
        gui.getBtnNewGame().addActionListener(e -> {
            isSimpleMode = gui.getSimpleGame().isSelected();
            generateGameBoard();
        });
    }

    // Method to generate the game board based on the input size
    private void generateGameBoard() {
        try {
            // First get the board size from input
            boardSize = Integer.parseInt(gui.getBoardSizeField().getText());
            
            if (isSimpleMode) {
                if (boardSize != 3) {    
                    boardSize = 3; 
                    JOptionPane.showMessageDialog(gui, "Invalid board size. Defaulting to 3x3.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                gameMode = new SimpleGameMode();    
            } else {
                // Changed condition for general mode: must be between 4 and 8 inclusive
                if (boardSize < 4 || boardSize > 8) {
                    boardSize = 8; // Default to 8x8 if input is invalid
                    JOptionPane.showMessageDialog(gui, "Invalid board size. Board size must be between 4-8. Defaulting to 8x8.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                gameMode = new GeneralGameMode(boardSize);
            }

            currentPlayer = "Blue"; // Set the initial player

            // Generate the game board on the GUI
            gui.getBoardPanel().removeAll(); // Clear any previous board content
            gui.getBoardPanel().setLayout(new GridLayout(boardSize, boardSize));

            // Create buttons for each cell in the board
            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    GameButton button = new GameButton(" ");
                    button.setFont(new Font("Arial", Font.BOLD, 35));
                    int finalRow = row;
                    int finalCol = col;

                    // Add action listener to handle button click
                    button.addActionListener(e -> handleButtonClick(button, finalRow, finalCol));
                    gui.getBoardPanel().add(button);
                }
            }

            // Refresh the panel to display the new board
            gui.getBoardPanel().revalidate();
            gui.getBoardPanel().repaint();

            // Update the turn label
            gui.updateTurnLabel(currentPlayer);

            // Reset scores
            gui.updateScores(0, 0);
        } catch (NumberFormatException ex) {
            // Handle case where input is not a number
            if (isSimpleMode) {
                boardSize = 3;  // Default for simple mode
                JOptionPane.showMessageDialog(gui, "Invalid board size. Defaulting to 3x3.", "Error", JOptionPane.ERROR_MESSAGE);
                gameMode = new SimpleGameMode();
            } else {
                boardSize = 8;  // Default for general mode
                JOptionPane.showMessageDialog(gui, "Invalid board size. Board size must be between 4x4 and 8x8. Defaulting to 8x8.", "Error", JOptionPane.ERROR_MESSAGE);
                gameMode = new GeneralGameMode(boardSize);
            }
            
            // Generate the board with default size
            currentPlayer = "Blue";
            gui.getBoardPanel().removeAll();
            gui.getBoardPanel().setLayout(new GridLayout(boardSize, boardSize));

            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    GameButton button = new GameButton(" ");
                    button.setFont(new Font("Arial", Font.BOLD, 35));
                    int finalRow = row;
                    int finalCol = col;
                    button.addActionListener(e -> handleButtonClick(button, finalRow, finalCol));
                    gui.getBoardPanel().add(button);
                }
            }

            gui.getBoardPanel().revalidate();
            gui.getBoardPanel().repaint();
            gui.updateTurnLabel(currentPlayer);
            gui.updateScores(0, 0);
        }
    }

    // Handle button click logic
    private void handleButtonClick(GameButton button, int row, int col) {
        // Only place a move if the spot is empty
        if (button.getText().equals(" ")) {
            char selectedLetter = getCurrentSelectedLetter();

            // Update the button with the letter
            button.setText(String.valueOf(selectedLetter));
            button.setForeground(currentPlayerColor());

            // Make the move in the game mode
            boolean sosFormed = gameMode.makeMove(row, col, selectedLetter);

            // Get the SOS sequences formed in the last move
            List<GameModeBase.SOSSequence> sosSequences = gameMode.getLastMoveSequences();

            // Check for SOS sequences and draw lines
            if (sosFormed) {
                // Draw lines over the SOS sequences
                drawSOSLines(sosSequences);

                // In general mode, update scores
                if (!isSimpleMode) {
                    int blueScore = gameMode.getBlueScore();
                    int redScore = gameMode.getRedScore();
                    gui.updateScores(blueScore, redScore);

                    // In general mode, the player gets another turn after make score
                    gui.updateTurnLabel(currentPlayer + " (Another turn)");
//                    return;
                } else {
                    // In simple mode, declare the winner immediately once SOS is formed
                    JOptionPane.showMessageDialog(gui, "Player " + currentPlayer + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    disableBoard(); // Disable further input
                    return;
                }
            } else {
                // Switch turn if no SOS was formed
                switchPlayer();
            }
            // Update the turn label
            gui.updateTurnLabel(currentPlayer);

            // Check for game over conditions 
            if (gameMode.isGameOver()) {
                if (isSimpleMode) {
                	// In simple mode, declare a draw
                    JOptionPane.showMessageDialog(gui, "Game Over! It's a draw.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else {
                	// In general mode, determine the winner based on scores
                    int blueScore = gameMode.getBlueScore();
                    int redScore = gameMode.getRedScore();
                    String winner;
                    if (blueScore > redScore) {
                        winner = "Blue wins!";
                    } else if (redScore > blueScore) {
                        winner = "Red wins!";
                    } else {
                        winner = "It's a draw!";
                    }
                    JOptionPane.showMessageDialog(gui, winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
                disableBoard();
            }
        }
    }

    // Helper method to switch players
    private void switchPlayer() {
        currentPlayer = currentPlayer.equals("Blue") ? "Red" : "Blue";
    }

    // Helper method to get the current selected letter, either 'S' or 'O'
    private char getCurrentSelectedLetter() {
        char selectedLetter;
        if (currentPlayer.equals("Blue")) {
            selectedLetter = gui.getBlueS().isSelected() ? 'S' : 'O';
        } else {
            selectedLetter = gui.getRedS().isSelected() ? 'S' : 'O';
        }
        System.out.println("Current Player: " + currentPlayer + ", Selected Letter: " + selectedLetter);
        return selectedLetter;
    }

    // Helper method to determine the current player's color
    private Color currentPlayerColor() {
        return currentPlayer.equals("Blue") ? Color.BLUE : Color.RED;
    }

    // Draw lines over the SOS sequences
    private void drawSOSLines(List<GameModeBase.SOSSequence> sequences) {
        for (GameModeBase.SOSSequence sequence : sequences) {
            GameButton firstButton = getButtonAt(sequence.row1, sequence.col1);
            GameButton secondButton = getButtonAt(sequence.row2, sequence.col2);
            GameButton thirdButton = getButtonAt(sequence.row3, sequence.col3);

            if (firstButton != null && secondButton != null && thirdButton != null) {
                Color color = currentPlayerColor();
                // Create a line object with the current player's color and the direction of SOS
                GameButton.Line line = new GameButton.Line(color, sequence.direction);
                firstButton.addLine(line);
                secondButton.addLine(line);
                thirdButton.addLine(line);

                // Repaint the buttons to show the lines
                firstButton.repaint();
                secondButton.repaint();
                thirdButton.repaint();
            }
        }
    }
    
    // Retrieve the button at a specific row and column
    private GameButton getButtonAt(int row, int col) {
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            int index = row * boardSize + col;
            Component component = gui.getBoardPanel().getComponent(index);
            if (component instanceof GameButton) {
                return (GameButton) component;
            }
        }
        return null;
    }

    // Disable the game board to prevent further moves when game is over
    private void disableBoard() {
        Component[] components = gui.getBoardPanel().getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                component.setEnabled(false);
            }
        }
    }
}
