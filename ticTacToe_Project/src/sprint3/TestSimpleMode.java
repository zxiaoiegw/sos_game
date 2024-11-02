package sprint3;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSimpleMode {
	private SimpleGameMode game;

	@BeforeEach
    public void setUp() {
        game = new SimpleGameMode();
    }
	
    @Test
    void testGameOverWithHorizontalSOS() {
        // Game should not be over at start
        assertFalse(game.isGameOver());

        // Make moves to form SOS horizontally
        game.makeMove(0, 0, 'S');       
        game.makeMove(0, 1, 'O'); 
        game.makeMove(0, 2, 'S');
        // Game should be over after forming SOS
        assertTrue(game.isGameOver());
    }

    @Test
    // Fill board without forming SOS, the game is draw
    void testGameOverWithFullBoard() {
        // First row
        game.makeMove(0, 0, 'O');
        game.makeMove(0, 1, 'O');
        game.makeMove(0, 2, 'O');

        // Second row
        game.makeMove(1, 0, 'O');
        game.makeMove(1, 1, 'O');
        game.makeMove(1, 2, 'O');

        // Third row (except last cell)
        game.makeMove(2, 0, 'O');
        game.makeMove(2, 1, 'O');
        game.makeMove(2, 2, 'O');
        
        // Game over when board is full
        assertTrue(game.isGameOver());
    }
}