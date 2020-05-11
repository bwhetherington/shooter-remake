import java.awt.geom.Point2D;

public class Vector {	
	public double
		dx,
		dy;
	
	private double
		angle,
		magnitude;
	
	public Vector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
		resolveAngleMagnitude();
	}
	
	public Vector() {}
	
	public static Vector fromAngleMagnitude(double angle, double magnitude) {
		return new Vector(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	public Vector add(Vector v) {
		return new Vector(dx + v.dx, dy + v.dy);
	}
	
	public void addEq(Vector v) {
		dx += v.dx;
		dy += v.dy;
		resolveAngleMagnitude();
	}
	
	public Vector subtract(Vector v) {
		return new Vector(dx - v.dx, dy - v.dy);
	}
	
	public void subtractEq(Vector v) {
		dx -= v.dx;
		dy -= v.dy;
		resolveAngleMagnitude();
	}
	
	public Vector mult(double val) {
		return Vector.fromAngleMagnitude(getAngle(), getMagnitude() * val);
	}
	
	public void multEq(double val) {
		Vector v = mult(val);
		resolveAngleMagnitude();
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setAngle(double angle) {
		double magnitude = getMagnitude();
		dx = magnitude * Math.cos(angle);
		dy = magnitude * Math.sin(angle);
		resolveAngleMagnitude();
	}
	
	public double getMagnitude() {
		return magnitude;
	}
	
	public void setMagnitude(double magnitude) {
		double angle = getAngle();
		dx = magnitude * Math.cos(angle);
		dy = magnitude * Math.sin(angle);
		resolveAngleMagnitude();
	}
	
	public String toString() {
		return String.format("Vector[%f, %f]", dx, dy);
	}
	
	public void addMagnitude(double magnitude) {
		double xd = Math.signum(dx), yd = Math.signum(dy);
		setMagnitude(getMagnitude() + magnitude);
		if (xd != Math.signum(dx) || yd != Math.signum(dy)) {
			setMagnitude(0);
		}
	}
	
	public boolean isMoving() {
		return !(dx == 0 && dy == 0);
	}
	
	private void resolveMagnitude() {
		magnitude = Math.sqrt(dx * dx + dy * dy);
	}
	
	private void resolveAngle() {
		angle = Math.atan2(dy, dx);
	}
	
	private void resolveAngleMagnitude() {
		resolveMagnitude();
		resolveAngle();
	}
	
	public double dotProduct(Vector v) {
		return dx * v.dx + dy * v.dy;
	}
}
