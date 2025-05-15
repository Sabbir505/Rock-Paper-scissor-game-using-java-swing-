# Rock-Paper-Scissors Java Game

A desktop Rock-Paper-Scissors game built with Java Swing, featuring both Player vs Computer and Player vs Player modes, round selection, sound effects, and a simple AI for the computer opponent.

## Features

- **Player vs Computer**: Play against a computer that adapts to your move history.
- **Player vs Player**: Two players can play on the same keyboard.
- **Round Selection**: Choose between "Best of 3" or "Best of 5" rounds.
- **Sound Effects**: Audio feedback for clicks, wins, losses, and draws.
- **Visual Feedback**: Displays choices with images and emojis.
- **Cheating Detection**: Detects and penalizes if a player tries to input multiple moves per round in PvP mode.

## How to Run

1. **Requirements**:
   - Java JDK 8 or higher
   - (Optional) Java-compatible IDE (e.g., IntelliJ IDEA, Eclipse, NetBeans)

2. **Setup**:
   - Ensure all `.java` files (`RockPaperScissors.java`, `GameLogic.java`, `GameStats.java`, `ComputerAI.java`, `CountdownTimer.java`, `GameWindow.java`) are in the same directory.
   - Place the following sound files in the project directory:
     - `mouse-click-153941.wav`
     - `congratulations-message-notification-sound-sfx-1-334724.wav`
     - `game-over-31-179699.wav`
     - `draw-sword1-44724.wav`
   - Place the following image files in the project directory:
     - `rock.png`
     - `paper.png`
     - `scissor.png`

3. **Compile**:
   ```bash
   javac *.java
4.
   java RockPaperScissors
## Controls
- Player vs Computer :
  - Press R for Rock, P for Paper, S for Scissors when prompted.
- Player vs Player :
  - Player 1: A for Rock, S for Paper, D for Scissors
  - Player 2: J for Rock, K for Paper, L for Scissors
## Project Structure
- RockPaperScissors.java : Main entry point and GUI logic for menus and game modes.
- GameLogic.java : Determines the winner of each round.
- GameStats.java : Tracks scores and move history.
- ComputerAI.java : Implements a simple AI that predicts and counters the player's moves.
- CountdownTimer.java : Handles countdowns before each round.
- GameWindow.java : (Optional/legacy) Alternative window for displaying game results.
## Notes
- Make sure all required sound and image files are present in the project directory for full functionality.
- The game uses Java Swing for the GUI and Java Sound API for audio effects.
## License
This project is for educational purposes.
   
