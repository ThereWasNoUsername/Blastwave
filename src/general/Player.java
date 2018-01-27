package general;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Player extends Entity {
	//TO DO: Add ability that temporary prevents all nearby wireless communications from functioning
	
	Direction direction;	//The direction that the player will move on the next turn
	public Player(World world) {
		super(world);
	}
	public double getOpacity() {
		return 51;
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
		Point pos_next = direction.getOffset(pos);
		//See if we can move here
		if(world.isOpen(pos_next)) {
			world.move(this, pos_next);
		} else {
			world.addMessage("Your path is blocked by a wall!");
		}
		//Clear the move for the next turn
		direction = null;
	}

	@Override
	public BufferedImage getTile() {
		return world.tiles.player;
	}
	@Override
	public void alertCamera() {
		world.addMessage("You've been spotted by a camera!");
	}
	@Override
	public void markVisibility() {
		int range = 50;
		for(int xOffset = -range; xOffset-1 < range; xOffset++) {
			for(int yOffset = -range; yOffset-1 < range; yOffset++) {
				Point posOffset = new Point(pos.x + xOffset, pos.y + yOffset);
				if(!world.isValid(posOffset)) {
					continue;
				}
				//Set this point as visible if we can see it enough
				if(Helper.lineOfSight(world, pos, posOffset)) {
					int brightness = world.getBrightness(posOffset);
					if(brightness < 255 * 1/4 && brightness > 255 * 3/4) {
						world.setPlayerVisibility(posOffset);
					}
					
				}
			}
		}
	}

}
