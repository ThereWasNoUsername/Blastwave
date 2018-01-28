package general;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Elevator extends Entity {
	
	private final int COOLDOWN = 6;
	private int ticksLeft;
	private int empTicks;
	private int guardsLeft;
	public Elevator(World world) {
		super(world);
		empTicks = 0;
		ticksLeft = 0;
	}

	@Override
	public void update() {
		empTicks--;
		if(empTicks > 0) {
			return;
		}
		ticksLeft--;
		if(ticksLeft < 1) {
			ticksLeft = COOLDOWN;
			if(guardsLeft > 0) {
				for(Point p : new Point[] {
						new Point(pos.x-1, pos.y),
						new Point(pos.x+1, pos.y),
						new Point(pos.x, pos.y-1),
						new Point(pos.x, pos.y+1)
						} ) {
					if(world.isValid(p) && world.isOpen(p)) {
						world.addEntity(new Guard(world), p);
						world.addMessage("You have a bad feeling about this.");
						break;
					}
				}
			}
		}
	}

	@Override
	public double getOpacity() {
		// TODO Auto-generated method stub
		return 255;
	}

	@Override
	public BufferedImage getTile() {
		if(empTicks > 0) {
			return world.getBrightness(pos) > 128 ? world.res.elevator_emp : world.res.elevator_dark_emp;
		}
		return world.getBrightness(pos) > 128 ? world.res.elevator : world.res.elevator_dark;
	}

	@Override
	public void alert() {
		guardsLeft += 1;
	}

	@Override
	public void markVisibility() {
	}
	public int getEMPTicks() {
		return empTicks;
	}
	@Override
	public void emp() {
		world.addMessage("You hear an elevator slowing down to a halt");
		empTicks = 24;
	}
	
}
