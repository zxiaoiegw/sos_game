package sprint3;

public class Main {

    public static void main(String[] args) {
            GUI gui = new GUI();
            new Board(gui); // Initialize the game board with the GUI
            gui.setVisible(true);    // Make the GUI visible
    }
}

