package general;

import java.awt.image.BufferedImage;

public class Player extends Entity {
	Direction direction;	//The direction that the player will move on the next turn
	public Player(World world) {
		super(world);
	}
	//Are we ready to update the world? We wait until we know what the player wants to do
	public boolean isReady() {
		return direction != null;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	//Take a turn
	@Override
	public void update() {
		//See if we can move here
		if(world.isOpen(pos)) {
			world.move(this, direction.getOffset(pos));
		} else {
			world.addMessage("Your path is blocked by a wall!");
		}
		//Clear the move for the next turn
		move = null;
	}

	@Override
	public BufferedImage getTile() {
		return world.tiles.player;
	}

}
