package general;

import java.awt.Point;

public class Box {
	Point corner1, corner2;
	public Box(Point corner1, Point corner2) {
		this.corner1 = corner1;
		this.corner2 = corner2;
	}
	public void create(World w) {
		int xMin = Math.min(corner1.x, corner2.x);
		int xMax = Math.max(corner1.x, corner2.x);
		
		int yMin = Math.min(corner1.y, corner2.y);
		int yMax = Math.max(corner1.y, corner2.y);
		
		new Border(new Point(xMin, yMin), new Point(xMax, yMin)).create(w);
		new Border(new Point(xMin, yMax), new Point(xMax, yMax)).create(w);
		new Border(new Point(xMin, yMin), new Point(xMin, yMax)).create(w);
		new Border(new Point(xMax, yMin), new Point(xMax, yMax)).create(w);
	}
}
