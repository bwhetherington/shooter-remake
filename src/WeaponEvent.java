
public class WeaponEvent {
	public enum Type {
		FIRE_START,
		FIRE_STOP,
		FIRE,
		OVERHEAT,
		COOL_DOWN,
		HEAT_UP;
	}
	
	private Weapon source;
	private Type type;
	
	public WeaponEvent(Weapon source, Type type) {
		this.source = source;
		this.type = type;
	}
	
	public Weapon getSource() {
		return source;
	}
	
	public Type getType() {
		return type;
	}
}
