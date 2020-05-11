import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class EnemyShip extends Unit {
	
	private double
		followRadius,
		aiCounterCurrent,
		aiCounterTarget;
	
	public EnemyShip() {
		aiCounterTarget = Util.randomInt(20, 80);
		followRadius = 1000;
		setShowsName(true);
	}
	
	public void setFollowRadius(double followRadius) {
		this.followRadius = followRadius;
	}
	
	public void updateEntity() {
		PlayerShip player = getLevel().getPlayer();
		if (aiCounterCurrent >= aiCounterTarget) {
			aiCounterCurrent = 0;
			aiCounterTarget = Util.randomInt(20, 80);
			if (player != null) {
				Point2D loc = getLocation(), pLoc = player.getLocation();
				if (Util.distance(loc, pLoc) > followRadius) {
					rotateTo(Util.angleBetweenPoints(loc, pLoc));
				} else {
					rotateTo(Util.randomAngle());
				}
			}
		}
		aiCounterCurrent++;
		applyMovement();
		if (player != null) {
			rotateTurrets(player.getLocation());
			fireStart();
		}
		super.updateEntity();
	}

	public boolean isAllyOf(Entity other) {
		if (super.isAllyOf(other)) {
			return true;
		} else {
			return other instanceof EnemyShip;
		}
	}
	
	public Color getNameColor() {
		return Colors.ENEMY_COLOR;
	}
	
//	@Override
//	public void renderRadarIconEntity(Graphics g, Rectangle2D radarArea, double radius) {
//		Point2D
//			location = new Point2D.Double(getLocation().getX(), getLocation().getY()),
//			point = Util.pointOnEdgeOfRectangle(radarArea, Util.angleBetweenPoints(Util.getRectangleCenter(radarArea), location));
//		int dx = 0, dy = 0;
//		if (!radarArea.contains(location)) {
//			dx = (int) ((point.getX() - location.getX()) * radius * 2 / radarArea.getWidth());
//			dy = (int) ((point.getY() - location.getY()) * radius * 2 / radarArea.getHeight());
//			System.out.printf("%d, %d%n", dx, dy);
//		}
//		try {
//			g.translate(dx, dy);
//			renderRadarIcon(g);
//		} finally {
//			g.translate(-dx, -dy);
//		}
//	}
}
