
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;


public class GController extends JComponent implements GContainer {
	private LinkedList<GComponent> components;
	private GComponent hoveredComponent, selectedComponent;
	
	public GController() {
		components = new LinkedList<>();
		MouseInputAdapter adapter = new MouseInputAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				dispatchMouseEvent(e, GComponent.MOUSE_CLICKED);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				dispatchMouseEvent(e, GComponent.MOUSE_PRESSED);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				selectedComponent = null;
				dispatchMouseEvent(e, GComponent.MOUSE_RELEASED);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				dispatchMouseEvent(e, GComponent.MOUSE_DRAGGED);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				dispatchMouseEvent(e, GComponent.MOUSE_MOVED);
			}
			
		};
		addMouseListener(adapter);
		addMouseMotionListener(adapter);
	}
	
	public void paintComponent(Graphics g) {
		for (GComponent component : components) {
			if (component.isVisible()) {
				component.render(g);
			}
		}
	}
	
	public void addGComponent(GComponent component, int x, int y, Anchor anchor) {
		components.addFirst(component);
		component.setLocation(new Point(x, y));
		component.setAnchor(anchor);
		component.setParent(this);
	}
	
	public void dispatchMouseEvent(MouseEvent e, int type) {
		MouseEvent 
			e2 = Util.cloneMouseEvent(e),
			e3 = Util.cloneMouseEvent(e);	

		if (type == GComponent.MOUSE_MOVED || type == GComponent.MOUSE_DRAGGED) {
			if (type == GComponent.MOUSE_MOVED && hoveredComponent != null
					&& !hoveredComponent.getBounds().contains(e.getPoint())
					|| !(hoveredComponent == null || hoveredComponent
							.isVisible())) {
				hoveredComponent.processMouseEvent(e2, GComponent.MOUSE_EXITED);
				hoveredComponent = null;
			}
			
			if (type == GComponent.MOUSE_DRAGGED && selectedComponent != null) {
				selectedComponent.processMouseEvent(e2, GComponent.MOUSE_DRAGGED);
			}
			
			for (GComponent comp : components) {
				if (comp.getBounds().contains(e.getPoint()) && comp.isVisible()) {
					if (comp != hoveredComponent) {
						if (hoveredComponent != null) {
							hoveredComponent.processMouseEvent(e2, GComponent.MOUSE_EXITED);
						}
						comp.processMouseEvent(e2, GComponent.MOUSE_ENTERED);
						hoveredComponent = comp;
					}
					break;
				}
			}
		}
		
		for (GComponent comp : components) {
			if (comp.isVisible() && comp.getBounds().contains(e.getPoint())) {
				comp.processMouseEvent(e3, type);
				if (type == GComponent.MOUSE_PRESSED) {
					selectedComponent = comp;
				}
				break;
			}
		}
	}
	
	public List<GComponent> getGComponents() {
		return components;
	}
}
