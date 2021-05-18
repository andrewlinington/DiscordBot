package Commands.SH.Helper;

import java.util.Timer;
import java.util.TimerTask;

public class Rate {

    private Timer timer;

    private int numMessages;


    public void updateNumPointlessMessages (boolean isUseless) {
        if (isUseless) {
          numMessages++;
          timer.cancel();
          initTimer();
        }
    }


    private void initTimer() {
        timer = new Timer();
        timer.schedule(new IgnoreCommands(), 1000 * 10);
    }


    private class IgnoreCommands extends TimerTask {
        @Override
        public void run() {
            numMessages = 0;
            timer.cancel();
            timer = null;
        }
    }
}
