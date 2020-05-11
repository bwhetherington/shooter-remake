import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.MouseInputListener;

public class GSlider extends GComponent {
	private int sHeight = 30, sLocation;
	private boolean isSelected;
	private List<GSliderListener> listeners;
	private Color sliderColor;
	
	public GSlider(int width, int height) {
		super(width, height);
		listeners = new LinkedList<>();
		MouseInputListener listener = new MouseInputListener() {
			private Point lastPoint;
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				if (isSelected && lastPoint != null) {
					int dy = arg0.getPoint().y - lastPoint.y;
					moveSlider(dy);
				}
				lastPoint = arg0.getPoint();
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				isSelected = isValidY(arg0.getY());
				lastPoint = arg0.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				isSelected = false;
				lastPoint = arg0.getPoint();
			}	
		};
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}
	
	public double getPosition() {
		return ((double) sLocation) / (getHeight() - sHeight);
	}
	
	private boolean isValidY(int y) {
		return sLocation <= y && y <= sLocation + sHeight;
	}
	
	private void moveSlider(int dy) {
		sLocation += dy;
		validateSlider();
		dispatchGSliderEvent(new GSliderEvent(getPosition(), this));
	}
	
	private void validateSlider() {
		if (sLocation < 0) {
			sLocation = 0;
		}
		if (sLocation + sHeight > getHeight()) {
			sLocation = getHeight() - sHeight;
		}
	}
	
	public void renderComponent(Graphics g) {
		super.renderComponent(g);
		g.setColor(sliderColor);
		g.fillRect(0, sLocation, getWidth(), sHeight);
	}
	
	private void dispatchGSliderEvent(GSliderEvent event) {
		for (GSliderListener listener : listeners) {
			listener.sliderUpdated(event);
		}
	}
	
	public void addGSliderListener(GSliderListener listener) {
		listeners.add(listener);
	}
	
	public void setBorderColor(Color color) {
		super.setBorderColor(color);
		sliderColor = Util.modifyColor(color, 1.2);
	}
	
	public void setSliderRatio(double ratio) {
		sHeight = (int) Util.clamp(ratio * getHeight(), 10, getHeight());
	}
}
