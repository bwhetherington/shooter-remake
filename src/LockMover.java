
public class LockMover implements Mover {
	private Entity target;
	private boolean isLocked;
	
	public LockMover(Entity target) {
		this.target = target;
		ActionTimer timer = new ActionTimer(45, 1, () -> {
			isLocked = true;
		});
		timer.start();
	}

	@Override
	public void move(Entity e) {
		if (isLocked) {
			double angleToTarget = Util.angleBetweenPoints(e.getLocation(), target.getLocation());
			e.rotateTo(angleToTarget);
		}
		e.applyMovement();
	}
	
	
}
