
public class ActionTimer extends Timer implements Action {
	private Action action;
	private double
		curTime,
		timeScale,
		delay,
		speedMult;
	private int 
		repetitions,
		repetitionsTarget;
	public static final int REPEAT = -1;
	private boolean isRunning, isDone;
	
	public ActionTimer(double delay, int repetitions, Action action) {
		this.action = action;
		this.delay = delay;
		this.repetitionsTarget = repetitions;
		speedMult = 1;
		timeScale = 1;
		Timer.register(this);
	}
	
	public ActionTimer(double delay, int repetitions) {
		this(delay, repetitions, null);
	}
	
	public ActionTimer(double delay) {
		this(delay, REPEAT);
	}
	
	/**
	 * Constructs a timer with a specified delay and action. The timer
	 * is set to repeat by default.
	 * @param delay
	 * @param action
	 */
	public ActionTimer(double delay, Action action) {
		this(delay, REPEAT, action);
	}
	
	/**
	 * Sets the time scale of the timer.
	 * @param timeScale The time scale
	 */
	public void setTimeScale(double timeScale) {
		this.timeScale = timeScale;
	}
	
	/**
	 * Increments the time of the timer until it reaches the delay time,
	 * at which point it executes the specified action.
	 */
	public void update() {
		if (isRunning) {
			if (repetitionsTarget == REPEAT || repetitions < repetitionsTarget) {
				if (curTime >= delay) {
					curTime -= delay;
					repetitions++;
					perform();
				} else {
					curTime += timeScale * speedMult;
				}
			} else {
				isDone = true;
			}
		}
	}
	
	/**
	 * Start the timer.
	 */
	public void start() {
		isRunning = true;
		if (!isRunning) {
			curTime = 0;
		}
	}
	
	/**
	 * Stop the timer.
	 */
	public void stop() {
		isRunning = false;
	}
	
	/**
	 * Restart the timer.
	 */
	public void restart() {
		repetitions = 0;
	}
	
	/**
	 * Sets the speed multiplier of the timer.
	 * @param speedMult The speed multiplier
	 */
	public void setSpeedMult(double speedMult) {
		this.speedMult = speedMult;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public void perform() {
		if (action != null) {
			action.perform();
		}
	}

	@Override
	public boolean isDone() {
		return isDone;
	}
	
	public void destroy() {
		isDone = true;
	}
}
