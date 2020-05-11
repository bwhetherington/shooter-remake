import java.awt.Dimension;

public class Dimension2D {
	private double width, height;

	public Dimension2D(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
	
	public String toString() {
		return String.format("Dimension2D[%f x %f]", width, height);
	}
	
	public Dimension getSize() {
		return new Dimension((int) width, (int) height);
	}
}
