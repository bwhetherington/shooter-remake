
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Set;


public class GRadar extends GComponent {
	private Point2D point, radarPoint;
	private Point drawPoint;
	private Rectangle2D radarArea;
	private Set<Unit> units;
	private Color radarColor;
	private double radius;
	private Level level;
	private Unit target;
	private boolean showCoordinates;
	
	public GRadar(double radius, Unit target, Level level) {
		super(225, 225);
		this.radius = radius;
		this.target = target;
		this.level = level;
	}
	
	public void renderComponent(Graphics g) {
		super.renderComponent(g);
		if (this.level != null) {
			Font f = g.getFont();
			Color c = g.getColor();
			try {
				point = target.getLocation();
				double padding = 100;
				radarArea = new Rectangle2D.Double(point.getX() - radius, point.getY() - radius, radius * 2, radius * 2);
				for (Entity e : level.getEntities()) {
					e.renderRadarIconEntity(g, radarArea, getSize(), radius, padding);
				}
				if (showCoordinates) {
					Graphics2D g2d = (Graphics2D) g;
					Object previousValue = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g.setColor(Color.WHITE);
					g.setFont(GComponent.DEFAULT_FONT);
					String str = String.format("(%.0f, %.0f)", point.getX() / 100, point.getY() / 100);
					GraphicsUtil.drawShadowedString(g2d, str, 10, getHeight() - (g.getFontMetrics().getHeight() / 2), Colors.OUTLINE);
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, previousValue);
				}
			} finally {
				g.setFont(f);
				g.setColor(c);
			}
		}
	}
	
	public void setShowCoordinates(boolean show) {
		showCoordinates = show;
	}
	
	public boolean isShowingCoordinates() {
		return showCoordinates;
	}
	
}
