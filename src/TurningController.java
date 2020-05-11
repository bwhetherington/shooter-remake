import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class TurningController extends Controller {
	public static final int
		FORWARD = 0,
		LEFT = 1,
		RIGHT = 2;
	private boolean[] keysDown;
	
	public TurningController(Camera camera) {
		super(camera);
		keysDown = new boolean[3];
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			keysDown[FORWARD] = true;
		} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			keysDown[LEFT] = true;
		} else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			keysDown[RIGHT] = true;
		} else if (key == KeyEvent.VK_SPACE) {
			getEntity().applyForce(0, 100);
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			keysDown[FORWARD] = false;
		} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			keysDown[LEFT] = false;
		} else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			keysDown[RIGHT] = false;
		}
	}

	public void keyTyped(KeyEvent e) {}
	
	protected void updateController() {
		super.updateController();
		Entity entity = getEntity();
		if (keysDown[FORWARD]) {
			entity.applyMovement();
		}
		if (keysDown[LEFT]) {
			entity.turnLeft();
		}
		if (keysDown[RIGHT]) {
			entity.turnRight();
		}
	}
}
