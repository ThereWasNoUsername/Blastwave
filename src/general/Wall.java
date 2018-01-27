package general;

import java.awt.image.BufferedImage;

public class Wall extends Entity {
	public Wall(World world) {
		super(world);
	}
	@Override
	public void update() {
	}
	public BufferedImage getTile() {
		return world.tiles.wall;
	}
}