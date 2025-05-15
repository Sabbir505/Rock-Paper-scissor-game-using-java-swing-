import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame {
    private JLabel playerInputLabel;
    private JLabel computerInputLabel;
    private JButton playAgainBtn;
    private JButton mainMenuBtn;
    // Add other fields as needed (e.g., for playerChoice, computerChoice)

    public GameWindow() {
        // Initialize components
        playerInputLabel = new JLabel("You chose: ");
        computerInputLabel = new JLabel("Computer chose: ");
        playAgainBtn = new JButton("Play Again");
        mainMenuBtn = new JButton("Back to Main Menu");

        // Set up layout and add components
        setLayout(new GridLayout(0, 1));
        add(playerInputLabel);
        add(computerInputLabel);
        add(playAgainBtn);
        add(mainMenuBtn);

        // Add button listeners
        playAgainBtn.addActionListener(e -> resetGame());
        mainMenuBtn.addActionListener(e -> {
            this.dispose();
            new MainMenu();
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Call this after each round to update the labels
    public void updateChoices(String playerChoice, String computerChoice) {
        playerInputLabel.setText("You chose: " + playerChoice + " " + getIcon(playerChoice));
        computerInputLabel.setText("Computer chose: " + computerChoice + " " + getIcon(computerChoice));
    }

    private String getIcon(String choice) {
        switch (choice) {
            case "Rock": return "🪨";
            case "Paper": return "📄";
            case "Scissors": return "✂️";
            default: return "";
        }
    }

    private void resetGame() {
        // Implement game reset logic here
    }
}