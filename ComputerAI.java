import java.util.List;

public class ComputerAI {
    public static String predictMove(List<String> playerHistory) {
        if (playerHistory.size() <= 1) {
            return randomChoice();
        }
        int rock = 0, paper = 0, scissors = 0;
        for (String move : playerHistory.subList(0, playerHistory.size() - 1)) {
            if (move.equals("Rock")) rock++;
            else if (move.equals("Paper")) paper++;
            else if (move.equals("Scissors")) scissors++;
        }
        if (rock >= paper && rock >= scissors) return "Paper";
        if (paper >= rock && paper >= scissors) return "Scissors";
        return "Rock";
    }
    public static String randomChoice() {
        String[] choices = {"Rock", "Paper", "Scissors"};
        int idx = (int)(Math.random() * 3);
        return choices[idx];
    }
}