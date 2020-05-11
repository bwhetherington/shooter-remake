
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class GLifeBar extends GComponent {
	private Unit target;
	private boolean showLife;
	
	public GLifeBar(int width, int height, Unit target) {
		super(width, height);
		this.target = target;
	}
	
	public GLifeBar(Unit target) {
		this(256, 24, target);
	}
	
	public void renderComponent(Graphics g) {
		super.renderComponent(g);
		g.setColor(Colors.OUTLINE);
		int width = (int) (target.getLife() / target.getLifeMax() * getWidth());
		
		double shields = target.getShields(),
			   shieldsMax = target.getShieldsMax(),
			   shieldsPrevious = target.getShieldsPrevious();
		int barHeight = target.getShields() > 0 ? getHeight() / 2 : getHeight();
		if (shields > 0) {
			// Render shield bar
			int widthS = (int) (Util.clamp(shields, 0, shieldsMax) / shieldsMax * getWidth());
			
			// Draw remainder
			int remainderS = (int) (Util.clamp(shieldsPrevious - shields, 0, shieldsMax) / shieldsMax * getWidth());
			g.setColor(Colors.TEMPERATURE_BAR_OVERHEATED_FOREGROUND);
			g.fillRect(widthS, 0, remainderS, barHeight);
			
			// Draw life
			g.setColor(Colors.SHIELDS_BAR_FOREGROUND);
			g.fillRect(0, 0, widthS, barHeight);
		}
		
		// Draw remainder
		g.setColor(Color.WHITE);
		g.fillRect(width, getHeight() - barHeight, (int) ((target.getLifePrevious() - target.getLife()) / target.getLifeMax() * getWidth()),
				barHeight);
		
		g.setColor(Colors.LIFE_BAR_FOREGROUND);
		g.fillRect(0, getHeight() - barHeight, width, barHeight);
		if (showLife) {
			Graphics2D g2d = (Graphics2D) g;
			Object previousValue = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
			Font previousFont = g.getFont();
			try {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(Color.WHITE);
				g.setFont(GComponent.DEFAULT_FONT);
				String str = String.format("%.0f / %.0f", target.getLife(), target.getLifeMax());
				GraphicsUtil.drawShadowedString(g2d, str, 10, getHeight() / 2 + g.getFontMetrics().getHeight() / 3, Color.WHITE);
//				g.drawString(str, 10, getHeight() / 2 + g.getFontMetrics().getHeight() / 3);
			} finally {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, previousValue);
				g.setFont(previousFont);
			}
		}
	}
	
	public void setShowLife(boolean show) {
		showLife = show;
	}
	
	public boolean isShowingLife() {
		return showLife;
	}
	
}
