package timer;

import java.util.TimerTask;
import controller.MainWindowController;

public class Task extends TimerTask {
 
		final long start;
		public boolean running;
		

		public Task() {
			super();
			this.start = 0;
		}
		
		public Task(Task task) {
			this(task.start);
		}
		public Task(long start) {
			this.start=start;
		}
 
		@Override
		public boolean cancel() {
			final boolean cancel = super.cancel();
			running=false;
			return cancel;
		}
 
		public boolean isRunning() {
			return running;
		}
 
		@Override
		public void run() {
			running=true;
		}
 
	};