import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class DirectionalController extends Controller {
	public static final int
		FORWARD = 0,
		BACK = 1,
		LEFT = 2,
		RIGHT = 3,
	
		NORTH = 0,
		NORTHEAST = 1,
		EAST = 2,
		SOUTHEAST = 3,
		SOUTH = 4,
		SOUTHWEST = 5,
		WEST = 6,
		NORTHWEST = 7;
	private boolean[] keysDown;
	
	public DirectionalController(Camera camera) {
		super(camera);
		keysDown = new boolean[4];
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			keysDown[FORWARD] = true;
		} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			keysDown[BACK] = true;
		} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			keysDown[LEFT] = true;
		} else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			keysDown[RIGHT] = true;
		} else if (key == KeyEvent.VK_SPACE) {
			getEntity().rotateTo(1);
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			keysDown[FORWARD] = false;
		} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			keysDown[BACK] = false;
		} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			keysDown[LEFT] = false;
		} else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			keysDown[RIGHT] = false;
		}
	}
	
	protected void updateController() {
		super.updateController();
		int direction = getDirection();
		Entity entity = getEntity();
		if (direction > -1) {
			entity.applyMovement();
			switch (direction) {
			case NORTH:
				entity.rotateTo(Math.toRadians(270));
				break;
			case NORTHEAST:
				entity.rotateTo(Math.toRadians(315));
				break;
			case EAST:
				entity.rotateTo(0);
				break;
			case SOUTHEAST:
				entity.rotateTo(Math.toRadians(45));
				break;
			case SOUTH:
				entity.rotateTo(Math.toRadians(90));
				break;
			case SOUTHWEST:
				entity.rotateTo(Math.toRadians(135));
				break;
			case WEST:
				entity.rotateTo(Math.toRadians(180));
				break;
			case NORTHWEST:
				entity.rotateTo(Math.toRadians(225));
				break;
			}
		}
	}
	
	private int getDirection() {
		if (keysDown[FORWARD] && !(keysDown[LEFT] || keysDown[BACK] || keysDown[RIGHT])) {
			return NORTH;
		}
		if (keysDown[FORWARD] && keysDown[LEFT] && !(keysDown[BACK] || keysDown[RIGHT])) {
			return NORTHWEST;
		}
		if (keysDown[LEFT] && !(keysDown[FORWARD] || keysDown[BACK] || keysDown[RIGHT])) {
			return WEST;
		}
		if (keysDown[BACK] && keysDown[LEFT] && !(keysDown[FORWARD] || keysDown[RIGHT])) {
			return SOUTHWEST;
		}
		if (keysDown[BACK] && !(keysDown[LEFT] || keysDown[FORWARD] || keysDown[RIGHT])) {
			return SOUTH;
		}
		if (keysDown[BACK] && keysDown[RIGHT] && !(keysDown[FORWARD] || keysDown[LEFT])) {
			return SOUTHEAST;
		}
		if (keysDown[RIGHT] && !(keysDown[LEFT] || keysDown[BACK] || keysDown[FORWARD])) {
			return EAST;
		}
		if (keysDown[FORWARD] && keysDown[RIGHT] && !(keysDown[BACK] || keysDown[LEFT])) {
			return NORTHEAST;
		}
		return -1;
	}
}
