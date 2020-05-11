
public class GSlidePanel extends GPanel {
	private static final int SLIDER_WIDTH = 20;
	private double progress;
	private GComponent component;
	
	public GSlidePanel(GComponent component, int height) {
		super(component.getWidth() + SLIDER_WIDTH, height);
		this.component = component;
		GSlider slider = new GSlider(SLIDER_WIDTH, height);
		GSliderListener listener = new GSliderListener() {
			public void sliderUpdated(GSliderEvent event) {
				if (component.getHeight() > getHeight()) {
					progress = event.getProgress();
					int bottom = component.getHeight() - getHeight();
					component.setY((int) (-progress * bottom));
				}
			}
		};
		slider.addGSliderListener(listener);
		addGComponent(component);
		addGComponent(slider, 0, 0, Anchor.TOP_RIGHT);
		
		ActionTimer timer = new ActionTimer(0, () -> {
			component.setClip(getFullClip());
			slider.setSliderRatio(Util.clamp((double) getHeight() / component.getHeight(), 0, 1));
		});
		timer.start();
	}
}
