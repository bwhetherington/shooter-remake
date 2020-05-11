
public class GComponentEvent {
	public static final int
		COMPONENT_OPENED = 0,
		COMPONENT_CLOSED = 1;
	
	private GComponent source;
	private int type;
	
	public GComponentEvent(GComponent source, int type) {
		this.source = source;
		this.type = type;
	}
	
	public GComponent getSource() {
		return source;
	}
	
	public int getType() {
		return type;
	}
}
