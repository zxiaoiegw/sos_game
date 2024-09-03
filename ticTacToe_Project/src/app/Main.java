package app;

public class Main {

	public static void main(String[] args) {
		GUI gui = new GUI();
		Board board = new Board(gui);
		gui.setVisible(true);
	}
}
