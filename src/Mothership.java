import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Mothership extends BossEnemy {
	public Mothership() {
		Sprite s = new Sprite(Util.loadSpriteSheet("mothership.sprite"));
		setSprite(s);
		
		Point2D[] hardPoints = {
				new Point2D.Double(),
				new Point2D.Double(),
				null,
				null,
				new Point2D.Double(70, 70),
				new Point2D.Double(-70, 70),
				new Point2D.Double(70, -70),
				new Point2D.Double(-70, -70),
				new Point2D.Double(0, 80),
				new Point2D.Double(80, 0),
				new Point2D.Double(0, -80),
				new Point2D.Double(-80, 0)
		};
		setHardPoints(hardPoints);
		
		setUsesRotation(false);
		
		Turret 
			turret1 = new LaserTurret(),
			turret2 = new LaserTurret(),
			turret3 = new LaserTurret(),
			turret4 = new LaserTurret(),
			cannon = new MothershipCannonTurret();
		
		attachEntity(turret1, Entity.TURRET_ONE);
		attachEntity(turret2, Entity.TURRET_TWO);
		attachEntity(turret3, Entity.TURRET_THREE);
		attachEntity(turret4, Entity.TURRET_FOUR);
		attachEntity(cannon, Entity.WEAPON);
		
		setLifeMax(1800);
		setLife(1800);
//		setShieldsMax(1800);
//		setShields(1800);
		setLifeRegen(2);
		setMovementSpeed(3);
		setMovementAcceleration(1);
		setTurningSpeed(0.1);
		setTurningAcceleration(0.01);
		
		Dimension size = getSpriteSize();
		setBounds(new Region(new Ellipse2D.Double(0, 0, size.width, size.height)));
		
		setMass(50);
		setRadius(256);
	}
	
	protected void onDeath() {
		Impact.createLarge(getLocation(), getLevel());
	}
	
	@Override
	protected void onShieldsDamage(double angle) {
		Impact.createShieldPulseMassive(this, Entity.ORIGIN);
		Impact.createShieldImpactMassive(this, Entity.ORIGIN, angle);
	}
}
