
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class GDebugger extends GComponent {
	private Level level;
	
	private static final Font
		TITLE_FONT = new Font("default", Font.BOLD, 16),
		BODY_FONT = new Font("default", Font.PLAIN, 14);
	
	public GDebugger(Level level) {
		super(200, 108 + 26);
		this.level = level;
		setBackgroundColor(new Color(22, 23, 24, 128));
		setBorderColor(new Color(0, 0, 0, 0));
	}
	
	public void renderComponent(Graphics g) {
		super.renderComponent(g);
		g.setColor(Color.WHITE);
		g.setFont(TITLE_FONT);
		g.drawString("Level", 10, 26);
		g.setFont(BODY_FONT);
		int lineCount = 0;
		g.drawString(String.format("Framerate: %.0f", Game.TARGET_FRAME_RATE / level.getTimeScale()), 10, 26 + ((BODY_FONT.getSize() + 10) * ++lineCount));
		g.drawString(String.format("Total Entities: %d", level.getEntityCount()), 10, 26 + ((BODY_FONT.getSize() + 10) * ++lineCount));
		g.drawString(String.format("On-Screen Entities: %d", level.getOnScreenEntityCount()), 10, 26 + ((BODY_FONT.getSize() + 10) * ++lineCount));
		g.drawString(String.format("Active Timers: %d", Timer.count()), 10, 26 + ((BODY_FONT.getSize() + 10) * ++lineCount));
	}
}
