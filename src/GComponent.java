
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;


public class GComponent {
	public static final Font DEFAULT_FONT = new Font("default", Font.BOLD, 16);
	
	public static final int
		// Mouse Events
		MOUSE_PRESSED = 9,
		MOUSE_RELEASED = 10,
		MOUSE_CLICKED = 11,
		MOUSE_ENTERED = 12,
		MOUSE_EXITED = 13,
		MOUSE_DRAGGED = 14,
		MOUSE_MOVED = 15;
	
	private static final Stroke
		BORDER_STROKE = new BasicStroke(4f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER),
		INNER_BORDER_STROKE = new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
	private int
		x,
		y,
		width,
		height;
	private Anchor anchor;
	private Color
		border,
		background;
	private boolean showBackground;
	private Rectangle clip;
	private Set<MouseListener> mouseListeners;
	private Set<MouseMotionListener> mouseMotionListeners;
	private Set<GComponentListener> componentListeners;
	private GContainer parent;
	private boolean isVisible;
	
	public GComponent() {
		this(0, 0);
	}
	
	public GComponent(int width, int height) {
		this.width = width;
		this.height = height;
//		border = Color.WHITE;
		setBackgroundColor(Colors.UI_COLOR);
		showBackground = true;
		clip = new Rectangle(0, 0, width, height);
		mouseListeners = new HashSet<>();
		mouseMotionListeners = new HashSet<>();
		componentListeners = new HashSet<>();
		isVisible = true;
	}
	
	public void render(Graphics g) {
		switch (anchor) {
		case TOP_LEFT:
			renderTL(g);
			break;
		case TOP_CENTER:
			renderTC(g);
			break;
		case TOP_RIGHT:
			renderTR(g);
			break;
		case CENTER_LEFT:
			renderLC(g);
			break;
		case CENTER:
			renderC(g);
			break;
		case CENTER_RIGHT:
			renderRC(g);
			break;
		case BOTTOM_LEFT:
			renderBL(g);
			break;
		case BOTTOM_CENTER:
			renderBC(g);
			break;
		case BOTTOM_RIGHT:
			renderBR(g);
			break;
		}
	}
	
	private void renderTL(Graphics g) {
		Rectangle bounds = g.getClipBounds();
		try {
			g.translate(getLeftX(bounds, x), getTopY(bounds, y));
			renderComponentFull(g);
		} finally {
			g.translate(-getLeftX(bounds, x), -getTopY(bounds, y));
		}
	}
	
	private void renderTC(Graphics g) {
		Rectangle bounds = g.getClipBounds();
		try {
			g.translate(getCenterX(bounds, x), getTopY(bounds, y));
			renderComponentFull(g);
		} finally {
			g.translate(-getCenterX(bounds, x), -getTopY(bounds, y));
		}
	}
	
	private void renderTR(Graphics g) {
		Rectangle bounds = g.getClipBounds();
		try {
			g.translate(getRightX(bounds, x), getTopY(bounds, y));
			renderComponentFull(g);
		} finally {
			g.translate(-getRightX(bounds, x), -getTopY(bounds, y));
		}
	}
	
	private void renderLC(Graphics g) {
		Rectangle bounds = g.getClipBounds();
		try {
			g.translate(getLeftX(bounds, x), getCenterY(bounds, y));
			renderComponentFull(g);
		} finally {
			g.translate(-getLeftX(bounds, x), -getCenterY(bounds, y));
		}
	}
	
	private void renderC(Graphics g) {
		Rectangle bounds = g.getClipBounds();
		try {
			g.translate(getCenterX(bounds, x), getCenterY(bounds, y));
			renderComponentFull(g);
		} finally {
			g.translate(-getCenterX(bounds, x), -getCenterY(bounds, y));
		}
	}
	
	private void renderRC(Graphics g) {
		Rectangle bounds = g.getClipBounds();
		try {
			g.translate(getRightX(bounds, x), getCenterY(bounds, y));
			renderComponentFull(g);
		} finally {
			g.translate(-getRightX(bounds, x), -getCenterY(bounds, y));
		}
	}
	
	private void renderBL(Graphics g) {
		Rectangle bounds = g.getClipBounds();
		try {
			g.translate(getLeftX(bounds, x), getBottomY(bounds, y));
			renderComponentFull(g);
		} finally {
			g.translate(-getLeftX(bounds, x), -getBottomY(bounds, y));
		}
	}
	
	private void renderBC(Graphics g) {
		Rectangle bounds = g.getClipBounds();
		try {
			g.translate(getCenterX(bounds, x), getBottomY(bounds, y));
			renderComponentFull(g);
		} finally {
			g.translate(-getCenterX(bounds, x), -getBottomY(bounds, y));
		}
	}
	
	private void renderBR(Graphics g) {
		Rectangle bounds = g.getClipBounds();
		try {
			g.translate(getRightX(bounds, x), getBottomY(bounds, y));
			renderComponentFull(g);
		} finally {
			g.translate(-getRightX(bounds, x), -getBottomY(bounds, y));
		}
	}
	
	private void renderComponentFull(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Color originalColor = g2d.getColor();
		Stroke originalStroke = g2d.getStroke();
		Rectangle originalClip = g.getClipBounds();
		try {
			g.setClip(getFullClip());
			if (showBackground) {
				g.setColor(background);
				g.fillRect(0, 0, width, height);
			}
			renderComponent(g);
			if (showBackground) {
				g.setColor(border);
				g2d.setStroke(BORDER_STROKE);
				g.drawRect(0, 0, width, height);
			}
		} finally {
			g2d.setColor(originalColor);
			g2d.setStroke(originalStroke);
			g.setClip(originalClip);
		}
	}
	
	public void renderComponent(Graphics g) {}
	
	protected int getLeftX(Rectangle r, int x) {
		return x; 
	}
	
	protected int getCenterX(Rectangle r, int x) {
		return r.width / 2 - width / 2 + x; 
	}
	
	protected int getRightX(Rectangle r, int x) {
		return r.width - width - x;
	}
	
	protected int getTopY(Rectangle r, int y) {
		return y; 
	}
	
	protected int getCenterY(Rectangle r, int y) {
		return r.height / 2 - height / 2 + y; 
	}
	
	protected int getBottomY(Rectangle r, int y) {
		return r.height - height - y;
	}
	
	public void setAnchor(Anchor anchor) {
		this.anchor = anchor;
	}
	
	public void setBackgroundPainted(boolean paint) {
		showBackground = paint;
	}
	
	public void setLocation(Point location) {
		x = location.x;
		y = location.y;
	}
	
	public void setX(int x) {
		this.x = x;
		clip.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
		clip.y = y;
	}
	
	public Point getLocation() {
		return new Point(x, y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setSize(Dimension size) {
		width = size.width;
		height = size.height;
	}
	
	public void setWidth(int width) {
		this.width = width;
		clip.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
		clip.height = height;
	}
	
	public Dimension getSize() {
		return new Dimension(width, height);
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setBackgroundColor(Color color) {
		background = color;
		Color brd = Util.modifyColor(color, 1.1);
		setBorderColor(new Color(brd.getRed(), brd.getGreen(), brd.getBlue()));
	}
	
	public void setBorderColor(Color color) {
		border = color;
	}
	
	public Color getBackgroundColor() {
		return background;
	}
	
	public Color getBorderColor() {
		return border;
	}
	
	public Rectangle getBounds() {
		if (parent == null) {
			return new Rectangle(x, y, width, height);
		} else {
			Dimension size = parent.getSize();
			Rectangle bounds = new Rectangle(0, 0, size.width, size.height);
			switch (anchor) {
			case TOP_LEFT:
				return new Rectangle(getLeftX(bounds, x), getTopY(bounds, y), width, height);
			case TOP_CENTER:
				return new Rectangle(getCenterX(bounds, x), getTopY(bounds, y), width, height);
			case TOP_RIGHT:
				return new Rectangle(getRightX(bounds, x), getTopY(bounds, y), width, height);
			case CENTER_LEFT:
				return new Rectangle(getLeftX(bounds, x), getCenterY(bounds, y), width, height);
			case CENTER:
				return new Rectangle(getCenterX(bounds, x), getCenterY(bounds, y), width, height);
			case CENTER_RIGHT:
				return new Rectangle(getRightX(bounds, x), getCenterY(bounds, y), width, height);
			case BOTTOM_LEFT:
				return new Rectangle(getLeftX(bounds, x), getBottomY(bounds, y), width, height);
			case BOTTOM_CENTER:
				return new Rectangle(getCenterX(bounds, x), getBottomY(bounds, y), width, height);
			case BOTTOM_RIGHT:
				return new Rectangle(getRightX(bounds, x), getBottomY(bounds, y), width, height);
			default:
				return new Rectangle(x, y, width, height);
			}
		}
	}
	
	public void addMouseListener(MouseListener listener) {
		mouseListeners.add(listener);
	}
	
	public void addMouseMotionListener(MouseMotionListener listener) {
		mouseMotionListeners.add(listener);
	}
	
	public void processMouseEvent(MouseEvent e, int type) {
		Rectangle bounds = getBounds();
		e.translatePoint(-bounds.x, -bounds.y);
		switch (type) {
		case MOUSE_PRESSED:
			for (MouseListener listener : mouseListeners) {
				listener.mousePressed(e);
			}
			break;
		case MOUSE_RELEASED:
			for (MouseListener listener : mouseListeners) {
				listener.mouseReleased(e);
			}
			break;
		case MOUSE_CLICKED:
			for (MouseListener listener : mouseListeners) {
				listener.mouseClicked(e);
			}
			break;
		case MOUSE_ENTERED:
			for (MouseListener listener : mouseListeners) {
				listener.mouseEntered(e);
			}
			break;
		case MOUSE_EXITED:
			for (MouseListener listener : mouseListeners) {
				listener.mouseExited(e);
			}
			break;
		case MOUSE_DRAGGED:
			for (MouseMotionListener listener : mouseMotionListeners) {
				listener.mouseDragged(e);
			}
			break;
		case MOUSE_MOVED:
			for (MouseMotionListener listener : mouseMotionListeners) {
				listener.mouseMoved(e);
			}
			break;
		}
	}
	
	public void processComponentEvent(GComponentEvent event) {
		for (GComponentListener listener : componentListeners) {
			switch (event.getType()) {
			case GComponentEvent.COMPONENT_OPENED:
				listener.componentOpened(event);
				break;
			case GComponentEvent.COMPONENT_CLOSED:
				listener.componentClosed(event);
				break;
			}
		}
	}
	
	public void setParent(GContainer parent) {
		this.parent = parent;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		GComponentEvent event = new GComponentEvent(this, isVisible ? GComponentEvent.COMPONENT_OPENED : GComponentEvent.COMPONENT_CLOSED);
		processComponentEvent(event);
	}
	
	public void addGComponentListener(GComponentListener listener) {
		componentListeners.add(listener);
	}
	
	public Rectangle getClip() {
		return clip;
	}
	
	public Rectangle getFullClip() {
		return parent == null ? clip : parent.getFullClip().intersection(clip);
	}
	
	public void setClip(Rectangle clip) {
		this.clip = clip;
	}
}
