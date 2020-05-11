import java.awt.Graphics;
import java.awt.geom.*;
import java.awt.*;

public class Beam extends Entity {
	private double target, distance;
	private Effect effect;
	private Entity source;
	private ActionTimer timer;
	
	private static final double DISTANCE = 8192, WIDTH = 8;
	
	private Beam(Entity source, int attachPoint, double target, double distance, double duration) {
		this.target = target;
		this.distance = distance;

//		setSprite(new Sprite(Util.loadSpriteSheet("laser-beam.sprite")));
		
		timer = new ActionTimer(duration, 1) {
			public void perform() {
				kill();
				destroy();
			}
		};
		timer.start();
		
		Region r = new Region(new Rectangle2D.Double(0, 0, distance * 2, WIDTH));
		setBounds(r);
		
		setAngle(target);		
		source.attachEntity(this, attachPoint);
		
		Renderer renderer = (g) -> {
			double angle = getAngle();
			
			Graphics2D g2d = (Graphics2D) g;
			try {
				g2d.rotate(angle, distance, WIDTH / 2);
				g2d.setColor(Colors.ENEMY_LIFE_BAR_FOREGROUND);
				g2d.fillRect((int) distance, 0, (int) distance, (int) WIDTH); 
			} finally {
				g2d.rotate(-angle, distance, WIDTH / 2);
			}
		};
		addRenderer(renderer);
	}
	
	public void updateEntity() {
		super.updateEntity();
		for (Unit u : getLevel().getUnitsInBounds(getBounds())) {
			double angle = Util.angleBetweenPoints(getLocation(), u.getLocation());
			if (Util.normalizeAngle(angle - getAngle()) < 2 * Math.PI / 3) {
				if (!(belongsTo(u) || isAllyOf(u))) {
					u.damage(14, this);
					Impact.createLaser(Util.randomPointInRadius(u.getLocation(), 50), getLevel());
				}
			}
		}
	}
	
	public void kill() {
		timer.destroy();
		super.kill();
	}
	
	public static void create(Entity source, int attachPoint, double target, double distance, double duration) {
		new Beam(source, attachPoint, target, distance, duration);
	}
	
}