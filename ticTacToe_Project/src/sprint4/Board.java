package sprint4;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import javax.swing.Timer;

public class Board {
    private String currentPlayer; // "Blue" or "Red"
    private int boardSize;
    private GUI gui;
    private boolean isSimpleMode;
    private GameMode gameMode;
    private ComputerPlayer blueComputer;
    private ComputerPlayer redComputer;
    private Timer computerMoveTimer;
    private boolean gameInProgress;
    private BoardHelpers helpers; // Helper methods for board operations

    public Board(GUI gui) {
        this.gui = gui;
        this.gameInProgress =false;
        this.helpers = new BoardHelpers(this, gui);

        // Action listener for New Game button to generate the game board
        gui.getBtnNewGame().addActionListener(e -> {
            isSimpleMode = gui.getSimpleGame().isSelected();
            generateGameBoard();
        });
        
        // Initialize computer move timer with 600ms delay
        computerMoveTimer = new Timer(600, e -> makeComputerMove());
        computerMoveTimer.setRepeats(false);
    }

    // Method to generate the game board based on the input size
    private void generateGameBoard() {
        try {
        	gameInProgress = true;
            // First get the board size from input
            boardSize = Integer.parseInt(gui.getBoardSizeField().getText());
            
            if (isSimpleMode) {
                if (boardSize != 3) {    
                    boardSize = 3; 
                    JOptionPane.showMessageDialog(gui, "Invalid board size. Defaulting to 3x3.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                gameMode = new SimpleGameMode();    
            } else {
                // Changed condition for general mode: must be between 4-8
                if (boardSize < 4 || boardSize > 8) {
                    boardSize = 8; // Default to 8x8 if input is invalid
                    JOptionPane.showMessageDialog(gui, "Invalid board size. Board size must be between 4-8. Defaulting to 8x8.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                gameMode = new GeneralGameMode(boardSize);
            }

            currentPlayer = "Blue"; // Blue player always starts first
            initializeComputerPlayers();

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
            
            // Start computer move if Blue is computer
            if (gui.getBlueComputer().isSelected()) {
            	computerMoveTimer.start();
            }
            	
        } catch (NumberFormatException ex) {
            // Handle invalid board size input
            if (isSimpleMode) {
                boardSize = 3;  
                JOptionPane.showMessageDialog(gui, "Invalid board size. Defaulting to 3x3.", "Error", JOptionPane.ERROR_MESSAGE);
                gameMode = new SimpleGameMode();
            } else {
                boardSize = 8;  
                JOptionPane.showMessageDialog(gui, "Invalid board size. Board size must be between 4-8. Defaulting to 8x8.", "Error", JOptionPane.ERROR_MESSAGE);
                gameMode = new GeneralGameMode(boardSize);
            }
        }
    }
    
    // Initializes computer players based on user selection
    private void initializeComputerPlayers() {
    	if (gui.getBlueComputer().isSelected()) {
    		blueComputer = new ComputerPlayer('B', gameMode, isSimpleMode);
    	}
    	if (gui.getRedComputer().isSelected()) {
    		redComputer = new ComputerPlayer('R', gameMode, isSimpleMode);
    	}
    }
    
    // Makes a move for the computer player when it's their turn
    private void makeComputerMove() {
        if (!gameInProgress) return;

        boolean isBlueComputer = currentPlayer.equals("Blue") && gui.getBlueComputer().isSelected();
        boolean isRedComputer = currentPlayer.equals("Red") && gui.getRedComputer().isSelected();

        if (isBlueComputer || isRedComputer) {
            ComputerPlayer currentComputerPlayer = isBlueComputer ? blueComputer : redComputer;
            char[][] boardState = getCurrentBoardState();
            
            ComputerPlayer.Move move = currentComputerPlayer.makeMove(boardState);
            if (move != null) {
                GameButton button = helpers.getButtonAt(move.row, move.col);
                if (button != null) {
                    button.setText(String.valueOf(move.letter));
                    button.setForeground(helpers.currentPlayerColor());
                    processMove(button, move.row, move.col, move.letter);
                }
            }
        }
    }
    
    // Retrieves the current state of the game board
    private char[][] getCurrentBoardState() {
    	char[][] board = new char[boardSize][boardSize];
    	for(int i =0; i < boardSize; i++) {
    		for (int j = 0; j < boardSize; j++) {
    			GameButton button = helpers.getButtonAt(i, j);
    			board[i][j] = button.getText().charAt(0);
    		}
    	}
    	return board;
    }
    
    // Handles moves when a cell is clicked by human player
    private void handleButtonClick(GameButton button, int row, int col) {
        if (!gameInProgress) return;
        
        // Check if it's a human player's turn
        boolean isHumanTurn = (currentPlayer.equals("Blue") && gui.getBlueHuman().isSelected()) ||
                             (currentPlayer.equals("Red") && gui.getRedHuman().isSelected());
        
        if (isHumanTurn && button.getText().equals(" ")) {
            char selectedLetter = helpers.getCurrentSelectedLetter();
            button.setText(String.valueOf(selectedLetter));
            button.setForeground(helpers.currentPlayerColor());
            processMove(button, row, col, selectedLetter);
        }
    }

    // Processes a move made by a player, updating the game state, handles SOS formation and turn switching
    private void processMove(GameButton button, int row, int col, char letter) {
        boolean sosFormed = gameMode.makeMove(row, col, letter);
        List<GameModeBase.SOSSequence> sosSequences = gameMode.getLastMoveSequences();

        if (sosFormed) {
            helpers.drawSOSLines(sosSequences);
            
            if (!isSimpleMode) {
                helpers.updateScores();
                gui.updateTurnLabel(currentPlayer + " (Another turn)");
            } else {
                JOptionPane.showMessageDialog(gui, "Player " + currentPlayer + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                endGame();
                return;
            }
        } else {
            helpers.switchPlayer();
        }

        gui.updateTurnLabel(currentPlayer);

        if (gameMode.isGameOver()) {
            handleGameOver();
        } else if (isComputerTurn()) {
            computerMoveTimer.start();
        }
    }

    // Handles game over conditions and display the winner or draw
    private void handleGameOver() {
        if (isSimpleMode) {
            JOptionPane.showMessageDialog(gui, "Game Over! It's a draw.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int blueScore = gameMode.getBlueScore();
            int redScore = gameMode.getRedScore();
            String winner = determineWinner(blueScore, redScore);
            JOptionPane.showMessageDialog(gui, winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
        endGame();
    }

    // Determines the winner based on the scores in general game mode
    private String determineWinner(int blueScore, int redScore) {
        if (blueScore > redScore) {
            return "Blue wins with " + blueScore + " points!";
        } else if (redScore > blueScore) {
            return "Red wins with " + redScore + " points!";
        } else {
            return "It's a draw! Both players scored " + blueScore + " points.";
        }
    }

    // Checks if current turn belongs to a computer player
    private boolean isComputerTurn() {
        return (currentPlayer.equals("Blue") && gui.getBlueComputer().isSelected()) ||
               (currentPlayer.equals("Red") && gui.getRedComputer().isSelected());
    }

    // Ends the game and disabling the game board
    private void endGame() {
        gameInProgress = false;
        computerMoveTimer.stop();
        helpers.disableBoard();
    }
    
    // Getters and setters needed by BoardHelpers
    public ComputerPlayer getBlueComputer() {
        return blueComputer;
    }

    public ComputerPlayer getRedComputer() {
        return redComputer;
    }
    
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String player) {
        this.currentPlayer = player;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int size) {
        this.boardSize = size;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode mode) {
        this.gameMode = mode;
    }
}