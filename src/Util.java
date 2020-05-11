import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Util {
	public static final Point2D ORIGIN = new Point2D.Double(0, 0);
	public static final double
		PI = Math.PI,
		TWO_PI = PI * 2,
		HALF_PI = PI / 2;

	private static Map<String, BufferedImage> images = new HashMap<>();
	private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();

	public static BufferedImage loadImage(String path) {
		BufferedImage image = null;
		if (images.containsKey(path)) {
			image = images.get(path);
		} else {
			try {
				image = ImageIO.read(new File("resources/images/" + path));
				images.put(path, image);
			} catch (IOException e) {
				System.err.println("Resource not found: " + path);
				image = null;
			}
		}
		return image;
	}

	public static double normalizeAngle(double angle) {
		return (angle % TWO_PI + TWO_PI) % TWO_PI;
	}

	public static void rotate(Point2D point, Point2D center, double angle) {
		double sin = Math.sin(angle), cos = Math.cos(angle);

		if (cos != 1) {
			double deltaX = point.getX() - center.getX(), deltaY = point.getY()
					- center.getY();
			double newX = center.getX() + deltaX * cos - deltaY * sin;
			double newY = center.getY() + deltaX * sin + deltaY * cos;
			point.setLocation(newX, newY);
		}
	}

	public static BufferedImage rotateImage(BufferedImage image, Double degrees) {
		if (image == null) {
			System.out.println("Image");
			return image;
		}
		AffineTransform tx = AffineTransform.getRotateInstance(degrees,
				image.getWidth() / 2, image.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_BILINEAR);
		return op.filter(image, null);
	}

	public static BufferedImage toBufferedImage(Image img) {
		BufferedImage bimage = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		return bimage;
	}

	public static boolean areOppositeAngles(double angle1, double angle2) {
		return Math.abs(normalizeAngle(angle1) - normalizeAngle(angle2)) == Math.PI;
	}

	public static double angleBetweenPoints(Point2D p1, Point2D p2) {
		return Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
	}

	public static double oppositeAngle(double angle) {
		return normalizeAngle(angle + Math.PI);
	}

	public static boolean isLeftOf(double angle1, double angle2) {
		return normalizeAngle(angle1 - angle2) > Math.PI;
	}

	public static double approximateAngle(double angle) {
		return Math.toRadians(((int) Math.toDegrees(angle) / 2)) * 2;
	}

	public static double deviateAngle(double angle, double deviation) {
		return angle + (Math.random() - 0.5) * deviation * 2;
	}

	public static double distance(Point2D p1, Point2D p2) {
		double x1 = p1.getX(), y1 = p1.getY(), x2 = p2.getX(), y2 = p2.getX(), dx = x2
				- x1, dy = y2 - y1;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public static double randomAngle() {
		return Math.random() * 2 * Math.PI;
	}

	public static int randomInt(int lower, int upper) {
		return (int) (Math.random() * (upper - lower + 1) + lower);
	}

	public static void preloadSpriteSheet(String id, File file) {
		try {
			spriteSheets.put(id, SpriteIO.read(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static SpriteSheet loadSpriteSheet(String id) {
		return spriteSheets.get(id);
	}

	public static void preloadImage(String image) {
		loadImage(image);
	}

	public static void preloadResources() {
		// Images
		preloadImage("starry-bg-alt.png");

		File[] sprites = new File("resources/sprites").listFiles();
		for (File file : sprites) {
			preloadSpriteSheet(file.getName(), file);
		}
	}

	public static Color modifyColor(Color c, double amount) {
		int amountInt = (int) ((amount - 1) * 255);
		int r = c.getRed() + amountInt, g = c.getGreen() + amountInt, b = c
				.getBlue() + amountInt, a = c.getAlpha();
		if (r < 0)
			r = 0;
		if (r > 255)
			r = 255;
		if (g < 0)
			g = 0;
		if (g > 255)
			g = 255;
		if (b < 0)
			b = 0;
		if (b > 255)
			b = 255;
		return new Color(r, g, b, a);
	}

	public static int getStringWidth(String str, Font font) {
		return (int) getStringBounds(str, font).getWidth();
	}
	
	public static int getStringHeight(String str, Font font) {
		return (int) getStringBounds(str, font).getHeight();
	}
	
	public static Rectangle2D getStringBounds(String str, Font font) {
		return font.getStringBounds(str, new FontRenderContext(font.getTransform(), false, false)).getBounds();
	}
	
	public static MouseEvent cloneMouseEvent(MouseEvent e) {
		return new MouseEvent(
				e.getComponent(),
				e.getID(),
				e.getWhen(),
				e.getModifiers(),
				e.getX(),
				e.getY(),
				e.getClickCount(),
				e.isPopupTrigger());
	}
	
	public static double angleDifference(double angle1, double angle2) {
		return Math.min(realModulus(angle1 - angle2, TWO_PI), realModulus(angle2 - angle1, TWO_PI));
	}
	
	public static double realModulus(double operand, double modulo) {
		return (operand % modulo + modulo) % modulo;
	}
	
	public static double randomDouble(double lower, double upper) {
		return Math.random() * (upper - lower) + lower;
	}
	
	public static void translate(Rectangle2D rect, double dx, double dy) {
		rect.setFrame(rect.getX() + dx, rect.getY() + dy, rect.getWidth(), rect.getHeight());
	}
	
	public static void translate(Ellipse2D ell, double dx, double dy) {
		ell.setFrame(ell.getX() + dx, ell.getY() + dy, ell.getWidth(), ell.getHeight());
	}
	
	public static Point2D pointOnEdgeOfRectangle(Rectangle2D rect, double angle) {
		double width = rect.getWidth(), height = rect.getHeight();
		
		double rectAtan = Math.atan2(height, width);
		double tanTheta = Math.tan(angle);
		int region;
		  
		  if ((angle > -rectAtan) && (angle <= rectAtan)) {
		      region = 1;
		  } else if ((angle > rectAtan) && (angle <= (Math.PI - rectAtan))) {
		      region = 2;
		  } else if ((angle > (Math.PI - rectAtan)) || (angle <= -(Math.PI - rectAtan))) {
		      region = 3;
		  } else {
		      region = 4;
		  }
		  
		  double x = 0, y = 0;
		  int xFactor = 1;
		  int yFactor = 1;
		  
		  switch (region) {
		    case 1: yFactor = -1; break;
		    case 2: yFactor = -1; break;
		    case 3: xFactor = -1; break;
		    case 4: xFactor = -1; break;
		  }
		  
		  if ((region == 1) || (region == 3)) {
		    x += xFactor * (width / 2.); 
		    y += yFactor * (width / 2.) * tanTheta;
		  } else {
		    x += xFactor * (height / (2. * tanTheta));
		    y += yFactor * (height /  2.);
		  }
		  
		  return new Point2D.Double(x + rect.getX(), y + rect.getY());
	}
	
	public static Point2D getRectangleCenter(Rectangle2D rect) {
		return new Point2D.Double(rect.getCenterX(), rect.getCenterY());
	}
	
	public static Point2D randomPointInRadius(Point2D center, double radius) {
		double dist = randomDouble(0, radius), angle = randomAngle();
		Vector v = Vector.fromAngleMagnitude(angle, dist);
		return new Point2D.Double(center.getX() + v.dx, center.getY() + v.dy);
	}
	
	public static Rectangle2D padRectangle(Rectangle2D rect, double padding) {
		return new Rectangle2D.Double(rect.getX() + padding, rect.getY() + padding, rect.getWidth() - padding * 2, rect.getHeight() - padding * 2);
	}
	
	public static Color shadowColor(Color color) {
		Color nc = new Color(color.getRed() / 6, color.getGreen() / 6, color.getBlue() / 6, 64);
		return nc;
	}
	
	public static Color getBorderColor(Color bg) {
		Color brd = Util.modifyColor(bg, 1.1);
		return new Color(brd.getRed(), brd.getGreen(), brd.getBlue());
	}
	
	public static double clamp(double val, double min, double max) {
		return val < min ? min : val > max ? max : val;
	}
	
	public static int clamp(int val, int min, int max) {
		return val < min ? min : val > max ? max : val;
	}
	
	public static Point2D offsetPoint(Point2D point, Vector offset) {
		return new Point2D.Double(point.getX() + offset.dx, point.getY() + offset.dy);
	}
	
	public static Point2D offsetPoint(Point2D point, double angle, double distance) {
		return offsetPoint(point, Vector.fromAngleMagnitude(angle, distance));
	}
}
