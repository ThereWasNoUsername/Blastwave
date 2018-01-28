package general;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Camera extends Entity {
	private boolean[][] visibility;
	private int range;
	int empTicks;
	public Camera(World world, int range) {
		super(world);
		visibility = new boolean[world.width][world.height];
		this.range = range;
		int empTicks = 0;
	}
	@Override
	public void update() {
		empTicks--;
		
		visibility = new boolean[world.width][world.height];
		
		if(empTicks > 0)
			return;
		
		for(int xOffset = -range; xOffset-1 < range; xOffset++) {
			for(int yOffset = -range; yOffset-1 < range; yOffset++) {
				Point posOffset = new Point(pos.x + xOffset, pos.y + yOffset);
				if(!world.isValid(posOffset)) {
					continue;
				}
				//Set this point as visible if we can see it enough
				if(Helper.lineOfSight(world, pos, posOffset)) {
					//TO DO: Factor brightness into visibility (i.e. below 255 * 2/8 or above 255 * 6/8 will impact)
					
					//We emit some light
					double distance = posOffset.distance(pos);
					world.incBrightness(posOffset, (int) (204 / (Math.max(1, distance * distance / 6))));
					
					int brightness = world.getBrightness(posOffset);
					/*
					if(brightness < 255 * 1/4 && brightness > 255 * 3/4) {
						visibility[posOffset.x][posOffset.y] = true;
					}
					*/
					if(brightness > 255 * 1/4 /* && brightness < 255 * 3/4 */) {
						visibility[posOffset.x][posOffset.y] = true;
					}
				}
				try {
					//throw new Exception("Implement");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//If we can see the player, alert!
		Point playerPos = world.player.getPos();
		if(visibility[playerPos.x][playerPos.y]) {
			Helper.playSound(world.res.sound_spotted, -5);
			world.addMessage("You've been spotted!");
			world.alert(pos, range);
		}
	}
	@Override
	public double getOpacity() {
		// TODO Auto-generated method stub
		return 51;
	}

	@Override
	public BufferedImage getTile() {
		if(empTicks > 0)
			return world.getBrightness(pos) > 128 ? world.res.camera_emp : world.res.camera_dark_emp;
		return world.getBrightness(pos) > 128 ? world.res.camera : world.res.camera_dark;
	}
	@Override
	public void alert() { }
	@Override
	public void markVisibility() {
		for(int x = 0; x < world.width; x++) {
			for(int y = 0; y < world.height; y++) {
				if(visibility[x][y]) {
					world.setEnemyVisibility(new Point(x, y));
				}
			}
		}
	}
	public int getEMPTicks() {
		return empTicks;
	}
	@Override
	public void emp() {
		world.addMessage("You hear the sizzle of a Camera getting short-circuited.");
		empTicks = 18;
	}
}
