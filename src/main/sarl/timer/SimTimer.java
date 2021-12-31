package timer;

import java.util.Timer;
import java.util.TimerTask;

public class SimTimer {
 
	public static final long DELAY = 10;
 
	public final Timer timer;
	public Task timerTask;
 
	public SimTimer() { 
		timer = new Timer();
	}
 
	public void start() {
		timerTask = new Task();
		timer.schedule(timerTask, DELAY, DELAY);
	}
 
	public void pauseOrResume() {
		if ( timerTask.isRunning() ) {
			pause();
		}
		else {
			resume();
		}
	}
 
	public void pause() {
		timerTask.cancel();
	}
 
	public void resume() {
		timerTask=new Task(timerTask);
		timer.schedule(timerTask, DELAY, DELAY);
	}
 
	public void stop() {
		timerTask.cancel();
		timer.purge();
	}
}