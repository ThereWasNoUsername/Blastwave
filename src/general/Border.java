package general;

import java.awt.Point;

public class Border {
	private final Point start, end;
	public Border(Point start, Point end) {
		this.start = start;
		this.end = end;
	}
	public void create(World world) {
		Point displacement = new Point(end.x - start.x, end.y - start.y);
		double distance = Math.sqrt(displacement.x * displacement.x + displacement.y * displacement.y);
		Point.Double normal = new Point.Double(displacement.x / distance, displacement.y / distance);
		Point.Double current = new Point.Double(start.x, start.y);
		while(current.distance(start) < distance+0.5) {
			world.addEntity(new Wall(world), new Point((int) current.x, (int) current.y));
			current = new Point.Double(current.x + normal.x, current.y + normal.y);
		}
	}
}
