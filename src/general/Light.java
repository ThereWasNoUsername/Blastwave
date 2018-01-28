package general;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Light extends Entity {

	int brightness;
	int range;
	int decayScale;
	public Light(World world, int brightness, int decayScale) {
		super(world);
		this.brightness = brightness;
		range = 50;
		this.decayScale = decayScale;
	}

	@Override
	public void update() {
		for(int xOffset = -range; xOffset-1 < range; xOffset++) {
			for(int yOffset = -range; yOffset-1 < range; yOffset++) {
				Point posOffset = new Point(pos.x + xOffset, pos.y + yOffset);
				if(!world.isValid(posOffset)) {
					continue;
				}
				if(Helper.lineOfSight(world, pos, posOffset)) {
					double distance = posOffset.distance(pos);
					world.incBrightness(posOffset, (int) (brightness / (Math.max(1, distance * distance / decayScale))));
				}
			}
		}
	}

	@Override
	public double getOpacity() {
		return 0;
	}

	@Override
	public BufferedImage getTile() {
		return brightness > 0 ? world.tiles.light : world.tiles.light_emp;
	}
	@Override
	public void alert() {}
	@Override
	public void markVisibility() {}

	@Override
	public void emp() {
		world.addMessage("You hear the sound of a lightbulb exploding.");
		brightness = 0;
	}

}
