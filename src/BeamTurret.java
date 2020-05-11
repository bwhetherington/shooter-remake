import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class BeamTurret extends Turret {
	public BeamTurret() {
		Sprite s = new Sprite(Util.loadSpriteSheet("laser.sprite"));
		
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
			super(50, 100, 80, 40);
			setKickback(0);
		}

		public void fire() {
			BeamTurret.this.playAnimation(Sprite.ATTACK);
			Beam.create(BeamTurret.this, Entity.WEAPON, getAngle(), 8192, 10);
		}
		
	}
}
