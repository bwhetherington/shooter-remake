import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class BossEnemy extends EnemyShip {
	
	public BossEnemy() {
		setRadarIcon(new Sprite(Util.loadSpriteSheet("skull-icon.sprite")));
		setOverheadIcon(new Sprite(Util.loadSpriteSheet("skull-icon-large.sprite")));
		setBarWidth(110);
	}
	
//	@Override
//	public void renderRadarIconEntity(Graphics g, Rectangle2D radarArea) {
//		Point2D
//			location = getLocation(),
//			point = Util.pointOnEdgeOfRectangle(radarArea, Util.angleBetweenPoints(Util.getRectangleCenter(radarArea), location));
//		int
//			dx = (int) (point.getX() - location.getX()),
//			dy = (int) (point.getY() - location.getY());
//		try {
//			g.translate(dx, dy);
//			renderRadarIcon(g);
//		} finally {
//			g.translate(-dx, -dy);
//		}
//	}
}
