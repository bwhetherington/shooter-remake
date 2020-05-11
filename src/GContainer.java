
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.List;


public interface GContainer {
	int getWidth();
	int getHeight();
	
	Dimension getSize();
	
	void addGComponent(GComponent comp, int x, int y, Anchor anchor);
	
	default void addGComponent(GComponent comp, int x, int y) {
		addGComponent(comp, x, y, Anchor.TOP_LEFT);
	}
	
	default void addGComponent(GComponent comp, Anchor anchor) {
		addGComponent(comp, 0, 0, anchor);
	}
	
	default void addGComponent(GComponent comp) {
		addGComponent(comp, 0, 0, Anchor.TOP_LEFT);
	}
	
	List<GComponent> getGComponents();
	
	void dispatchMouseEvent(MouseEvent e, int type);
	
	default Rectangle getClip() {
		return new Rectangle(0, 0, getWidth(), getHeight());
	}
	
	default Rectangle getFullClip() {
		return getClip();
	}
}
