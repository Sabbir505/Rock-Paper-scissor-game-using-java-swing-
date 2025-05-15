/**
 * GameLogic handles gesture comparison and result calculation.
 */
public class GameLogic {
    /**
     * Compares two gestures and returns the result string.
     */
    public static String getResult(String p1, String p2) {
        if (p1.equals(p2)) return "Draw!";
        if ((p1.equals("Rock") && p2.equals("Scissors")) ||
            (p1.equals("Paper") && p2.equals("Rock")) ||
            (p1.equals("Scissors") && p2.equals("Paper"))) {
            return "Player 1 Wins!";
        } else {
            return "Player 2 Wins!";
        }
    }
}