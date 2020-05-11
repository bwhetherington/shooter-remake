import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class LaserTurret extends Turret {

	public LaserTurret() {
		Sprite s = new Sprite(Util.loadSpriteSheet("laser.sprite"));
		
		Region b = new Region(new Rectangle2D.Double(0, 0, 10, 10));
		Point2D[] hardPoints = {
				new Point2D.Double(),
				new Point2D.Double(32, 0)
		};
		setSprite(s);
		setBounds(b);
		setHardPoints(hardPoints);
		
		Weapon gun = new Laser();
		setWeapon(gun);
	}
	
	private class Laser extends Weapon {
		
		public Laser() {
			super(50, 50, 40, 9);
			setKickback(1);
		}

		public void fire() {
			Region b = new Region(new Rectangle2D.Double(0, 0, 24, 24));
			Sprite s = new Sprite(Util.loadSpriteSheet("laser-bullet.sprite"));
			Mover m = (e) -> {
				e.applyMovement();
			};
			Effect e = (source, target) -> {
				if (target instanceof Unit) {
					((Unit) target).damage(12, LaserTurret.this, getLocation());
				}
			};
			Projectile bullet = new Projectile(LaserTurret.this, s, b, getAngle(), m, e) {
				protected void onDeath() {
					Impact.createLaser(getLocation(), getLevel());
				}
			};
			bullet.setMass(0.5);
			bullet.setMovementAcceleration(6);
			bullet.setMovementSpeed(12);
			bullet.setLocation(getHardPoint(Entity.WEAPON));
			getLevel().addEntity(bullet);
			playAnimation(Sprite.ATTACK);
		}
		
	}
}