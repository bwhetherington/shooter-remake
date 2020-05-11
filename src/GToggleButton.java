
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.util.HashSet;


public class GToggleButton extends GButton {
	private Color toggleBaseColor, toggleHoverColor, togglePressColor;
	private boolean isToggled;
	
	public GToggleButton(int width, int height, String text, Color color) {
		super(width, height, text, color);
		initializeToggleListener();
	}
	
	public GToggleButton(String text, Color color) {
		super(text, color);
		initializeToggleListener();
	}
	
	public GToggleButton(String text) {
		super(text);
		initializeToggleListener();
	}
	
	public GToggleButton(int width, int height, String text) {
		super(width, height, text);
		initializeToggleListener();
	}
	
	public GToggleButton(int width, int height) {
		super(width, height);
		
	}
	
	public void setBackgroundColor(Color color) {
		super.setBackgroundColor(color);
		toggleBaseColor = Util.modifyColor(color, 1.2);
		toggleHoverColor = Util.modifyColor(toggleBaseColor, 1.1);
		togglePressColor = Util.modifyColor(toggleBaseColor, 1.2);
	};
	
	private void initializeToggleListener() {
		addGButtonListener(new GButtonAdapter() {
			public void buttonPressed(GButtonEvent e) {
				isToggled = !isToggled;
			}
		});
	}
	
	public boolean isToggled() {
		return isToggled;
	}
	
	public void setToggled(boolean toggled) {
		isToggled = toggled;
	}
	
	protected Color getCurrentColor() {
		return isToggled ? (isPressed() ? togglePressColor : isHovered() ? toggleHoverColor : toggleBaseColor) : super.getCurrentColor();
	}

}
