package general;

import java.awt.image.BufferedImage;

public class Relay extends Entity {

	private int range;
	boolean ready;
	int empTicks;
	public Relay(World world, int range) {
		super(world);
		this.range = range;
		empTicks = 0;
	}

	@Override
	public void update() {
		empTicks--;
		//We can only activate once per turn
		ready = true;
	}

	@Override
	public double getOpacity() {
		// TODO Auto-generated method stub
		return 102;
	}

	@Override
	public BufferedImage getTile() {
		if(empTicks > 0) {
			return world.getBrightness(pos) > 128 ? world.res.relay_emp : world.res.relay_dark_emp;
		}
		return world.getBrightness(pos) > 128 ? world.res.relay : world.res.relay_dark;
	}

	@Override
	public void alert() {
		if(empTicks > 0)
			return;
		if(ready) {
			ready = false;
			world.alert(pos, range);
		}
	}

	@Override
	public void markVisibility() {
	}
	public int getEMPTicks() {
		return empTicks;
	}
	@Override
	public void emp() {
		world.addMessage("You hear the buzzing sound of a Relay malfunctioning");
		empTicks = 20;
	}

}
