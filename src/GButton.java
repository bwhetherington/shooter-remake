
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.util.HashSet;
import java.util.Set;


public class GButton extends GComponent {
	private boolean isEnabled = true, isHovered, isPressed;
	private Color baseColor, hoverColor, pressColor;
	private String text;
	private Set<GButtonListener> listeners;
	private Font textFont;
	
	public static final Color
		CONFIRM_COLOR = new Color(32, 168, 48, 192),
		CANCEL_COLOR = new Color(168, 48, 32, 192),
		DEFAULT_COLOR = new Color(32, 96, 168, 192),
		DISABLED_COLOR = new Color(96, 96, 96, 192);
	
	public static final int
		PADDING_VERTICAL = 4,
		PADDING_HORIZONTAL = 6;
	
	public GButton(int width, int height, String text, Color color) {
		super(width, height);
		this.text = text;
		setBackgroundColor(color);
		listeners = new HashSet<>();
		textFont = new Font("default", Font.BOLD, 16);
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (isEnabled) {
					isHovered = true;
				}
			}
			
			public void mouseExited(MouseEvent e) {
				if (isEnabled) {
					isHovered = false;
				}
			}
			
			public void mousePressed(MouseEvent e) {
				if (isEnabled) {
					isPressed = true;
					dispatchEvent(new GButtonEvent(GButton.this));
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				if (isEnabled) {
					isPressed = false;
				}
			}
		});
	}
	
	public GButton(String text, Color color) {
		this(0, 0, text, color);
		int textWidth = Util.getStringWidth(text, textFont),
			textHeight = textFont.getSize();
		setWidth(textWidth + 2 * PADDING_HORIZONTAL);
		setHeight(textHeight + 2 * PADDING_VERTICAL);
	}
	
	public GButton(String text) {
		this(text, DEFAULT_COLOR);
	}
	
	public GButton(int width, int height, String text) {
		this(width, height, text, DEFAULT_COLOR);
	}
	
	public GButton(int width, int height) {
		this(width, height, "");
	}
	
	@Override
	public void renderComponent(Graphics g) {
		setBackgroundColorTemp(getCurrentColor());
		super.renderComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Rectangle clip = g.getClipBounds();
		g.setColor(getBorderColor());
		g.setFont(textFont);
		FontMetrics metrics = g.getFontMetrics();
		int strWidth = metrics.stringWidth(text),
			strHeight = metrics.getHeight(),
			x = clip.width / 2 - strWidth / 2,
			y = clip.height / 2 + strHeight / 3;
		GraphicsUtil.drawShadowedString(g, text, x, y, Color.WHITE);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	@Override
	public void setBackgroundColor(Color c) {
		super.setBackgroundColor(c);
		baseColor = c;
		hoverColor = Util.modifyColor(baseColor, 1.1);
		pressColor = Util.modifyColor(baseColor, 1.2);
	}
	
	private void setBackgroundColorTemp(Color c) {
		super.setBackgroundColor(c);
	}
	
	private void dispatchEvent(GButtonEvent e) {
		for (GButtonListener listener : listeners) {
			listener.buttonPressed(e);
		}
	}
	
	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
		setBackgroundColorTemp(enabled ? baseColor : DISABLED_COLOR);
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void addGButtonListener(GButtonListener listener) {
		listeners.add(listener);
	}
	
	public boolean isPressed() {
		return isPressed;
	}
	
	public boolean isHovered() {
		return isHovered;
	}
	
	protected Color getHoverColor() {
		return hoverColor;
	}
	
	public Color getBaseColor() {
		return baseColor;
	}
	
	protected Color getCurrentColor() {
		return isPressed ? pressColor : isHovered ? hoverColor : baseColor;
	}
}
