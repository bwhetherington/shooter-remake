import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class MothershipCannonTurret extends Turret {

	public MothershipCannonTurret() {
		Sprite s = new Sprite(Util.loadSpriteSheet("mothership-cannon.sprite"));
		
		Region b = new Region(new Rectangle2D.Double(0, 0, 10, 10));
		Point2D[] hardPoints = {
				new Point2D.Double(),
				new Point2D.Double(72, 0)
		};
		setSprite(s);
		setBounds(b);
		setHardPoints(hardPoints);
		
		Weapon gun = new MothershipCannon();
		setWeapon(gun);
	}
	
	private class MothershipCannon extends Weapon {
		
		public MothershipCannon() {
			super(45, 50, 40, 9);
			setKickback(50);
		}

		public void fire() {
			for (int i = -1; i < 2; i++) {
				Region b = new Region(new Rectangle2D.Double(0, 0, 24, 24));
				Sprite s = new Sprite(Util.loadSpriteSheet("laser-bullet-round.sprite"));
				Mover m = (e) -> {
					e.applyMovement();
				};
				Effect e = (source, target) -> {
					if (target instanceof Unit) {
						((Unit) target).damage(12, MothershipCannonTurret.this, getLocation());
					}
				};
				Projectile bullet = new Projectile(MothershipCannonTurret.this, s, b, getAngle() + i * Math.toRadians(15), m, e) {
					protected void onDeath() {
						Impact.createLaser(getLocation(), getLevel());
					}
				};
				bullet.setMovementAcceleration(6);
				bullet.setMovementSpeed(12);
				bullet.setLocation(getHardPoint(Entity.WEAPON));
				getLevel().addEntity(bullet);
				playAnimation(Sprite.ATTACK);
			}
		}
		
	}
}