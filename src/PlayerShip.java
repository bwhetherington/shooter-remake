import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


public class PlayerShip extends Unit {
	private Turret turret;
	
	public PlayerShip() {
		Point2D[] hardPoints = {
				new Point2D.Double(),
				new Point2D.Double(),
				new Point2D.Double(-36, -48),
				new Point2D.Double(-36, 48)
		};
		setHardPoints(hardPoints);
		Rectangle r1 = new Rectangle(4, 0, 48, 128),
				  r2 = new Rectangle(0, 40, 128, 48);
		setBounds(new Region(r1, r2));
		Sprite s = new Sprite(Util.loadSpriteSheet("player-ship.sprite"));
		setSprite(s);
		setLifeMax(500);
		setLife(500);
		setMovementSpeed(20);
		setMovementAcceleration(1.5);
		setTurningSpeed(0.1);
		setTurningAcceleration(0.02);
		
		turret = new GunTurret();
		attachEntity(turret, Entity.WEAPON);
		
//		setShowsLifeBar(false);
		setLifeRegen(1);
		setMass(30);
		setInvulnerable(false);
		setArmor(10);
		
		setShieldsMax(200);
		setShields(200);
		setRadius(128);
	}
	
	protected void onDeath() {
		Impact.createMedium(getLocation(), getLevel());
	}
	
	public Turret getTurret() {
		return turret;
	}
	
	public String getName() {
		return "Player Ship";
	}
	
	public void renderRadarIcon(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Object previousValue = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		try {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			BufferedImage icon = Util.rotateImage(Util.loadImage("arrow-icon.png"), getAngle());
			g.drawImage(icon, -icon.getWidth() / 2, -icon.getHeight() / 2, null);
		} finally {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, previousValue);
		}
	}
}
