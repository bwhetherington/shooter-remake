import java.util.Set;


public class Projectile extends Entity {
	private Mover mover;
	private Effect impact;
	private Entity source;
	
	public Projectile(Entity source, Sprite s, Region b, double angle, Mover m, Effect impact) {
		super(b, s);
		setAngle(angle);
		this.mover = m;
		this.impact = impact;
		this.source = source;
		setCullable(true);
		applyForce(source.getVelocity());
		setRestitution(0);
	}
	
	public void setMover(Mover m) {
		this.mover = m;
	}

	protected void updateEntity() {
		super.updateEntity();
		Set<Entity> impacted = getLevel().getEntitiesInBounds(getBounds());
		boolean didImpact = false;
		if (!impacted.isEmpty()) {
			if (impact != null) {
				for (Entity e : impacted) {
					if (e.isCollidable() && !(e == source || e == this || source.belongsTo(e) || e instanceof Projectile)) {
						impact.execute(this, e);
						e.applyForce(getVelocity().mult(getMass()));
						didImpact = true;
					}
				}
			}
			if (didImpact) {
				kill();
			}
		}
	}
	
	protected void updateLocation() {
		mover.move(this);
		super.updateLocation();
	}
	
	public void setImpact(Effect e) {
		this.impact = e;
	}
	
}
