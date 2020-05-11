import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Entity {
	protected static final double TURNING_FRICTION = 0.008,
			TURNING_FRICTION_SQUARE = TURNING_FRICTION * TURNING_FRICTION;

	public static final int POINT_WIDTH = 6, ORIGIN = 0, WEAPON = 1, WEAPON_L = 2, WEAPON_R = 3, TURRET_ONE = 4,
			TURRET_TWO = 5, TURRET_THREE = 6, TURRET_FOUR = 7;

	public static final double NO_TARGET_ANGLE = Double.POSITIVE_INFINITY;

	private ActionTimer spriteTimer;
	private Region bounds;
	private Sprite sprite, radarIcon, overheadIcon;
	private Vector momentum, movement;
	private Level level;
	private List<Renderer> renderers;
	private double angularMovement, angularMomentum, timeScale, movementAcceleration, movementSpeed,
			turningAcceleration, turningSpeed, targetAngle, mass, rootMass, restitution = 0.2125, radius;
	private int barWidth, barHeight;
	private boolean drawBounds;
	private Point2D[] hardPoints;
	private Entity[] attachedEntities;
	private Entity parent;
	private int hardPoint, radarIconRadius = 2;
	private Weapon weapon;
	private boolean isCullable, isMoving, showOverheatBar, isAlive, isCollidable, useRotation, showOverheadIcon,
			showRadarIcon, showRadarIconLongRange = true;
	private Renderer overheatRenderer;

	public Entity(Region bounds, Sprite sprite) {
		this.bounds = bounds == null ? new Region() : bounds;
		this.sprite = sprite;
		timeScale = 1;
		movement = new Vector();
		momentum = new Vector();
		renderers = new LinkedList<>();
		// drawBounds = true;
		hardPoints = new Point2D[] { new Point2D.Double() };
		attachedEntities = new Entity[1];
		spriteTimer = new ActionTimer(2, () -> {
			if (Entity.this.sprite != null) {
				Entity.this.sprite.update();
			}
			if (Entity.this.radarIcon != null) {
				Entity.this.radarIcon.update();
			}
		});
		spriteTimer.start();
		targetAngle = NO_TARGET_ANGLE;
		isAlive = true;
		useRotation = true;

		barWidth = 80;
		barHeight = 12;

		// Weapon overheat bar
		overheatRenderer = (g) -> {
			if (showOverheatBar && weapon != null && weapon.getTemperature() > 0) {
				Dimension size = getTopParent().getSpriteSize();
				int dx = (int) (((double) size.getWidth() - barHeight) / 2), dy = -5;
				Color c = g.getColor();
				try {
					g.translate(dx, dy);
					g.setColor(Colors.BAR_BACKGROUND);
					g.fillRect(0, 0, barWidth, barHeight);
					g.setColor(weapon.getTemperature() < weapon.getTemperatureFire() ? Colors.TEMPERATURE_BAR_FOREGROUND
							: Colors.TEMPERATURE_BAR_OVERHEATED_FOREGROUND);
					double width = weapon.getTemperature() / weapon.getTemperatureFire();
					g.fillRect(0, 0, (int) ((width > 1 ? 1 : width) * barWidth), barHeight);
					g.setColor(Color.WHITE);
					g.drawRect(0, 0, barWidth, barHeight);
				} finally {
					g.translate(-dx, -dy);
					g.setColor(c);
				}
			}
		};
		addRenderer(overheatRenderer);

		Renderer overheadIconRenderer = (g) -> {
			if (overheadIcon != null) {
				BufferedImage image = overheadIcon.getCurrentFrame();
				Dimension size = getSpriteSize();
				int dx = (size.width - image.getWidth()) / 2, dy = -64;
				g.drawImage(image, dx, dy, null);
			}
		};
		addRenderer(overheadIconRenderer);

		setMass(1);
	}

	public Entity() {
		this(null, null);
	}

	public boolean isCullable() {
		return isCullable;
	}

	public void setCullable(boolean cullable) {
		this.isCullable = cullable;
	}

	public void setOverheatShown(boolean show) {
		showOverheatBar = show;
	}

	public void setBarWidth(int width) {
		barWidth = width;
	}

	public void setBarHeight(int height) {
		barHeight = height;
	}

	public int getBarWidth() {
		return barWidth;
	}

	public int getBarHeight() {
		return barHeight;
	}

	public double getTimeScale() {
		return timeScale;
	}

	public void setHardPoints(Point2D[] hardPoints) {
		this.hardPoints = hardPoints;
		for (Entity e : attachedEntities) {
			if (e != null) {
				e.kill();
			}
		}
		attachedEntities = new Entity[hardPoints.length];
	}

	private boolean isValidAttachmentPoint(int attachmentPoint) {
		return -1 < attachmentPoint && attachmentPoint < hardPoints.length;
	}

	public void attachEntity(Entity e, int attachmentPoint) {
		if (isValidAttachmentPoint(attachmentPoint)) {
			attachedEntities[attachmentPoint] = e;
			e.parent = this;
			e.removeRenderer(e.overheatRenderer);
			if (e.overheatRenderer != null) {
				addRenderer(e.overheatRenderer);
			}
			e.hardPoint = attachmentPoint;
			e.timeScale = timeScale;
			if (level != null) {
				level.addEntity(e);
			}
		} else {
			System.err.println("Invalid point");
		}
	}

	public Point2D getHardPoint(int hardPoint) {
		if (-1 < hardPoint && hardPoint < hardPoints.length) {
			Point2D original = hardPoints[hardPoint], location = getLocation();
			Vector v = new Vector(original.getX(), original.getY());
			if (useRotation) {
				v.setAngle(getAngle());
			}
			return new Point2D.Double(location.getX() + v.dx, location.getY() + v.dy);
		} else {
			throw new IllegalArgumentException("Invalid hard point: " + hardPoint);
		}
	}

	public double getMass() {
		return parent == null ? mass : parent.getMass();
	}

	public double getRootMass() {
		return parent == null ? rootMass : parent.getRootMass();
	}

	public void setMass(double mass) {
		this.mass = mass;
		this.rootMass = Math.sqrt(mass);
	}

	public void setBounds(Region bounds) {
		this.bounds = bounds;
	}

	public Point2D getLocation() {
		if (bounds == null) {
			return Util.ORIGIN;
		}
		return bounds.getLocation();
	}

	public double getAngle() {
		if (bounds == null) {
			return 0;
		}
		return bounds.getAngle();
	}

	public void update() {
		if (level != null) {
			updateEntity();
		}
	}

	protected void updateEntity() {
		updateLocation();
		if (isMoving) {
			playAnimation(Sprite.MOVE);
		} else {
			playAnimation(Sprite.STAND);
		}
		if (weapon != null) {
			weapon.update();
		}
		// if (isCollidable()) {
		// Map<Entity, Point2D> collisions = checkForCollisions();
		// for (Map.Entry<Entity, Point2D> collision : collisions.entrySet()) {
		// collide(collision.getKey(), collision.getValue());
		// }
		// }
		isMoving = false;
	}

	public void fireStart() {
		if (weapon != null) {
			weapon.start();
		}
		for (Entity e : attachedEntities) {
			if (e != null) {
				e.fireStart();
			}
		}
	}

	public void fireStop() {
		if (weapon != null) {
			weapon.stop();
		}
		for (Entity e : attachedEntities) {
			if (e != null) {
				e.fireStop();
			}
		}
	}

	protected void updateLocation() {
		if (parent == null) {
			double angle = getAngle();
			if (targetAngle != NO_TARGET_ANGLE && !(angle == targetAngle && angularMovement == 0)) {
				if (Util.isLeftOf(getAngle(), targetAngle)) {
					turnRight();
				} else {
					turnLeft();
				}
				if (Util.approximateAngle(angle) == Util.approximateAngle(targetAngle)
						&& Math.abs(angularMovement) <= 0.02) {
					angularMovement = 0;
					setAngle(targetAngle);
					targetAngle = NO_TARGET_ANGLE;
				}
			}
			translate(getVelocity().mult(timeScale));
			rotate((angularMovement + angularMomentum) * timeScale);
			applyFrictionVectors();
			applyFrictionAngle();
			applyFrictionAngleMomentum();
		} else {
			setLocation(parent.getHardPoint(hardPoint));
		}
	}

	public void setAngle(double angle) {
		bounds.setAngle(angle);
	}

	private void applyFrictionVectors() {
		momentum.addMagnitude(-restitution * timeScale);
		movement.addMagnitude(-restitution * timeScale);
	}

	private void applyFrictionAngle() {
		double sgn = Math.signum(angularMovement);
		angularMovement -= sgn * TURNING_FRICTION * timeScale;
		if (sgn != Math.signum(angularMovement)) {
			angularMovement = 0;
		}
	}

	private void applyFrictionAngleMomentum() {
		double sgn = Math.signum(angularMomentum);
		angularMomentum -= sgn * TURNING_FRICTION * timeScale;
		if (sgn != Math.signum(angularMomentum)) {
			angularMomentum = 0;
		}
	}

	public void rotateTo(double angle) {
		targetAngle = angle;
	}

	public Vector getVelocity() {
		if (parent == null) {
			return momentum.add(movement);
		} else {
			return parent.getVelocity();
		}
	}

	public double getAngularMomentum() {
		return angularMovement;
	}

	public void render(Graphics g) {
		Rectangle2D cameraBounds = level.getCameraBounds();
		Point2D location = getLocation();
		int dx = (int) -cameraBounds.getX(), dy = (int) -cameraBounds.getY();
		g.translate(dx, dy);
		try {
			Dimension size = sprite == null ? bounds.getSize().getSize() : sprite.getSize();
			int dx2 = (int) (location.getX() - size.width / 2.0), dy2 = (int) (location.getY() - size.height / 2.0);
			g.translate(dx2, dy2);
			try {
				renderSprite(g);
				for (Renderer r : renderers) {
					r.render(g);
				}
			} finally {
				g.translate(-dx2, -dy2);
			}
			if (drawBounds) {
				renderBounds(g);
			}
		} finally {
			g.translate(-dx, -dy);
		}
	}

	private void renderSprite(Graphics g) {
		if (sprite != null) {
			BufferedImage toRender = sprite.getCurrentFrame();
			if (useRotation) {
				toRender = Util.rotateImage(toRender, getAngle());
			}
			g.drawImage(toRender, 0, 0, null);
		}
	}

	private void renderBounds(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.draw(bounds.getArea());
		for (int i = 0; i < hardPoints.length; i++) {
			Point2D hp = getHardPoint(i);
			int x = (int) hp.getX() - POINT_WIDTH / 2, y = (int) hp.getY() - POINT_WIDTH / 2;
			g.setColor(Colors.BAR_BACKGROUND);
			g.fillRect(x, y, POINT_WIDTH, POINT_WIDTH);
			g.setColor(Colors.OUTLINE);
			g.drawRect(x, y, POINT_WIDTH, POINT_WIDTH);
		}
	}

	public void addRenderer(Renderer r) {
		renderers.add(r);
	}

	public void removeRenderer(Renderer r) {
		renderers.remove(r);
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Entity[] getAttachedEntities() {
		return attachedEntities;
	}

	public void kill() {
		if (level != null) {
			if (parent != null) {
				parent.attachedEntities[hardPoint] = null;
			}
			
			onDeath();
			isAlive = false;
			if (weapon != null) {
				weapon.destroy();
			}
			for (Entity e : attachedEntities) {
				if (e != null) {
					e.kill();
				}
			}
			spriteTimer.destroy();
			level.removeEntity(this);
		}
	}

	public boolean intersects(Entity other) {
		return other.isAlive && intersects(other.bounds);
	}

	public boolean intersects(Region other) {
		if (isAlive) {
			return bounds.intersects(other);
		} else {
			return false;
		}
	}

	public void applyMovement() {
		applyMovement(getAngle(), movementAcceleration * timeScale * mass);
		isMoving = movement.getMagnitude() > 0;
	}

	public void applyMovement(Vector v) {
		movement.addEq(v);
		fixMovementVector();
	}

	public void applyMovement(double angle, double magnitude) {
		applyMovement(Vector.fromAngleMagnitude(angle, magnitude / mass));
	}

	public void turnLeft() {
		angularMovement -= turningAcceleration * timeScale;
		fixTurningSpeed();
	}

	public void pushLeft(double acceleration) {
		angularMomentum -= acceleration * timeScale;
		fixTurningSpeed();
	}

	public void turnRight() {
		angularMovement += turningAcceleration * timeScale;
		fixTurningSpeed();
	}

	public void pushRight(double acceleration) {
		angularMomentum += acceleration * timeScale;
		fixTurningSpeed();
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void fixMovementVector() {
		if (movement.getMagnitude() > movementSpeed) {
			movement.setMagnitude(movementSpeed);
		}
	}

	public void fixTurningSpeed() {
		if (Math.abs(angularMovement) > turningSpeed) {
			angularMovement = Math.signum(angularMovement) * turningSpeed;
		}
		if (Math.abs(angularMomentum) > turningSpeed) {
			angularMomentum = Math.signum(angularMomentum) * turningSpeed;
		}
	}

	public Region getBounds() {
		return bounds;
	}

	public void setMovementSpeed(double speed) {
		movementSpeed = speed;
	}

	public double getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementAcceleration(double acceleration) {
		movementAcceleration = acceleration;
	}

	public void setTurningSpeed(double speed) {
		turningSpeed = speed;
	}

	public void setTurningAcceleration(double acceleration) {
		turningAcceleration = acceleration;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void playAnimation(int animation) {
		if (sprite != null) {
			sprite.playAnimation(animation);
		}
	}

	public void applyForce(double angle, double magnitude) {
		applyForce(Vector.fromAngleMagnitude(angle, magnitude));
	}

	public void applyForce(Vector v) {
		if (parent == null) {
			momentum.addEq(v.mult(1 / mass));
		} else {
			parent.applyForce(v);
		}
	}

	public Level getLevel() {
		return level;
	}

	public Dimension2D getSize() {
		return bounds.getSize();
	}

	public Dimension getSpriteSize() {
		return sprite == null ? null : sprite.getSize();
	}

	public void setLocation(Point2D location) {
		bounds.setLocation(location);
	}

	public Vector getVector() {
		return momentum.add(movement);
	}

	public void rotateTurrets(Point2D target) {
		for (Entity e : attachedEntities) {
			if (e instanceof Turret) {
				e.setAngle(Util.angleBetweenPoints(e.getLocation(), target));
			}
		}
	}

	public Entity getParent() {
		return parent;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public boolean belongsTo(Entity e) {
		if (this == e) {
			return true;
		} else {
			if (parent == null) {
				return false;
			} else {
				if (parent == e) {
					return true;
				} else {
					return parent.belongsTo(e);
				}
			}
		}
	}

	public boolean isAllyOf(Entity other) {
		Entity parent = getTopParent();
		return parent != this && parent.isAllyOf(other.getTopParent());
	}

	protected void onDeath() {
	}

	public void setTimeScale(double speed) {
		this.timeScale = speed;
		for (Entity e : attachedEntities) {
			if (e != null) {
				e.setTimeScale(speed);
			}
		}
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public Entity getTopParent() {
		return parent == null ? this : parent.getTopParent();
	}

	public String getName() {
		return getClass().getName();
	}

	public Color getNameColor() {
		return Colors.OUTLINE;
	}

	public void renderRadarIconEntity(Graphics g, Rectangle2D radarArea, Dimension radarSize, double radius, double padding) {
		Point2D p = getLocation();
		Rectangle2D paddedRadarArea = Util.padRectangle(radarArea, padding);
		boolean adjusted = false;
		if (!paddedRadarArea.contains(p)) {
			p = Util.pointOnEdgeOfRectangle(paddedRadarArea, Util.angleBetweenPoints(Util.getRectangleCenter(paddedRadarArea), p));
			adjusted = true;
		}
		if (paddedRadarArea.contains(getLocation())) {
			
			double w = radarSize.getWidth(), h = radarSize.getHeight(), dx = p.getX() - radarArea.getX(),
					dy = p.getY() - radarArea.getY();
			int x = (int) (dx / radius * w) - radarSize.width / 2, y = (int) (dy / radius * h) - radarSize.height / 2;
			try {
				g.translate(x, y);
				renderRadarIcon(g);
			} finally {
				g.translate(-x, -y);
			}
		}
	}

	public void renderRadarIcon(Graphics g) {
		if (showRadarIcon) {
			if (radarIcon != null) {
				BufferedImage image = radarIcon.getCurrentFrame();
				g.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2, null);
			} else {
				g.setColor(getNameColor());
				g.fillRect(-2, -2, 4, 4);
				g.setColor(Util.modifyColor(getNameColor(), 0.5));
				g.drawRect(-2, -2, 4, 4);
			}
		}
	}

	public boolean isCollidable() {
		return isCollidable;
	}

	public void setCollidable(boolean isCollidable) {
		this.isCollidable = isCollidable;
	}

	public void setRadarIcon(Sprite icon) {
		this.radarIcon = icon;
	}

	public void setOverheadIcon(Sprite icon) {
		this.overheadIcon = icon;
	}

	public boolean usesRotation() {
		return useRotation;
	}

	public void setUsesRotation(boolean usesRotation) {
		this.useRotation = usesRotation;
	}

	public Point2D getCollisionPoint(Entity other) {
		return getBounds().intersectionPoint(other.getBounds());
	}

	public Map<Entity, Point2D> checkForCollisions() {
		Map<Entity, Point2D> collisions = new HashMap<>();
		Level level = getLevel();
		if (level != null) {
			for (Entity e : level.getEntities()) {
				Point2D collision = getCollisionPoint(e);
				if (e != this && e.isCollidable() && collision != null) {
					collisions.put(e, collision);
				}
			}
		}
		return collisions;
	}

	public void translate(Vector v) {
		bounds.translate(v);
	}

	public void translate(double dx, double dy) {
		bounds.translate(dx, dy);
	}

	public void rotate(double angle) {
		bounds.rotate(angle);
	}

	public void collide(Entity other, Point2D point) {
		Ellipse2D circle1 = getBounds().getCircleBounds(), circle2 = other.getBounds().getCircleBounds();
		Point2D point1 = getLocation(), point2 = other.getLocation();
		double angle1 = Util.angleBetweenPoints(point, other.getLocation()),
				angle2 = Util.angleBetweenPoints(point, getLocation()),
				distance = point1.distance(point2) - (circle1.getWidth() / 2 + circle2.getWidth() / 2);
		other.applyMovement(angle1, getMass() * getVelocity().getMagnitude());
	}

	public boolean showRadarIcon() {
		return showRadarIcon;
	}

	public void setShowRadarIcon(boolean show) {
		showRadarIcon = show;
	}
	
	public double getRestitution() {
		return restitution;
	}
	
	public void setRestitution(double restitution) {
		this.restitution = restitution;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public double getRadius() {
		return radius;
	}
}
