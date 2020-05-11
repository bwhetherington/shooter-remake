import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Unit extends Entity {
	private static final double
		LIFE_BAR_DEGEN_RATE = 0.01;
	
	private double
		life,
		lifeMax,
		lifeRegen,
		lifePrevious,
		lifeIncrement,
		shields,
		shieldsMax,
		shieldsIncrement,
		shieldsPrevious,
		shieldsRegen,
		shieldsDelay,
		armor;
	private boolean 
		showLifeBar,
		showName,
		isInvulnerable;
	private static final Font
		NAME_FONT = new Font("normal", Font.BOLD, 16);

	public Unit() {
		showLifeBar = true;
		Renderer healthBarRenderer = (g) -> {
			if (showLifeBar && getToughness() < getToughnessMax()) {
				Dimension size = getSpriteSize();
				int dx = (int) (((double) size.getWidth() - getBarWidth()) / 2), dy = -20;
				g.translate(dx, dy);
				Color c = g.getColor();
				try {
					Color bg, brd, l;
					bg = this instanceof EnemyShip ? Colors.ENEMY_UI_COLOR : Colors.UI_COLOR;
					brd = this instanceof EnemyShip ? Colors.ENEMY_UI_BORDER : Colors.UI_BORDER;
					l = this instanceof EnemyShip ? Colors.ENEMY_LIFE_BAR_FOREGROUND : Colors.LIFE_BAR_FOREGROUND;
					g.setColor(bg);
					g.fillRect(0, 0, getBarWidth(), getBarHeight());
					int width = (int) (Util.clamp(life, 0, lifeMax) / lifeMax * getBarWidth());
					
					int barHeight = shields > 0 ? getBarHeight() / 2 : getBarHeight();
					if (shields > 0) {
						// Render shield bar
						int widthS = (int) (Util.clamp(shields, 0, shieldsMax) / shieldsMax * getBarWidth());
						
						// Draw remainder
						int remainderS = (int) (Util.clamp(shieldsPrevious - shields, 0, shieldsMax) / shieldsMax * getBarWidth());
						g.setColor(Colors.TEMPERATURE_BAR_OVERHEATED_FOREGROUND);
						g.fillRect(widthS, 0, remainderS, barHeight);
						
						// Draw life
						g.setColor(brd);
						g.fillRect(0, 0, widthS, barHeight);
					}
					
					// Draw remainder
					int remainder = (int) (Util.clamp(lifePrevious - life, 0, lifeMax) / lifeMax * getBarWidth());
					g.setColor(Colors.TEMPERATURE_BAR_OVERHEATED_FOREGROUND);
					g.fillRect(width, 0, remainder, barHeight);
					
					// Draw life
					g.setColor(l);
					g.fillRect(0, getBarHeight() - barHeight, width, barHeight);
					
					// Draw outline
					g.setColor(brd);
					g.drawRect(0, 0, getBarWidth(), getBarHeight());
				} finally {
					g.translate(-dx, -dy);
					g.setColor(c);
				}
			}
		};
		addRenderer(healthBarRenderer);
		
		Renderer nameRenderer = (g) -> {
			if (showName) {
				Dimension size = getSpriteSize();
				String name = getName();
				Rectangle2D textSize = g.getFontMetrics(NAME_FONT).getStringBounds(name, g);
				int dx = (int) ((size.width - textSize.getWidth()) / 2), dy = (int) (-20 - getBarHeight() + textSize.getHeight() / 2 - 5);
				g.translate(dx, dy);
				Color c = g.getColor();
				Font f = g.getFont();
				try {
					g.setColor(getNameColor());
					g.setFont(NAME_FONT);
					GraphicsUtil.drawShadowedString(g, name, 0, 0, getNameColor());
				} finally {
					g.translate(-dx, -dy);
					g.setColor(c);
					g.setFont(f);
				}
			}
		};
		addRenderer(nameRenderer);
		
		setCollidable(true);
		setShowRadarIcon(true);
	}

	public void updateEntity() {
		super.updateEntity();
		if (life <= 0) {
			kill();
		}
		// Life
		incrementLife(lifeRegen * getTimeScale() / 60);
		double lifeIncrement = this.lifeIncrement * getTimeScale();
		if (life >= lifePrevious) {
			lifePrevious = life;
		} else {
			lifePrevious -= lifeIncrement;
		}
		
		// Shields
		double shieldsIncrement = this.shieldsIncrement * getTimeScale();
		if (shields >= shieldsPrevious) {
			shieldsPrevious = shields;
		} else {
			shieldsPrevious -= shieldsIncrement;
		}
	}
	
	public void setLifeRegen(double regen) {
		this.lifeRegen = regen;
	}

	public void setLife(double life) {
		this.life = life;
		if (this.life > lifeMax) {
			this.life = lifeMax;
		}
	}
	
	public void incrementLife(double inc) {
		setLife(this.life + inc);
	}
	
	public double getLife() {
		return life;
	}

	public void setLifeMax(double lifeMax) {
		this.lifeMax = lifeMax;
		lifeIncrement = LIFE_BAR_DEGEN_RATE * lifeMax;
	}
	
	public double getLifeMax() {
		return lifeMax;
	}

	public void damage(double damage, Entity source, Point2D location) {
		damage = Util.clamp(damage - armor, 0, damage);
		if (source == null || getLevel().useFriendlyFire() || !source.isAllyOf(this)) {
			Level level = getLevel();
			if (level != null) {
				if (!isInvulnerable()) {
					double shieldsOld = shields;
					shields = Util.clamp(shields - damage, 0, shields);
					if (shields < shieldsOld) { 
						if (location != null) {
							onShieldsDamage(Util.angleBetweenPoints(getLocation(), location));
						}
					}
					life -= damage - (shieldsOld - shields);
				}
				if (level.isCameraTarget(this)) {
					double angle = (source == null) ? 
						Util.randomAngle() :
						Util.angleBetweenPoints(getLocation(), source.getLocation());
					level.shakeCamera(angle, 3);
				}		
			}
		}
	}
	
	public void damage(double damage, Entity source) {
		damage(damage, source, null);
	}

	public void damage(double damage) {
		damage(damage, null);
	}
	
	public boolean showsLifeBar() {
		return showLifeBar;
	}
	
	public void setShowsLifeBar(boolean showLifeBar) {
		this.showLifeBar = showLifeBar;
	}
	
	public boolean showsName() {
		return showName;
	}
	
	public void setShowsName(boolean showsName) {
		this.showName = showsName;
	}
	
	public void collide(Entity e, Point2D collision) {
		super.collide(e, collision);
		if (e instanceof Unit) {
			Unit unit = (Unit) e;
			if (Math.random() < 0.2 * getTimeScale()) {
				Impact.createSmall(collision, getLevel());
				unit.damage(getLifeMax() / 10, this);
			}
		}
	}
	
	public boolean isInvulnerable() {
		return isInvulnerable;
	}
	
	public void setInvulnerable(boolean invulnerable) {
		isInvulnerable = invulnerable;
	}
	
	public double getLifePrevious() {
		return lifePrevious;
	}
	
	public double getArmor() {
		return armor;
	}
	
	public void setArmor(double armor) {
		this.armor = armor;
	}
	
	public void setShields(double shields) {
		this.shields = shields;
	}
	
	public void setShieldsMax(double shieldsMax) {
		this.shieldsMax = shieldsMax;
		shieldsIncrement = shieldsMax * LIFE_BAR_DEGEN_RATE;
	}
	
	public void setShieldsRegen(double shieldsRegen) {
		this.shieldsRegen = shieldsRegen;
	}
	
	public void setShieldsDelay(double shieldsDelay) {
		this.shieldsDelay = shieldsDelay;
	}
	
	public double getShields() {
		return shields;
	}
	
	public double getShieldsMax() {
		return shieldsMax;
	}
	
	public double getShieldsRegen() {
		return shieldsRegen;
	}
	
	public double getShieldsDelay() {
		return shieldsDelay;
	}
	
	public double getShieldsPrevious() {
		return shieldsPrevious;
	}
	
	public double getToughness() {
		return life + shields;
	}
	
	public double getToughnessMax() {
		return lifeMax + shieldsMax;
	}
	
	public boolean intersects(Region other) {
		if (shields > 0) {
			Point2D location = getLocation();
			double r = getRadius();
			Ellipse2D newBounds = new Ellipse2D.Double(0, 0, r, r);
			Region re = new Region(newBounds);
			re.setLocation(location);
			return other.intersects(re);
		} else {
			return super.intersects(other);
		}
	}
	
	protected void onShieldsDamage(double angle) {
		Impact.createShieldPulse(this, Entity.ORIGIN);
		Impact.createShieldImpact(this, Entity.ORIGIN, angle);
	}
}
