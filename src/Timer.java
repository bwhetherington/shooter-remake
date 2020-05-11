import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public abstract class Timer {
	private static double timeScale;
	private static Set<Timer> timers = new HashSet<>();
	private static Queue<Timer> 
		toRemove = new LinkedList<>(),
		toAdd = new LinkedList<>();
	private static boolean isRunning = true;
	
	public static void setTimeScaleGlobal(double timeScale) {
		for (Timer timer : timers) {
			timer.setTimeScale(timeScale);
		}
		Timer.timeScale = timeScale;
	}
	
	public static void updateGlobal() {
		if (isRunning) {
			while (!toAdd.isEmpty()) {
				Timer t = toAdd.poll();
				t.setTimeScale(timeScale);
				timers.add(t);
			}
			while (!toRemove.isEmpty()) {
				Timer t = toRemove.poll();
				timers.remove(t);
			}
			for (Timer timer : timers) {
				if (timer.isDone()) {
					unregister(timer);
				} else {
					timer.update();
				}
			}
		}
	}
	
	public static void register(Timer timer) {
		toAdd.offer(timer);
//		System.out.println("+ " + timer);
	}
	
	public static void unregister(Timer timer) {
		toRemove.offer(timer);
//		System.out.println("- " + timer);
	}
	
	public static int count() {
		return timers.size();
	}
	
	public static void pause() {
		isRunning = false;
	}
	
	public static void unpause() {
		isRunning = true;
	}
	
	public static void setPaused(boolean isPaused) {
		Timer.isRunning = !isPaused;
	}
	
	protected abstract void update();
	
	public abstract void setTimeScale(double timeScale);
	
	public abstract boolean isDone();
	
	public abstract void start();
	
	public abstract void stop();
}
