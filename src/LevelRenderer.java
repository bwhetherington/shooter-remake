import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;


public class LevelRenderer extends JComponent {
	private Level level;
	
	public LevelRenderer(Level level) {
		this.level = level;
	}
	
	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		level.setCameraSize(new Dimension(getWidth(), getHeight()));
		level.render(g);
	}
}
