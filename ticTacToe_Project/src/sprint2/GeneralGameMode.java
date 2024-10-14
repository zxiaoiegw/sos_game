package sprint2;

public class GeneralGameMode extends GameModeBase {
    private int blueScore; // Score for the blue player
    private int redScore; // Score for the red player

    public GeneralGameMode(int boardSize) {
        super(boardSize);
        this.blueScore = 0;
        this.redScore = 0;
    }

    @Override
    protected void handleSOSFound() {
        // Update the score for the current player
        if (currentPlayer == 'B') {
            blueScore++;
        } else {
            redScore++;
        }
        // After forming an SOS, switch the player
        switchPlayer();
    }

    @Override
    public int getBlueScore() {
        return blueScore;
    }

    @Override
    public int getRedScore() {
        return redScore;
    }
}
