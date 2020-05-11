
public class Turret extends Entity {
	
	public void setWeapon(Weapon weapon) {
		super.setWeapon(weapon);
		if (weapon != null) {
			weapon.addWeaponListener(new  WeaponAdapter() {
				public void fire(WeaponEvent event) {
					Level level = getLevel();
					Entity parent = getTopParent();
					if (level != null) { 
						if (level.isCameraTarget(parent)) {
							level.shakeCamera(getAngle(), -weapon.getKickback() / getRootMass());
						}
						applyForce(getAngle(), -weapon.getKickback() / 3);
					}
				}
			});
		}
	}
	
}
