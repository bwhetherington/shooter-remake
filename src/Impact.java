import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public final class Impact {
	public static final double
		EXPLOSION_RADIUS_MEDIUM = 48,
		EXPLOSION_RADIUS_LARGE = 96,
		EXPLOSION_FRAGMENT_CHANCE = 0.08;
	
	private Impact() {}
	
	public static void createSmall(Point2D location, Level level) {
		final Entity e = new Entity();
		e.setBounds(new Region(new Rectangle2D.Double(0, 0, 1, 1)));
		e.setLocation(location);

		Sprite s = new Sprite(Util.loadSpriteSheet("explosion-small.sprite"));
		e.setSprite(s);

		level.addEntity(e);
		
		ActionTimer timer = new ActionTimer(0, ActionTimer.REPEAT) {
			public void perform() {
				if (e.getSprite().isDone()) {
					e.kill();
					destroy();
				}
			}
		};
		timer.start();
	}
	
	public static void createMedium(Point2D location, Level level) {
		final Entity e = new Entity();
		e.setBounds(new Region(new Rectangle2D.Double(0, 0, 1, 1)));
		e.setLocation(location);

		Sprite s = new Sprite(Util.loadSpriteSheet("explosion-large.sprite"));
		e.setSprite(s);

		level.addEntity(e);
		
		ActionTimer timer = new ActionTimer(0, ActionTimer.REPEAT) {
			public void perform() {
				if (Math.random() < EXPLOSION_FRAGMENT_CHANCE * level.getTimeScale()) {
					double
						dx = Math.random() * EXPLOSION_RADIUS_MEDIUM * 2 - EXPLOSION_RADIUS_MEDIUM,
						dy = Math.random() * EXPLOSION_RADIUS_MEDIUM * 2 - EXPLOSION_RADIUS_MEDIUM;
					Point2D fragmentLocation = new Point2D.Double(location.getX() + dx, location.getY() + dy);
					createSmall(fragmentLocation, level);
				}
				if (e.getSprite().isDone()) {
					e.kill();
					destroy();
				}
			}
		};
		timer.start();
	}
	
	public static void createLarge(Point2D location, Level level) {
		final Entity e = new Entity();
		e.setBounds(new Region(new Rectangle2D.Double(0, 0, 1, 1)));
		e.setLocation(location);

		Sprite s = new Sprite(Util.loadSpriteSheet("explosion-large.sprite"));
		e.setSprite(s);

		level.addEntity(e);
		
		ActionTimer timer = new ActionTimer(0, ActionTimer.REPEAT) {
			public void perform() {
				if (Math.random() < EXPLOSION_FRAGMENT_CHANCE * level.getTimeScale()) {
					double
						dx = Math.random() * EXPLOSION_RADIUS_LARGE * 2 - EXPLOSION_RADIUS_LARGE,
						dy = Math.random() * EXPLOSION_RADIUS_LARGE * 2 - EXPLOSION_RADIUS_LARGE;
					Point2D fragmentLocation = new Point2D.Double(location.getX() + dx, location.getY() + dy);
					createMedium(fragmentLocation, level);
				}
				if (e.getSprite().isDone()) {
					e.kill();
					destroy();
				}
			}
		};
		timer.start();
	}
	
	public static void createLaser(Point2D location, Level level) {
		final Entity e = new Entity();
		e.setBounds(new Region(new Rectangle2D.Double(0, 0, 1, 1)));
		e.setLocation(location);

		Sprite s = new Sprite(Util.loadSpriteSheet("laser-impact.sprite"));
		e.setSprite(s);


		level.addEntity(e);
		
		ActionTimer timer = new ActionTimer(0, ActionTimer.REPEAT) {
			public void perform() {
				if (e.getSprite().isDone()) {
					e.kill();
					destroy();
				}
			}
		};
		timer.start();
	}
	
	public static void createShieldPulse(Entity entity, int attachPoint) {
		final Entity e = new Entity();
		e.setBounds(new Region(new Rectangle2D.Double(0, 0, 1, 1)));
		entity.attachEntity(e, attachPoint);

		Sprite s = new Sprite(Util.loadSpriteSheet("shield-pulse.sprite"));
		e.setSprite(s);
		
		ActionTimer timer = new ActionTimer(0, ActionTimer.REPEAT) {
			public void perform() {
				if (e.getSprite().isDone()) {
					e.kill();
					destroy();
				}
			}
		};
		timer.start();
	}
	
	public static void createShieldImpact(Entity entity, int attachPoint, double angle) {
		final Entity e = new Entity();
		e.setBounds(new Region(new Rectangle2D.Double(0, 0, 1, 1)));
		entity.attachEntity(e, attachPoint);
		
		e.setAngle(angle);

		Sprite s = new Sprite(Util.loadSpriteSheet("shield-impact.sprite"));
		e.setSprite(s);
		
		ActionTimer timer = new ActionTimer(0, ActionTimer.REPEAT) {
			public void perform() {
				if (e.getSprite().isDone()) {
					e.kill();
					destroy();
				}
			}
		};
		timer.start();
	}
	
	public static void createShieldPulseMassive(Entity entity, int attachPoint) {
		final Entity e = new Entity();
		e.setBounds(new Region(new Rectangle2D.Double(0, 0, 1, 1)));
		entity.attachEntity(e, attachPoint);

		Sprite s = new Sprite(Util.loadSpriteSheet("shield-pulse-massive.sprite"));
		e.setSprite(s);
		
		ActionTimer timer = new ActionTimer(0, ActionTimer.REPEAT) {
			public void perform() {
				if (e.getSprite().isDone()) {
					e.kill();
					destroy();
				}
			}
		};
		timer.start();
	}
	
	public static void createShieldImpactMassive(Entity entity, int attachPoint, double angle) {
		final Entity e = new Entity();
		e.setBounds(new Region(new Rectangle2D.Double(0, 0, 1, 1)));
		entity.attachEntity(e, attachPoint);
		
		e.setAngle(angle);

		Sprite s = new Sprite(Util.loadSpriteSheet("shield-impact-massive.sprite"));
		e.setSprite(s);
		
		ActionTimer timer = new ActionTimer(0, ActionTimer.REPEAT) {
			public void perform() {
				if (e.getSprite().isDone()) {
					e.kill();
					destroy();
				}
			}
		};
		timer.start();
	}
}
