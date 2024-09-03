package app;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestGUI {
	
	private GUI gui;

    @BeforeEach
    void setUp() {
        gui = new GUI();
    }

    @Test
    void testGUIComponentsInitialization() {
        // Check if the New Game button is initialized correctly
        JButton btnNewGame = gui.getBtnNewGame();
        assertNotNull(btnNewGame, "New Game button should not be null");
        assertEquals("New Game", btnNewGame.getText(), "New Game button text should be 'New Game'");

        // Check if the Board Panel is initialized correctly
        JPanel boardPanel = gui.getBoardPanel();
        assertNotNull(boardPanel, "Board panel should not be null");
    }
}
