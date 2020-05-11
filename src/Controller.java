import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public abstract class Controller implements KeyListener, MouseListener, MouseMotionListener {
	protected Camera camera;
	private Point mouseLocationRaw, mouseLocationActual;
	
	public Controller(Camera camera) {
		this.camera = camera;
		mouseLocationRaw = new Point();
		mouseLocationActual = new Point();
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseLocationRaw = e.getPoint();
	}

	public void mouseMoved(MouseEvent e) {
		mouseLocationRaw = e.getPoint();
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {
		getEntity().fireStop();
	}

	public void mouseExited(MouseEvent e) {
		getEntity().fireStop();
	}

	public void mousePressed(MouseEvent e) {
		getEntity().fireStart();
	}

	public void mouseReleased(MouseEvent e) {
		getEntity().fireStop();
	}

	public void keyPressed(KeyEvent e) {}
	
	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}
	
	public final void update() {
		if (!(getLevel() == null || getLevel().isPaused())) {
			updateController();
		}
	}
	
	protected void updateController() {
		Rectangle2D cameraBounds = camera.getBounds();
		mouseLocationActual.x = (int) cameraBounds.getX() + mouseLocationRaw.x;
		mouseLocationActual.y = (int) cameraBounds.getY() + mouseLocationRaw.y;
		Entity e = getEntity();
		e.rotateTurrets(mouseLocationActual);
	}
	
	public Entity getEntity() {
		return camera.getTarget();
	}
	
	public Level getLevel() {
		return getEntity().getLevel();
	}
}
