import java.awt.Color;
import java.awt.Graphics;

public final class GraphicsUtil {
	private GraphicsUtil() {}
	
	public static void drawShadowedString(Graphics g, String str, int x, int y, Color c1, Color c2) {
		Color colorPrevious = g.getColor();
		try {
			g.setColor(c2);
			g.drawString(str, x + 1, y + 1);
			g.setColor(c1);
			g.drawString(str, x, y);
		} finally {
			g.setColor(colorPrevious);
		}
	}
	
	public static void drawShadowedString(Graphics g, String str, int x, int y, Color c) { 
		drawShadowedString(g, str, x, y, c, Util.shadowColor(c));
	}
}
