import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.event.MouseInputAdapter;

public class GPanel extends GComponent implements GContainer {
	private LinkedList<GComponent> components;
	private GComponent hoveredComponent, selectedComponent;

	public GPanel() {
		this(0, 0);
	}

	public GPanel(int width, int height) {
		super(width, height);
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

	public void addGComponent(GComponent component, int x, int y, Anchor anchor) {
		components.addFirst(component);
		component.setLocation(new Point(x, y));
		component.setAnchor(anchor);
		component.setParent(this);
	}

	public List<GComponent> getGComponents() {
		return components;
	}

	public void renderComponent(Graphics g) {
		super.renderComponent(g);
		for (GComponent component : components) {
			if (component.isVisible()) {
				component.render(g);
			}
		}
	}

	public void dispatchMouseEvent(MouseEvent e, int type) {
		MouseEvent 
			e2 = Util.cloneMouseEvent(e),
			e3 = Util.cloneMouseEvent(e);	
		if (type == GComponent.MOUSE_EXITED) {
			if (hoveredComponent != null) {
				hoveredComponent.processMouseEvent(e2, GComponent.MOUSE_EXITED);
				hoveredComponent = null;
			}
		}
		
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
				if (comp != hoveredComponent
						&& comp.getBounds().contains(e.getPoint())
						&& comp.isVisible()) {
					if (hoveredComponent != null) {
						hoveredComponent.processMouseEvent(e2,
								GComponent.MOUSE_EXITED);
					}
					comp.processMouseEvent(e2, GComponent.MOUSE_ENTERED);
					hoveredComponent = comp;
				}
			}
		}

		for (GComponent comp : components) {
			if (comp.isVisible() && comp.getBounds().contains(e.getPoint())) {
				if (type == GComponent.MOUSE_PRESSED) {
					selectedComponent = comp;
				}
				comp.processMouseEvent(e3, type);
			}
		}
	}
}
