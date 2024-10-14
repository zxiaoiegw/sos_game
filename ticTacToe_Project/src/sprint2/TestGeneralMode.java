package sprint2;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestGeneralMode {
    private GeneralGameMode gameMode;

    @Before
    public void setUp() {
        gameMode = new GeneralGameMode(8); // General game mode with an 3*3 ... up to 8*8 board
    }

    @Test
    public void testMakeMove() {
        // Initial player checks
        assertEquals("Initial player should be Blue ('B')", 'B', gameMode.getCurrentPlayer());
        assertEquals("Cell should be empty initially", ' ', gameMode.getCell(4, 4));

        // Blue player makes a move at (4, 4) with 'O'
        boolean sosFormed = gameMode.makeMove(4, 4, 'O');

        // Verify the move
        assertEquals("Cell should have an 'O' after the move", 'O', gameMode.getCell(4, 4));
        assertFalse("No SOS should be formed", sosFormed);

        // Verify scores remain unchanged
        assertEquals("Blue score should be 0", 0, gameMode.getBlueScore());
        assertEquals("Red score should be 0", 0, gameMode.getRedScore());

        // Switch player to Red
        gameMode.switchPlayer();

        // Red player makes a move at (3, 3) with 'S'
        sosFormed = gameMode.makeMove(3, 3, 'S');

        // Verify the move
        assertEquals("Cell should have an 'S' after the move", 'S', gameMode.getCell(3, 3));

        // Verify scores remain unchanged
        assertEquals("Red score should still be 0", 0, gameMode.getRedScore());
    }
}
