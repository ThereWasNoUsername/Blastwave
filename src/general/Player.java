package general;

import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import general.Player.PlayerState;

public class Player extends Entity {
	//TO DO: Add ability that temporary prevents all nearby wireless communications from functioning
	enum PlayerState {
		DEAD, ESCAPED, ACTIVE
	}
	private PlayerState state;
	private Direction direction;	//The direction that the player will move on the next turn
	private int empRadius;
	private int empCooldown;
	public Player(World world) {
		super(world);
		state = PlayerState.ACTIVE;
		empRadius = 0;
		empCooldown = 0;
	}
	public double getOpacity() {
		return 51;
	}
	//Are we ready to update the world? We wait until we know what the player wants to do
	public boolean isReady() {
		return state == PlayerState.ACTIVE && direction != null;
	}
	public void kill() {
		state = PlayerState.DEAD;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public void setEMPCooldown(int ticks) {
		empCooldown = ticks;
	}
	public int getEMPCooldown() {
		return empCooldown;
	}
	//Take a turn
	@Override
	public void update() {
		empCooldown--;
		if(empRadius > 0) {
			Helper.playSound(world.res.sound_blastwave, -7);
			
			//EMP makes a loud noise
			world.alert(pos, empRadius * 3);
			
			empRadius++;
			
			for(int x = -empRadius; x <= empRadius; x++) {
				for(int y = -empRadius; y <= empRadius; y++) {
					Point p = new Point(pos.x + x, pos.y + y);
					if(!world.isValid(p)) {
						continue;
					}
					if(!world.isOpen(p)) {
						world.getAt(p).emp();
					}
				}
			}
			
			if(empRadius >= 16) {
				empRadius = 0;
				world.addMessage("The air and light return to normal while you are left with a headache");
			}
		}
		
		Point pos_next = direction.getOffset(pos);
		//See if we can move here
		if(world.isOpen(pos_next)) {
			world.move(this, pos_next);
		} else if(world.getAt(pos_next) instanceof Wall) {
			world.addMessage("Your path is blocked by a wall!");
		} else if(world.getAt(pos_next) instanceof Exit) {
			world.removeEntity(pos);
			world.addMessage("You have escaped this facility!");
			world.addMessage("Press Backspace to restart the level or press F to load a custom level.");
			state = PlayerState.ESCAPED;
		} else if(world.getAt(pos_next) instanceof Guard && world.getAt(pos_next).getEMPTicks() > 0) {
			world.addMessage("You punch out the guard while they are dazed.");
			world.removeEntity(pos_next);
		}
		//Clear the move for the next turn
		direction = null;
	}

	@Override
	public BufferedImage getTile() {
		return world.getBrightness(pos) > 128 ? world.res.player : world.res.player_dark;
	}
	@Override
	public void alert() {
		
	}
	@Override
	public void markVisibility() {
		int range = 20;
		for(int xOffset = -range; xOffset-1 < range; xOffset++) {
			for(int yOffset = -range; yOffset-1 < range; yOffset++) {
				Point posOffset = new Point(pos.x + xOffset, pos.y + yOffset);
				if(!world.isValid(posOffset)) {
					continue;
				}
				//Set this point as visible if we can see it enough
				if(Helper.lineOfSight(world, pos, posOffset)) {
					
					int brightness = world.getBrightness(posOffset);
					/*
					if(brightness < 255 * 1/4 && brightness > 255 * 3/4) {
						world.setPlayerVisibility(posOffset);
					}
					*/
					//Can't see things in the dark if they are too far away
					if((brightness < (255 * 1/8)) && posOffset.distance(pos) > 3) {
						continue;
					}
					world.setPlayerVisibility(posOffset);
					
				} else {
				}
			}
		}
	}
	@Override
	public void emp() {}
	public void activateEMP() {
		world.clearMessages();
		world.addMessage("The air rumbles and the light bends as a minor headache descends upon you.");
		
		empCooldown = 48;
		empRadius = 1;
	}
	public PlayerState getState() {
		return state;
	}
	public int getEMPRadius() {
		return empRadius;
	}

}
