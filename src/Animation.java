import java.io.Serializable;

public class Animation implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 176588250271760395L;
		private int start, stop, current;
		private double speed, time;
		private boolean repeat, isDone;

		/**
		 * Constructs an animation with a specified start and stop frame.
		 * 
		 * @param startThe first frame of the animation
		 * @param stop The last frame of the animation
		 */
		public Animation(int start, int stop, boolean repeat) {
			this(start, stop, repeat, 1);
		}
		
		/**
		 * Constructs an animation with the specified start frame, stop 
		 * frame, repeat mode, and speed.
		 * 
		 * @param start
		 * @param stop
		 * @param repeat
		 * @param speed
		 */
		public Animation(int start, int stop, boolean repeat, double speed) {
			this.start = start;
			this.stop = stop;
			current = start;
			this.repeat = repeat;
			this.speed = speed;
		}
		
		public Animation() {
			this(0, 0, true);
		}

		/**
		 * Returns the current frame.
		 * 
		 * @return The current frame
		 */
		public int getCurrent() {
			return current;
		}

		/**
		 * Returns true if the current frame is less than the end frame.
		 * 
		 * @return Whether or not the animation is complete
		 */
		public boolean isDone() {
			return isDone;
		}

		public void restart() {
			current = start;
			isDone  = false;
		}
		
		public boolean isRepeating() {
			return repeat;
		}

		/**
		 * Increments the current frame if the animation is not complete.
		 */
		public void update() {
			time += speed;
			if (time >= 1) {
				while (time-- >= 1);
				current++;
				if (current > stop) {
					if (repeat) {
						current = start;
					} else {
						current--;
						isDone = true;
					}
				}
			}
		}
		
		public String toString() {
			return String.format("Animation[[%d, %d], %b]", start, stop, repeat);
		}
		
		public Animation clone() {
			return new Animation(start, stop, repeat, speed);
		}
	}