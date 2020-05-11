
public class GSliderEvent {
	private double progress;
	private GSlider source;
	
	public GSliderEvent(double progress, GSlider source) {
		this.progress = progress;
		this.source = source;
	}
	
	public double getProgress() {
		return progress;
	}
	
	public GSlider getSource() {
		return source;
	}
}
