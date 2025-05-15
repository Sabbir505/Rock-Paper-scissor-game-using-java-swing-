import javax.swing.*;

public class CountdownTimer {
    private Timer timer;
    private int seconds;
    private Runnable onFinish;

    public CountdownTimer(int seconds, Runnable onFinish) {
        this.seconds = seconds;
        this.onFinish = onFinish;
    }

    public void start(JLabel label, String prefix) {
        timer = new Timer(1000, e -> {
            if (seconds > 0) {
                label.setText(prefix + seconds);
                seconds--;
            } else {
                timer.stop();
                onFinish.run();
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }
}