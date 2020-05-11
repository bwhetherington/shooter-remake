import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Level {
	public static final double
		CULL_RADIUS = 2000;
	
	private Set<Entity> entities;
	private Queue<Entity> toAdd, toRemove;
	private BufferedImage textureImage;
	private Paint texture;
	private Rectangle2D viewBounds;
	private Camera camera;
	private Dimension cameraSize;
	private PlayerShip player;
	private double timeScale;
	private int onScreenEntityCount;
	private boolean isPaused, useFriendlyFire;

	public Level() {
		entities = new LinkedHashSet<>();
		toAdd = new LinkedList<>();
		toRemove = new LinkedList<>();
		timeScale = 1;
	}
	
	public PlayerShip getPlayer() {
		return player;
		// hi
	}

	public void update() {
		if (!isPaused) {
			if (camera != null) {
				camera.setSize(new Dimension2D(cameraSize.width, cameraSize.height));
				camera.update();
			}
			Entity mod;
			while (!toAdd.isEmpty()) {
				mod = toAdd.poll();
				mod.setLevel(this);
				mod.setTimeScale(timeScale);
				entities.add(mod);
				if (mod instanceof PlayerShip) {
					player = (PlayerShip) mod;
				}
			}
			while (!toRemove.isEmpty()) {
				mod = toRemove.poll();
				mod.setLevel(null);
				mod.setTimeScale(0);
				entities.remove(mod);
			}
			for (Entity e : entities) {
				if (e.isCullable() && e.getLocation().distance(camera.getLocation()) > CULL_RADIUS) {
					e.kill();
				}
				e.update();
			}
			updateView();
		}
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public void setPaused(boolean paused) {
		isPaused = paused;
		Timer.setPaused(paused);
	}
	
	public int getEntityCount() {
		return entities.size();
	}

	public void render(Graphics g) {
		if (texture != null) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setPaint(texture);
			g2d.fill(new Rectangle(0, 0, cameraSize.width, cameraSize.height));
		}
		for (Entity e : entities) {
			if (camera.getBounds().intersects(e.getBounds().getBounds())) {
				e.render(g);
			}
		}
	}
	
	private void updateView() {
		Point2D cameraLocation = camera.getLocation();
		viewBounds = new Rectangle2D.Double(-cameraLocation.getX() * 0.5, -cameraLocation.getY() * 0.5, textureImage.getWidth(), textureImage.getHeight());
		texture = new TexturePaint(textureImage, viewBounds);
	}

	public void addEntity(Entity e) {
		toAdd.offer(e);
		e.setLevel(this);
		for (Entity a : e.getAttachedEntities()) {
			if (a != null) {
				addEntity(a);
			}
		}
	}

	public void removeEntity(Entity e) {
		toRemove.offer(e);
	}

	public Point2D getCameraLocation() {
		if (camera == null) {
			return Util.ORIGIN;
		} else {
			return camera.getLocation();
		}
	}

	public Rectangle2D getCameraBounds() {
		return camera == null ? null : camera.getBounds();
	}
	
	public void setCameraSize(Dimension size) {
		cameraSize = size;
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
		camera.setTimeScale(timeScale);
	}
	
	public void setTexture(BufferedImage image) {
		textureImage = image;
	}
	
	public Entity getCameraTarget() {
		return camera.getTarget();
	}
	
	public boolean isCameraTarget(Entity entity) {
		return entity == getCameraTarget();
	}
	
	public void shakeCamera(double angle, double magnitude) {
		if (camera != null) {
			camera.shake(angle, magnitude);
		}
	}
	
	public Set<Entity> getEntitiesInBounds(Region b) {
		Set<Entity> inArea = new HashSet<>();
		for (Entity e : entities) {
			if (e.isAlive() && e.intersects(b)) {
				inArea.add(e);
			}
		}
		return inArea;
	}
	
	public Set<Entity> getEntitiesInRectangle(Rectangle2D r) {
		Set<Entity> inArea = new HashSet<>();
		for (Entity e : entities) {
			if (e.isAlive() && r.contains(e.getBounds().getBounds())) {
				inArea.add(e);
			}
		}
		return inArea;
	}
	
	public Set<Unit> getUnitsInBounds(Region b) {
		Set<Unit> inArea = new HashSet<>();
		for (Entity e : entities) {
			if (e.isAlive() && e instanceof Unit && e.intersects(b)) {
				inArea.add((Unit) e);
			}
		}
		return inArea;
	}
	
	public Set<Unit> getUnitsInRectangle(Rectangle2D r) {
		Set<Unit> inArea = new HashSet<>();
		for (Entity e : entities) {
			if (e.isAlive() && e instanceof Unit && r.contains(e.getBounds().getBounds())) {
				inArea.add((Unit) e);
			}
		}
		return inArea;
	}
	
	public void setTimeScale(double speed) {
		this.timeScale = speed;
		for (Entity e : entities) {
			e.setTimeScale(speed);
		}
		if (camera != null) {
			camera.setTimeScale(speed);
		}
	}
	
	public int getOnScreenEntityCount() {
		int count = 0;
		for (Entity e : entities) {
			if (camera.getBounds().intersects(e.getBounds().getBounds())) {
				count++;
			}
		}
		return count;
	}
	
	public Set<Entity> getEntities() {
		return entities;
	}
	
	public double getTimeScale() {
		return timeScale;
	}
	
	public boolean useFriendlyFire() {
		return useFriendlyFire;
	}
	
	public void setUseFriendlyFire(boolean friendlyFire) {
		useFriendlyFire = friendlyFire;
	}
	
	public Set<Unit> getUnits() {
		Set<Unit> units = new HashSet<>();
		for (Entity e : getEntities()) {
			if (e instanceof Unit) {
				units.add((Unit) e);
			}
		}
		return units;
	}
}
