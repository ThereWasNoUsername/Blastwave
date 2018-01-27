public enum Direction {
		UP, RIGHT, DOWN, LEFT, NONE;
    public Point getOffset(Point p) {
        switch(this) {
        case UP:
            return new Point(p.getX(), p.getY()+1);
            break;
        case RIGHT:
            return new Point(p.getX()+1, p.getY());
            break;
        case DOWN:
            return new Point(p.getX(), p.getY()-1);
            break;
        case LEFT:
            return new Point(p.getX()-1, p.getY());
            break;
        default:
            return p;
            break;
        }
    }
}
