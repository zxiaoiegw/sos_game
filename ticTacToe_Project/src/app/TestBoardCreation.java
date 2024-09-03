package app;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBoardCreation {
	private Board board;
    private GUI gui;
    
    @BeforeEach
    public void setUp() {
        // GUI and Board instances
        gui = new GUI();
        board = new Board(gui);
    }


	@Test
	public void test() {
		 // Set the board size to 4x4 via GUI
        gui.getBoardSizeField().setText("4");
        
        // Simulate clicking the New Game button
        gui.getBtnNewGame().doClick();
        
     // Check if the board array is correctly initialized with the 4x4 size
        assertEquals(4, board.getBoardArray().length);
        assertEquals(4, board.getBoardArray()[0].length);
		}
}
