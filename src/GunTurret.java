import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class GunTurret extends Turret {

	public GunTurret() {
		Sprite s = new Sprite(Util.loadSpriteSheet("gun.sprite"));
		
		Region b = new Region(new Rectangle2D.Double(0, 0, 20, 20));
		Point2D[] hardPoints = {
				new Point2D.Double(),
				new Point2D.Double(32, 0)
		};
		setSprite(s);
		setBounds(b);
		setHardPoints(hardPoints);
		
		Weapon gun = new Gun();
		setWeapon(gun);
	}
	
	private class Gun extends Weapon {
		
		public Gun() {
			super(4, 100, 80, 3);
			setKickback(17);
		}

		public void fire() {
			for (int i = 0; i < 1; i++) {
				Region b = new Region(new Rectangle2D.Double(0, 0, 12, 8));
				Sprite s = new Sprite(Util.loadSpriteSheet("bullet.sprite"));
				Mover m = (e) -> {
					e.applyMovement();
				};
				Effect e = (source, target) -> {
					if (target instanceof Unit) {
						((Unit) target).damage(24, GunTurret.this, getLocation());
					}
				};
				Projectile bullet = new Projectile(GunTurret.this, s, b, Util.deviateAngle(getAngle(), 0.1), m, e) {
					protected void onDeath() {
						Impact.createSmall(getLocation(), getLevel());
					}
				};
				bullet.setMass(0.5);
				double speed = Util.randomDouble(23, 27);
				bullet.setMovementAcceleration(speed);
				bullet.setMovementSpeed(speed);
				bullet.setLocation(getHardPoint(Entity.WEAPON));
				getLevel().addEntity(bullet);
				GunTurret.this.playAnimation(Sprite.ATTACK);
			}
		}
		
	}
}
