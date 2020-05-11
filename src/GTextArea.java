import java.awt.Font;

public class GTextArea extends GPanel {
	public static final int PADDING = 10;
	private int textHeight;
	
	public GTextArea(int width) {
		super(width, PADDING);
		this.setBackgroundPainted(true);
		setBackgroundColor(Colors.ENEMY_UI_COLOR);
	}
	
	public void append(GLabel label) {
		addGComponent(label, PADDING, textHeight);
		textHeight += label.getHeight();
		this.setHeight(PADDING + textHeight);
	}
	
}
