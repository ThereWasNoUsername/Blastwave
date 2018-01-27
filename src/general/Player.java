package general;

import java.awt.image.BufferedImage;

public class Player extends Entity {
	enum Direction {
		UP, RIGHT, DOWN, LEFT, NONE
	}
	Direction move;
	public Player(World world) {
		super(world);
	}
	public boolean isReady() {
		return move != null;
	}
	public void setMovement(Direction move) {
		this.move = move;
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public BufferedImage getTile() {
		// TODO Auto-generated method stub
		return null;
	}

}
