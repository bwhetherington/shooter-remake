import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class Camera {
	private Entity target;
	private Vector
		shakeVector;
	private Point2D location;
	private Rectangle2D bounds;
	private Dimension2D size;
	private double
		timeScale,
		maxShakeDistance;
	
	public Camera(Entity target) {
		this.target = target;
		shakeVector = new Vector();
		size = new Dimension2D(1, 1);
		bounds = new Rectangle2D.Double(0, 0, size.getWidth(), size.getHeight());
		maxShakeDistance = 50;
	}
	
	public void setSize(Dimension2D size) {
		this.size = size;
	}
	
	public void update() {
		location = calculateLocation();
		bounds.setFrame(
			location.getX() - size.getWidth() / 2,
			location.getY() - size.getHeight() / 2,
			size.getWidth(),
			size.getHeight()
		);
	}
	
	public Rectangle2D getBounds() {
		return bounds;
	}
	
	private Point2D calculateLocation() {
		Point2D targetLocation = target.getLocation();
		Vector velocity = target.getVelocity();
		double
			angle = target.getAngle(),
			speed = velocity.getMagnitude(),
			offsetFrontBack = -Math.sqrt(speed * 4),
			offsetLeftRight = target.getAngularMomentum() * 0 * speed / target.getMovementSpeed();
		Vector offset = new Vector(offsetFrontBack, offsetLeftRight);
		offset.setAngle(offset.getAngle() + angle);
		offset.addEq(shakeVector);
		shakeVector.addMagnitude(-timeScale * 0.5);
		if (shakeVector.getMagnitude() > maxShakeDistance) {
			shakeVector.setMagnitude(maxShakeDistance);
		}
		
		Point2D location = new Point2D.Double(targetLocation.getX() + offset.dx, targetLocation.getY() + offset.dy);
		return location;
	}
	
	public Point2D getLocation() {
		return location;
	}
	
	public void shake(double angle, double magnitude) {
		shakeVector.addEq(Vector.fromAngleMagnitude(angle, magnitude));
	}
	
	public Entity getTarget() {
		return target;
	}
	
	public void setTimeScale(double timeScale) {
		this.timeScale = timeScale;
	}
}
