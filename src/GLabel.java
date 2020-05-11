import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;


public class GLabel extends GComponent {
	private String text;
	private Font textFont;
	private Color color;
	private boolean useAntialias;
	private int position;
	
	public static final int
		LEFT = 0,
		CENTER = 1,
		RIGHT = 2;
	
	public GLabel(String text, Font textFont, Color color) {
		super(Util.getStringWidth(text, textFont), Util.getStringHeight("Tg", textFont));
		this.text = text;
		this.textFont = textFont;
		this.color = color;
		useAntialias = true;
		setBackgroundPainted(false);
	}
	
	public GLabel(String text) {
		this(text, GComponent.DEFAULT_FONT, Colors.OUTLINE);
	}
	
	public void renderComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g.setFont(textFont);
		g.setColor(color);
		Object previousValue = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		Rectangle clip = g.getClipBounds();
		try {
			Rectangle newClip = new Rectangle(clip.x, clip.y, clip.width, clip.height + getHeight() / 2);
			g.setClip(newClip);
			if (useAntialias) {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			GraphicsUtil.drawShadowedString(g, text, 0, getHeight(), color);
		} finally {
			g.setClip(clip);
			if (useAntialias) {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, previousValue);
			}
		}
	}
}
