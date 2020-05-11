
public interface WeaponListener {
	
	void fireStart(WeaponEvent event);
	
	void fireStop(WeaponEvent event);
	
	void fire(WeaponEvent event);
	
	void overheat(WeaponEvent event);
	
	void coolDown(WeaponEvent event);
	
	void heatUp(WeaponEvent event);
}
