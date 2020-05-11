import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Allows for collision detection of complex shapes.
 * 
 * @author Benjamin Hetherington
 */
public class Region {
	private Area area;
	private Rectangle2D bounds;
	private Ellipse2D circleBounds;
	private Point2D location;
	private double angle;
	
	public Region(Shape shape) {
		this(new Shape[] {shape});
	}
	
	public Region(Shape... shapes) {
		area = new Area();
		for (Shape s : shapes) {
			area.add(new Area(s));
		}
		bounds = area.getBounds2D();

		double max = Math.max(bounds.getWidth(), bounds.getHeight());
		circleBounds = new Ellipse2D.Double(bounds.getCenterX(), bounds.getCenterY(), max / 2, max / 2);
		
		location = new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
		translate(-location.getX(), -location.getY());
	}
	
	public void translate(double dx, double dy) {
		AffineTransform tx = AffineTransform.getTranslateInstance(dx, dy);
		area.transform(tx);
		location.setLocation(location.getX() + dx, location.getY() + dy);
		Util.translate(circleBounds, dx, dy);
		Util.translate(bounds, dx, dy);
	}
	
	public void translate(Vector v) {
		translate(v.dx, v.dy);
	}
	
	public Rectangle2D getBounds() {
		return bounds;
	}
	
	public void rotate(double angle) {
		Point2D loc = new Point2D.Double(location.getX(), location.getY());
		translate(-loc.getX(), -loc.getY());
		AffineTransform at = AffineTransform.getRotateInstance(angle);
		area.transform(at);
		this.angle = Util.normalizeAngle(this.angle + angle);
		translate(loc.getX(), loc.getY());
	}
	
	public void setAngle(double angle) {
		rotate(angle - this.angle);
	}
	
	public boolean intersects(Region other) {
		return intersectionPoint(other) != null;
	}
	
	public Point2D intersectionPoint(Region other) {
		return testIntersectionPoint(area, other.area);
	}
	
	public double getAngle() {
		return angle;
	}
	
	public Point2D getLocation() {
		return location;
	}
	
	public void setLocation(Point2D location) {
		translate(location.getX() - this.location.getX(), location.getY() - this.location.getY());
	}

	public Area getArea() {
		return area;
	}
	
	public double getWidth() {
		return area.getBounds2D().getWidth();
	}
	
	public double getHeight() {
		return area.getBounds2D().getHeight();
	}
	
	public Dimension2D getSize() {
		return new Dimension2D(bounds.getWidth(), bounds.getHeight());
	}
	
	public Ellipse2D getCircleBounds() {
		return circleBounds;
	}
	
	private static Point2D testIntersectionPoint(Area a1, Area a2) {
		Area test = (Area) a1.clone();
		test.intersect(a2);
		if (test.isEmpty()) {
			return null;
		} else {
			Rectangle2D bounds = test.getBounds2D();
			Point2D center = new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
			return center;
		}
	}
}
