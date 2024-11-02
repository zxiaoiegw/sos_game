package sprint3;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestGeneralMode {
	private GeneralGameMode game;
    private final int BOARD_SIZE = 8; // Default board size for general mode

	@BeforeEach
    void setUp() {
        game = new GeneralGameMode(BOARD_SIZE);
    }

    @Test
    //Test game over when board is completely full
    void testGameOverWithFullBoard() {
        // Fill the entire board
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                assertFalse(game.isGameOver(), 
                    "Game should not be over before board is completely full");
                game.makeMove(i, j, 'O');
            }
        }
        
        // Game should be over when board is full
        assertTrue(game.isGameOver(), 
            "Game should be over when board is completely full");
    }

    @Test
    //Test game over condition with full board and multiple SOS
    void testGameOverWithFullBoardAndMultipleSOS() {
        // Fill most of the board with a pattern that creates multiple SOS
        for (int i = 0; i < BOARD_SIZE - 1; i += 3) {
            for (int j = 0; j < BOARD_SIZE - 2; j += 3) {
                game.makeMove(i, j, 'S');
                game.makeMove(i, j + 1, 'O');
                game.makeMove(i, j + 2, 'S');
            }
        }
        
        // Fill remaining spaces
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (game.makeMove(i, j, 'O')) {
                    // Move was valid (space was empty)
                    assertFalse(game.isGameOver(), 
                        "Game should not be over until last move");
                }
            }
        }
        
        // Verify game is over when board is full
        assertTrue(game.isGameOver());
        
        // Verify scores are greater than 0
        assertTrue(game.getBlueScore() > 0 || game.getRedScore() > 0,
            "At least one player should have scored");
    }
}