import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.Set;

public class GWindow extends GPanel {
	public static final int WINDOW_BAR_HEIGHT = 24, PADDING = 5;
	
	private GPanel contentPane, windowBar;
	
	public GWindow(int width, int height, String name) {
		super(width, height);
		setBackgroundPainted(false);
		
		// Initialize window bar
		windowBar = new GPanel(width, WINDOW_BAR_HEIGHT);
		windowBar.setBackgroundPainted(false);
		
		GButton closeButton = new GButton(22, 22, "X");
		closeButton.setBackgroundColor(Colors.ENEMY_COLOR);
		closeButton.addGButtonListener(new GButtonAdapter() {
			public void buttonPressed(GButtonEvent e) {
				setVisible(false);
			}
		});
		windowBar.addGComponent(closeButton, 1, 1, Anchor.TOP_RIGHT);
		
		GLabel windowLabel = new GLabel(name);
		windowBar.addGComponent(windowLabel, 0, 0, Anchor.CENTER);
		contentPane = new GPanel(width - 2, height - WINDOW_BAR_HEIGHT - PADDING - 2);
		super.addGComponent(windowBar, 0, 0, Anchor.TOP_LEFT);
		super.addGComponent(contentPane, 1, WINDOW_BAR_HEIGHT + PADDING + 1, Anchor.TOP_LEFT);
	}
	
	public GContainer getContentPane() {
		return contentPane;
	}
}
