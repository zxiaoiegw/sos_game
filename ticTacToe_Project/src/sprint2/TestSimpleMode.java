package sprint2;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestSimpleMode {
    private SimpleGameMode gameMode;

    @Before
    public void setUp() {
        gameMode = new SimpleGameMode(); // Simple game mode uses a 3x3 board only
    }

    @Test
    public void testMakeMove() {
        // Initial player checks
        assertEquals("Initial player should be Blue ('B')", 'B', gameMode.getCurrentPlayer());
        assertEquals("Cell should be empty initially", ' ', gameMode.getCell(1, 1));

        // Blue player makes a move at (1, 1) with 'S'
        boolean sosFormed = gameMode.makeMove(1, 1, 'S');

        // Verify the move
        assertEquals("Cell should have an 'S' after the move", 'S', gameMode.getCell(1, 1));
        assertFalse("No SOS should be formed yet", sosFormed);

        // Switch player to Red
        gameMode.switchPlayer();

        // Red player makes a move at (0, 0) with 'O'
        sosFormed = gameMode.makeMove(0, 0, 'O');

        // Verify the move
        assertEquals("Cell should have an 'O' after the move", 'O', gameMode.getCell(0, 0));    
    }
}
