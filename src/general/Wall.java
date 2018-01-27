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
		return world.tiles.wall;
	}
	@Override
	public void alertCamera() { System.out.println("Wall: alertCamera() not supported");}
	@Override
	public void markVisibility() { }
}