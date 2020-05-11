import java.awt.Rectangle;
import java.awt.geom.Point2D;


public class Banshee extends EnemyShip {
	
	public Banshee() {
		Sprite s = new Sprite(Util.loadSpriteSheet("banshee.sprite"));
		setSprite(s);
		
		setLifeMax(180);
		setLife(180);
		setMovementSpeed(10);
		setMovementAcceleration(1);
		setTurningSpeed(0.1);
		setTurningAcceleration(0.01);

		Point2D[] hardPoints = {
				new Point2D.Double(),
				new Point2D.Double()
		};
		setHardPoints(hardPoints);

		Entity gun = new LaserTurret();
		
		attachEntity(gun, Entity.WEAPON);
		
		Rectangle r1 = new Rectangle(16, 16, 104, 28),
				  r2 = new Rectangle(16, 100, 104, 28),
				  r3 = new Rectangle(16, 44, 64, 56);
		setBounds(new Region(r1, r2, r3));
		setLifeRegen(2);
		
		setMass(7);
		
//		setShieldsMax(200);
//		setShields(200);
		setRadius(128);
	}
	
	protected void onDeath() {
		Impact.createMedium(getLocation(), getLevel());
	}
}
