package general;

import java.awt.image.BufferedImage;

public class Wall extends Entity {
	public Wall(World world) {
		super(world);
	}
	public double getOpacity() {
		return 255;
	}
	@Override
	public void update() {
	}
	public BufferedImage getTile() {
		return world.getBrightness(pos) > 128 ? world.tiles.wall : world.tiles.wall_dark;
	}
	@Override
	public void alert() { }
	@Override
	public void markVisibility() { }
	@Override
	public void emp() { }
}