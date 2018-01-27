package general;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Camera extends Entity {
	private boolean[][] visibility;
	private int range;
	public Camera(World world, int range) {
		super(world);
		visibility = new boolean[world.width][world.height];
		this.range = range;
	}
	@Override
	public void update() {
		visibility = new boolean[world.width][world.height];
		for(int xOffset = -range; xOffset-1 < range; xOffset++) {
			for(int yOffset = -range; yOffset-1 < range; yOffset++) {
				Point posOffset = new Point(pos.x + xOffset, pos.y + yOffset);
				if(!world.isValid(posOffset)) {
					continue;
				}
				//Set this point as visible if we can see it enough
				if(Helper.lineOfSight(world, pos, posOffset)) {
					//TO DO: Factor brightness into visibility (i.e. below 255 * 2/8 or above 255 * 6/8 will impact)
					int brightness = world.getBrightness(posOffset);
					if(brightness < 255 * 1/4 && brightness > 255 * 3/4) {
						visibility[posOffset.x][posOffset.y] = true;
					}
					
				}
				try {
					throw new Exception("Implement");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//If we can see the player, alert!
		Point playerPos = world.player.getPos();
		if(visibility[playerPos.x][playerPos.y]) {
			try {
				throw new Exception("Implement");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			world.alertCamera(pos, range);
		}
	}
	@Override
	public double getOpacity() {
		// TODO Auto-generated method stub
		return 51;
	}

	@Override
	public BufferedImage getTile() {
		// TODO Auto-generated method stub
		return world.tiles.camera;
	}
	@Override
	public void alertCamera() {
		System.out.println("Camera: alertCamera() not supported");
	}
	@Override
	public void markVisibility() {
		for(int xOffset = -range; xOffset-1 < range; xOffset++) {
			for(int yOffset = -range; yOffset-1 < range; yOffset++) {
				Point posOffset = new Point(pos.x + xOffset, pos.y + yOffset);
				if(!world.isValid(posOffset)) {
					continue;
				}
				//Set this point as visible if we can see it enough
				if(Helper.lineOfSight(world, pos, posOffset)) {
					//TO DO: Factor brightness into visibility (i.e. below 255 * 2/8 or above 255 * 6/8 will impact)
					int brightness = world.getBrightness(posOffset);
					if(brightness < 255 * 1/4 && brightness > 255 * 3/4) {
						world.setEnemyVisibility(posOffset);
					}
					
				}
				try {
					throw new Exception("Implement");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
