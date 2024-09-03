package app;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board {
	private char[][] board;
    private char currentPlayer;
    private int boardSize;
    private GUI gui;
    
    public Board(GUI gui) {
    	this.gui = gui;
        
        // Action listener for New Game button to generate the game board
        gui.getBtnNewGame().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateGameBoard();
            }
        });
    }
        
     // Method to generate the game board based on the input size
        private void generateGameBoard() {
            // Get the board size from the text field
            try {
                boardSize = Integer.parseInt(gui.getBoardSizeField().getText());
                if(boardSize <= 2) {
                	throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
            		boardSize = 3; // Default to 3x3 if input is invalid
                    JOptionPane.showMessageDialog(gui, "Invalid board size. Defaulting to 3x3.", "Error", JOptionPane.ERROR_MESSAGE);
            	
            }
            
         // Initialize the board array
            board = new char[boardSize][boardSize];
            currentPlayer = 'S'; // Set the initial player

         // Inside the generateGameBoard() method in the Board class
            gui.getBoardPanel().removeAll(); // Clear any previous board content
            gui.getBoardPanel().setLayout(new GridLayout(boardSize, boardSize));
            
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    JButton button = new JButton(" ");
                    button.setFont(new Font("Arial", Font.BOLD, 35));
                    final int row = i;
                    final int col = j;
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            button.setText(String.valueOf(currentPlayer));
                            board[row][col] = currentPlayer;
                            // Switch player
                            currentPlayer = (currentPlayer == 'S') ? 'O' : 'S';
                        }
                    });
                    gui.getBoardPanel().add(button);
                }
            }
            
            // Refresh the panel to display the new board
            gui.getBoardPanel().revalidate();
            gui.getBoardPanel().repaint();
        }
        
     // Getter method to access the board array
        public char[][] getBoardArray() {
            return board;
        }

		public Integer getBoardSizeField() {
			return boardSize;
		}  
}
