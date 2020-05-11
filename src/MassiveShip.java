import java.awt.geom.Point2D;

public class MassiveShip extends Ship {
	public void setLocation(Point2D location) {
		super.setLocation(location);
//		Impact.createWarpInMassive(location, getLevel());
	}
}