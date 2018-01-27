package general;
import java.awt.Point;

public enum Direction {
		UP, RIGHT, DOWN, LEFT, NONE;
    public Point getOffset(Point p) {
        switch(this) {
        case UP:
            return new Point(p.x, p.y+1);
        case RIGHT:
            return new Point(p.x+1, p.y);
        case DOWN:
            return new Point(p.x, p.y-1);
        case LEFT:
            return new Point(p.x-1, p.y);
        default:
            return p;
        }
    }
}
