package general;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Exit extends Entity {

	public Exit(World world) {
		super(world);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		int range = 50;
		for(int xOffset = -range; xOffset-1 < range; xOffset++) {
			for(int yOffset = -range; yOffset-1 < range; yOffset++) {
				Point posOffset = new Point(pos.x + xOffset, pos.y + yOffset);
				if(!world.isValid(posOffset)) {
					continue;
				}
				if(Helper.lineOfSight(world, pos, posOffset)) {
					double distance = posOffset.distance(pos);
					world.incBrightness(posOffset, (int) (255 / (Math.max(1, distance * distance / 16))));
				}
			}
		}
	}

	@Override
	public double getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BufferedImage getTile() {
		// TODO Auto-generated method stub
		return world.res.exit;
	}

	@Override
	public void alert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markVisibility() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void emp() {
		// TODO Auto-generated method stub
		
	}
}
