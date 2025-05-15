import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class RockPaperScissors {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}

class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Rock-Paper-Scissors");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        JButton btnPvC = new JButton("Player vs Computer");
        JButton btnPvP = new JButton("Player vs Player");

        btnPvC.addActionListener(e -> {
            playClickSound();
            new GameModeFrame("Player vs Computer");
        });
        btnPvP.addActionListener(e -> {
            playClickSound();
            new GameModeFrame("Player vs Player");
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(btnPvC, gbc);
        gbc.gridy = 1;
        add(btnPvP, gbc);

        setVisible(true);
    }
    private void playClickSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("mouse-click-153941.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class GameModeFrame extends JFrame {
    private JLabel label;
    private Timer countdownTimer;
    private int countdown;
    private boolean inputLocked = false;
    private String playerChoice = null;
    private String computerChoice = null;
    private final String mode;
    // PvP-specific fields
    private String p1Input = null;
    private String p2Input = null;
    private boolean p1Cheated = false;
    private boolean p2Cheated = false;
    private int totalRounds;
    private int currentRound;
    private int p1Score;
    private int p2Score;
    private java.util.List<String> playerMoveHistory = new java.util.ArrayList<>();
    private void playClickSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("mouse-click-153941.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private String getImageFileName(String choice) {
        switch (choice) {
            case "Rock": return "rock.png";
            case "Paper": return "paper.png";
            case "Scissors": return "scissor.png"; // singular file name
            default: return null;
        }
    }
    // Add this helper method inside GameModeFrame:
private ImageIcon centeredScaledIcon(String fileName, int width, int height) {
    ImageIcon icon = new ImageIcon(fileName);
    Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(img);
}
    private JLabel choiceImageLabel = new JLabel();

    public GameModeFrame(String mode) {
        this.mode = mode;
        setTitle(mode);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        label = new JLabel("", SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // Add vertical and horizontal space
        add(label, BorderLayout.NORTH);
        add(choiceImageLabel, BorderLayout.CENTER);
        setVisible(true);

        // Add KeyListener only once
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (mode.equals("Player vs Computer")) {
                    handlePvCKey(e);
                } else if (mode.equals("Player vs Player")) {
                    handlePvPKey(e);
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();

        // Ask for round selection
        Object[] options = {"Best of 3", "Best of 5"};
        int n = JOptionPane.showOptionDialog(this, "Choose number of rounds:", "Round Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (n == 0 || n == 1) {
            playClickSound();
        }
        if (n == 0) totalRounds = 3;
        else totalRounds = 5;
        currentRound = 1;
        p1Score = 0;
        p2Score = 0;

        if (mode.equals("Player vs Computer")) {
            startPvCMode();
        } else if (mode.equals("Player vs Player")) {
            startPvPMode();
        } else {
            label.setText("This is the " + mode + " screen.");
        }
    }

    private void startPvCMode() {
        countdown = 3;
        inputLocked = true;
        label.setText(roundStatus() + "\nGet ready! " + countdown);
        countdownTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                countdown--;
                if (countdown > 0) {
                    label.setText(roundStatus() + "\nGet ready! " + countdown);
                } else {
                    countdownTimer.stop();
                    label.setText(roundStatus() + "\nPress R, P, or S!");
                    inputLocked = false;
                }
            }
        });
        countdownTimer.setInitialDelay(0);
        countdownTimer.start();
        // Removed duplicate addKeyListener here
        setFocusable(true);
        requestFocusInWindow();
    }

    private void startPvPMode() {
        countdown = 3;
        inputLocked = true;
        label.setText(roundStatus() + "\nGet ready! " + countdown);
        // Reset state variables for each round
        p1Input = null;
        p2Input = null;
        p1Cheated = false;
        p2Cheated = false;
        countdownTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                countdown--;
                if (countdown > 0) {
                    label.setText(roundStatus() + "\nGet ready! " + countdown);
                } else {
                    countdownTimer.stop();
                    label.setText(roundStatus() + "\nPlayer 1: A/S/D | Player 2: J/K/L");
                    inputLocked = false;
                }
            }
        });
        countdownTimer.setInitialDelay(0);
        countdownTimer.start();
        // Removed duplicate addKeyListener here
        setFocusable(true);
        requestFocusInWindow();
    }

    private void showRoundResult(String msg) {
        label.setText(roundStatus() + "\n" + msg);
        int imgWidth = 80;
        int imgHeight = 80;
        if (mode.equals("Player vs Computer") && playerChoice != null && computerChoice != null) {
            JPanel panel = new JPanel(new GridLayout(1, 2));
            panel.setOpaque(false);
            JLabel playerLabel = new JLabel(centeredScaledIcon(getImageFileName(playerChoice), imgWidth, imgHeight));
            JLabel computerLabel = new JLabel(centeredScaledIcon(getImageFileName(computerChoice), imgWidth, imgHeight));
            playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            computerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(playerLabel);
            panel.add(computerLabel);
            choiceImageLabel.setIcon(null);
            choiceImageLabel.setText("");
            choiceImageLabel.setLayout(new BorderLayout());
            choiceImageLabel.removeAll();
            choiceImageLabel.add(panel, BorderLayout.CENTER);
            choiceImageLabel.revalidate();
            choiceImageLabel.repaint();
        } else if (mode.equals("Player vs Player") && p1Input != null && p2Input != null) {
            JPanel panel = new JPanel(new GridLayout(1, 2));
            panel.setOpaque(false);
            JLabel p1Label = new JLabel(centeredScaledIcon(getImageFileName(p1Input), imgWidth, imgHeight));
            JLabel p2Label = new JLabel(centeredScaledIcon(getImageFileName(p2Input), imgWidth, imgHeight));
            p1Label.setHorizontalAlignment(SwingConstants.CENTER);
            p2Label.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(p1Label);
            panel.add(p2Label);
            choiceImageLabel.setIcon(null);
            choiceImageLabel.setText("");
            choiceImageLabel.setLayout(new BorderLayout());
            choiceImageLabel.removeAll();
            choiceImageLabel.add(panel, BorderLayout.CENTER);
            choiceImageLabel.revalidate();
            choiceImageLabel.repaint();
        } else {
            choiceImageLabel.setIcon(null);
            choiceImageLabel.setText("");
            choiceImageLabel.removeAll();
        }
        Timer pause = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((Timer)e.getSource()).stop();
                if (p1Score > totalRounds/2 || p2Score > totalRounds/2 || currentRound == totalRounds) {
                    String winner;
                    boolean isDraw = p1Score == p2Score;
                    boolean playerWon = false;
                    boolean playerLost = false;
                    boolean p1Won = false;
                    boolean p2Won = false;
                    if (p1Score > p2Score) {
                        winner = (mode.equals("Player vs Computer") ? "You win the game!" : "Player 1 wins the game!");
                        if (mode.equals("Player vs Computer")) playerWon = true;
                        if (mode.equals("Player vs Player")) p1Won = true;
                    } else if (p2Score > p1Score) {
                        winner = (mode.equals("Player vs Computer") ? "Computer wins the game!" : "Player 2 wins the game!");
                        if (mode.equals("Player vs Computer")) playerLost = true;
                        if (mode.equals("Player vs Player")) p2Won = true;
                    } else {
                        winner = "It's a draw!";
                    }
                    label.setText(roundStatus() + "\n" + winner);
                    if (playerWon) {
                        playWinSound();
                    } else if (playerLost) {
                        playLoseSound();
                    } else if (p1Won) {
                        playWinSound(); // Player 1 wins in PvP
                    } else if (p2Won) {
                        playLoseSound(); // Player 2 wins in PvP
                    } else if (isDraw) {
                        playDrawSound(); // Optional: add a draw sound
                    }
                } else {
                    currentRound++;
                    if (mode.equals("Player vs Computer")) startPvCMode();
                    else startPvPMode();
                }
            }
        });
        pause.setRepeats(false);
        pause.start();
    }

    private String roundStatus() {
        String p1 = (mode.equals("Player vs Computer")) ? "You" : "P1";
        String p2 = (mode.equals("Player vs Computer")) ? "Computer" : "P2";
        return "Round " + currentRound + "/" + totalRounds + "  |  " + p1 + ": " + p1Score + "  " + p2 + ": " + p2Score;
    }
    private String keyToChoiceP1(char key) {
        switch (key) {
            case 'A': return "Rock";
            case 'S': return "Paper";
            case 'D': return "Scissors";
            default: return "";
        }
    }
    private String keyToChoiceP2(char key) {
        switch (key) {
            case 'J': return "Rock";
            case 'K': return "Paper";
            case 'L': return "Scissors";
            default: return "";
        }
    }
    private String getResult(String p1, String p2) {
        if (p1.equals(p2)) return "Draw!";
        if ((p1.equals("Rock") && p2.equals("Scissors")) ||
            (p1.equals("Paper") && p2.equals("Rock")) ||
            (p1.equals("Scissors") && p2.equals("Paper"))) {
            return "Player 1 Wins!";
        } else {
            return "Player 2 Wins!";
        }
    }
    // Add these methods inside GameModeFrame
    private void handlePvCKey(KeyEvent e) {
        if (inputLocked) return;
        char key = Character.toUpperCase(e.getKeyChar());
        if (key == 'R' || key == 'P' || key == 'S') {
            inputLocked = true;
            playerChoice = keyToChoice(key);
            // Store the player's move BEFORE generating the computer's move
            playerMoveHistory.add(playerChoice);
            computerChoice = smartAIMove();
            String result = getResult(playerChoice, computerChoice);
            if (playerChoice.equals(computerChoice)) {
                // Draw, no score
            } else if ((playerChoice.equals("Rock") && computerChoice.equals("Scissors")) ||
                       (playerChoice.equals("Paper") && computerChoice.equals("Rock")) ||
                       (playerChoice.equals("Scissors") && computerChoice.equals("Paper"))) {
                p1Score++;
            } else {
                p2Score++;
            }
            showRoundResult("You: " + playerChoice + " | Computer: " + computerChoice + "   " + result);
        }
    }

    private void handlePvPKey(KeyEvent e) {
        if (inputLocked) return;
        char key = Character.toUpperCase(e.getKeyChar());
        // Player 1: A/S/D
        if (key == 'A' || key == 'S' || key == 'D') {
            if (p1Input == null) {
                p1Input = keyToChoiceP1(key);
            } else {
                p1Cheated = true;
            }
        }
        // Player 2: J/K/L
        if (key == 'J' || key == 'K' || key == 'L') {
            if (p2Input == null) {
                p2Input = keyToChoiceP2(key);
            } else {
                p2Cheated = true;
            }
        }
        // Check for cheating
        if (p1Cheated && !p2Cheated) {
            inputLocked = true;
            p2Score++;
            showRoundResult("Player 1 cheated! Player 2 wins!");
        } else if (p2Cheated && !p1Cheated) {
            inputLocked = true;
            p1Score++;
            showRoundResult("Player 2 cheated! Player 1 wins!");
        } else if (p1Cheated && p2Cheated) {
            inputLocked = true;
            showRoundResult("Both players cheated! No winner.");
        } else if (p1Input != null && p2Input != null) {
            inputLocked = true;
            String result = getResult(p1Input, p2Input);
            if (p1Input.equals(p2Input)) {
                // Draw, no score
            } else if ((p1Input.equals("Rock") && p2Input.equals("Scissors")) ||
                       (p1Input.equals("Paper") && p2Input.equals("Rock")) ||
                       (p1Input.equals("Scissors") && p2Input.equals("Paper"))) {
                p1Score++;
            } else {
                p2Score++;
            }
            showRoundResult("P1: " + p1Input + " | P2: " + p2Input + "   " + result);
        }
    }
    private String keyToChoice(char key) {
        switch (key) {
            case 'R': return "Rock";
            case 'P': return "Paper";
            case 'S': return "Scissors";
            default: return "";
        }
    }
    private String smartAIMove() {
        if (playerMoveHistory.size() <= 1) {
            // First round: random
            return randomChoice();
        }
        int rock = 0, paper = 0, scissors = 0;
        for (String move : playerMoveHistory.subList(0, playerMoveHistory.size() - 1)) {
            if (move.equals("Rock")) rock++;
            else if (move.equals("Paper")) paper++;
            else if (move.equals("Scissors")) scissors++;
        }
        if (rock >= paper && rock >= scissors) {
            return "Paper"; // Counter most frequent Rock
        } else if (paper >= rock && paper >= scissors) {
            return "Scissors"; // Counter most frequent Paper
        } else {
            return "Rock"; // Counter most frequent Scissors
        }
    }
    private String randomChoice() {
        String[] choices = {"Rock", "Paper", "Scissors"};
        int idx = (int)(Math.random() * 3);
        return choices[idx];
    }
    // Add these methods inside GameModeFrame
    private void playWinSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("congratulations-message-notification-sound-sfx-1-334724.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void playLoseSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("game-over-31-179699.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Add this method inside GameModeFrame if you want a draw sound:
    private void playDrawSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("draw-sword1-44724.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



