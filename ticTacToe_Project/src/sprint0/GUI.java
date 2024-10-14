package sprint0;

import javax.swing.*;
import java.awt.*;


public class GUI extends JFrame {
	private JLabel boardSizeLabel;
	private JTextField boardSizeField;
	private JButton btnReplay, btnNewGame;
	private JRadioButton simpleGame, generalGame;
	
    private JRadioButton blueHuman, blueS, blueO, blueComputer;
    private JRadioButton redHuman, redS, redO, redComputer;
    private JCheckBox recordGameCheckBox;
    private JLabel currentTurnLabel;
    private JPanel boardPanel;
	
	public GUI() {
		
		setTitle("Tic Tac Toe Game");
	    setSize(800, 600);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null);
	    setLayout(new BorderLayout());
		
		
	    // Top Panel
	    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    topPanel.add(new JLabel("SOS"));
	    simpleGame = new JRadioButton("Simple game");
	    generalGame = new JRadioButton("General game");
	    ButtonGroup topPanelGroup = new ButtonGroup();
	    topPanelGroup.add(simpleGame);
	    topPanelGroup.add(generalGame);
	    
		boardSizeLabel = new JLabel("Board size ");
        boardSizeField = new JTextField("", 2);
        topPanel.add(simpleGame);
        topPanel.add(generalGame);
        topPanel.add(boardSizeLabel);
        topPanel.add(boardSizeField);
        add(topPanel, BorderLayout.NORTH);
        
     // Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components
        
        // Blue player panel on the left
        
        JPanel bluePlayerPanel = new JPanel();
        bluePlayerPanel.setLayout(new BoxLayout(bluePlayerPanel, BoxLayout.Y_AXIS));
        bluePlayerPanel.setBorder(BorderFactory.createTitledBorder("Blue player"));
        blueHuman = new JRadioButton("Human");
        blueS = new JRadioButton("S");
        blueO = new JRadioButton("O");
        blueComputer = new JRadioButton("Computer");

        // Grouping the radio buttons so only one can be selected in each group
        ButtonGroup bluePlayerGroup = new ButtonGroup();
        bluePlayerGroup.add(blueHuman);
        bluePlayerGroup.add(blueComputer);

        ButtonGroup blueSOGroup = new ButtonGroup();
        blueSOGroup.add(blueS);
        blueSOGroup.add(blueO);

        // Create a panel for S and O options and indent them
        JPanel blueSubPanel = new JPanel();
        blueSubPanel.setLayout(new BoxLayout(blueSubPanel, BoxLayout.Y_AXIS));
        blueSubPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Indentation

        blueSubPanel.add(blueS);
        blueSubPanel.add(blueO);

        // Add components to the blue player panel
        bluePlayerPanel.add(blueHuman);
        bluePlayerPanel.add(blueSubPanel);
        bluePlayerPanel.add(blueComputer);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1; 
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(bluePlayerPanel, gbc);
        
     // Middle Panel for the Game Board
        boardPanel = new JPanel();
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        boardPanel.setPreferredSize(new Dimension(400, 400)); 
     
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8; 
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(boardPanel, gbc);
        
 
     // Red player panel on the right
        JPanel redPlayerPanel = new JPanel();
        redPlayerPanel.setLayout(new BoxLayout(redPlayerPanel, BoxLayout.Y_AXIS));
        redPlayerPanel.setBorder(BorderFactory.createTitledBorder("Red player"));
        redHuman = new JRadioButton("Human");
        redS = new JRadioButton("S");
        redO = new JRadioButton("O");
        redComputer = new JRadioButton("Computer");

        ButtonGroup redPlayerGroup = new ButtonGroup();
        redPlayerGroup.add(redHuman);
        redPlayerGroup.add(redComputer);

        ButtonGroup redSOGroup = new ButtonGroup();
        redSOGroup.add(redS);
        redSOGroup.add(redO);

        JPanel redSubPanel = new JPanel();
        redSubPanel.setLayout(new BoxLayout(redSubPanel, BoxLayout.Y_AXIS));
        redSubPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        redSubPanel.add(redS);
        redSubPanel.add(redO);

        redPlayerPanel.add(redHuman);
        redPlayerPanel.add(redSubPanel);
        redPlayerPanel.add(redComputer);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.1; 
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(redPlayerPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);
        
     // Bottom Panel for Game Controls
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        recordGameCheckBox = new JCheckBox("Record game");
        currentTurnLabel = new JLabel("Current turn: blue (or red)");
        btnReplay = new JButton("Replay");
        btnNewGame = new JButton("New Game");

        bottomPanel.add(recordGameCheckBox);
        bottomPanel.add(currentTurnLabel);
        bottomPanel.add(btnReplay);
        bottomPanel.add(btnNewGame);
        add(bottomPanel, BorderLayout.SOUTH);
	}

	// getters
	public JTextField getBoardSizeField() {
		return boardSizeField;
	}
	
	public JButton getBtnNewGame() {
		return btnNewGame;
	}
	
	public JPanel getBoardPanel() {
		return boardPanel;
	}    
}
