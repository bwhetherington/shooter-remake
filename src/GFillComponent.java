
public class GFillComponent extends GComponent {
	private GContainer parent;
	
	public GFillComponent(GContainer parent) {
		this.parent = parent;
		ActionTimer timer = new ActionTimer(0, () -> {
			this.setSize(parent.getSize());
		});
		timer.start();
		this.setBackgroundPainted(false);
	}
}
