package general;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Guard extends Entity {
	private Point targetPos;
	private boolean[][] visibility;
	private int missesLeft;
	
	private int empTicks;
	public Guard(World world) {
		super(world);
		//path = null;
		visibility = new boolean[world.width][world.height];
		empTicks = 0;
		missesLeft = 3;
	}

	@Override
	public void update() {
		//EMP slows down guards
		empTicks--;
		if((empTicks%4) > 0)
			return;
		int range = 30;
		visibility = new boolean[world.width][world.height];
		for(int xOffset = -range; xOffset-1 < range; xOffset++) {
			for(int yOffset = -range; yOffset-1 < range; yOffset++) {
				Point posOffset = new Point(pos.x + xOffset, pos.y + yOffset);
				if(!world.isValid(posOffset)) {
					continue;
				}
				//Set this point as visible if we can see it enough
				if(Helper.lineOfSight(world, pos, posOffset)) {
					int brightness = world.getBrightness(posOffset);
					
					if(brightness > 255 * 1/4 || posOffset.distance(pos) < 3) {
						visibility[posOffset.x][posOffset.y] = true;
					}
				}
			}
		}
		//If we can see the player, alert!
		Point playerPos = world.player.getPos();
		if(visibility[playerPos.x][playerPos.y]) {
			try {
				//throw new Exception("Implement");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Helper.playSound(world.res.sound_spotted, -5);
			world.addMessage("You've been spotted!");
			
			world.alert(pos, range);
			targetPos = playerPos;
			
			//Attempt to shoot the player
			if(playerPos.distance(pos) > 3) {
				Helper.playSound(world.res.sound_shoot, 1);
				
				//The first shots are guaranteed misses
				boolean miss = false;
				if(empTicks > 0 && empTicks%2 == 0) {
					miss = true;
				} else if(missesLeft > 0) {
					missesLeft--;
					miss = true;
				} else if(Math.random() < 0.5) {
					//We miss by random chance
					miss = true;
				}
				
				if(miss) {
					Point hit = new Point(playerPos.x + (int) (Math.random()*3) - 1, playerPos.y + (int) (Math.random()*3) - 1);
					if(hit.equals(playerPos) || world.isOpen(pos)) {
						world.addMessage("The guard attempts to shoot you, but the shot misses.");
					} else {
						Entity e = world.getAt(hit);
						if(e instanceof Wall)
							world.addMessage("The guard attempts to shoot you, but the shot lands into a wall");
						else if(e instanceof Guard) {
							world.addMessage("The guard attempts to shoot you, but the shot hits another guard instead.");
							world.removeEntity(hit);
						} else if(e instanceof Camera) {
							world.addMessage("The guard attempts to shoot you, but they accidentally shoot out a Camera instead.");
							world.removeEntity(hit);
						} else if(e instanceof Relay) {
							world.addMessage("The guard attempts to shoot you, but they accidentally shoot out a Relay instead.");
							world.removeEntity(hit);
						} else if(e instanceof Light) {
							world.addMessage("The guard attempts to shoot you, but they accidentally shoot out a Light instead.");
							world.removeEntity(hit);
						}
					}
				} else {
					Helper.playSound(world.res.sound_strike, 3);
					
					world.killPlayer("The guard shoots you down!");
				}
			}
			
		} else {
			//Choose a random adjacent point to go to
			if(targetPos == null) {
				Point[] next = { new Point(pos.x - 1, pos.y), new Point(pos.x + 1, pos.y), new Point(pos.x, pos.y-1), new Point(pos.x, pos.y+1) };
				for(Point p : next) {
					if(world.isOpen(p)) {
						targetPos = p;
						break;
					}
				}
			} else if(pos.distance(targetPos) < 4) {
				//Extend our current path randomly
				
				Point displacement = new Point(targetPos.x - pos.x, targetPos.y - pos.y);
				double angle = Math.atan2(displacement.y, displacement.x);
				
				ArrayList<Point> next = new ArrayList<>(Arrays.asList(
						new Point((int) (targetPos.x + 1.5 * Math.cos(angle)), (int) (targetPos.y + 1.5 * Math.sin(angle))),
						new Point((int) (targetPos.x + 1.5 * Math.cos(angle)), (int) (targetPos.y + 1.5 * Math.sin(angle))),
						new Point((int) (targetPos.x + 1.5 * Math.cos(angle)), (int) (targetPos.y + 1.5 * Math.sin(angle))),
						
						new Point((int) (targetPos.x + 1.5 * Math.cos(angle+90)), (int) (targetPos.y + 1.5 * Math.sin(angle+90))),
						new Point((int) (targetPos.x + 1.5 * Math.cos(angle+90)), (int) (targetPos.y + 1.5 * Math.sin(angle+90))),
						
						new Point((int) (targetPos.x + 1.5 * Math.cos(angle-90)), (int) (targetPos.y + 1.5 * Math.sin(angle-90))),
						new Point((int) (targetPos.x + 1.5 * Math.cos(angle-90)), (int) (targetPos.y + 1.5 * Math.sin(angle-90))),
						
						new Point((int) (targetPos.x + 1.5 * Math.cos(angle+180)), (int) (targetPos.y + 1.5 * Math.sin(angle+180)))
						)); 
				Collections.shuffle(next);
				
				for(Point p : next) {
					if(world.isValid(p) && world.isOpen(p)) {
						targetPos = p;
						break;
					}
				}
			}
		}
		
		//Seek the targetPos
		//path = calcPath(playerPos);
		if(targetPos != null) {
			List<Point> next = new ArrayList<>();
			next.addAll(Arrays.asList(new Point[] { new Point(pos.x - 1, pos.y), new Point(pos.x + 1, pos.y), new Point(pos.x, pos.y + 1), new Point(pos.x, pos.y - 1) }));
			
			next.sort(new Comparator<Point>() {
				@Override
				public int compare(Point p1, Point p2) {
					double distance1 = p1.distance(targetPos);
					double distance2 = p2.distance(targetPos);
					if(distance1 < distance2) {
						return -1;
					} else if(distance1 > distance2) {
						return 1;
					}
					return 0;
				}
			});
			
			for(Point p : next) {
				if(world.isOpen(p)) {
					//Guard is slightly slower than the player
					if(Math.random() > 0.15) {
						world.move(this, p);
					}
					
					break;
				} else if(world.getAt(p) == world.player) {
					if(empTicks > 0) {
						Helper.playSound(world.res.sound_strike, 2);
						world.addMessage("A dazed guard attempts to strike you down, but misses.");
					} else {
						Helper.playSound(world.res.sound_strike, 2);
						world.killPlayer("The guard strikes you down with a metal baton.");
					}
					
				}
			}
			
		}
		
		
		/*
		if(path == null || path.size() == 0) {
			Point dest = null;
			while(dest == null) {
				dest = new Point(pos.x + (int) (Math.random() * 10), pos.y + (int) (Math.random() * 10));
				if(!world.isValid(pos)) {
					dest = null;
					continue;
				}
				if(!world.isOpen(dest)) {
					dest = null;
					continue;
				}
				path = calcPath(dest);
				if(path == null || path.size() == 0) {
					dest = null;
					continue;
				}
			}
		}
		Point next = path.get(path.size()-1);
		if(!world.isOpen(next)) {
			path = calcPath(path.get(0));
		} else {
			world.move(this, next);
		}
		*/
	}

	@Override
	public double getOpacity() {
		return 51;
	}

	@Override
	public BufferedImage getTile() {
		if(empTicks > 0) {
			return world.getBrightness(pos) > 128 ? world.res.patrol_emp : world.res.patrol_dark_emp; 
		}
		return world.getBrightness(pos) > 128 ? world.res.patrol : world.res.patrol_dark;
	}

	@Override
	public void alert() {
		if(empTicks > 0)
			return;
		//path = calcPath(world.player.getPos());
		targetPos = world.player.getPos();
	}
	public List<Point> calcPath(Point dest) {
		List<Point> path = new ArrayList<>();
		path.add(pos);
		try {
			if(calcPath(dest, path, 50)) {
				return path;
			}
		} catch(Exception e) {
			return null;
		}
		return null;
	}
	public boolean calcPath(Point dest, List<Point> path, int triesLeft) throws Exception {
		if(triesLeft == 0) {
			throw new Exception();
		}
		Point previous = path.get(path.size()-1);
		if(previous.equals(dest)) {
			return true;
		}
		List<Point> next = new ArrayList<>();
		next.addAll(Arrays.asList(new Point[] { new Point(previous.x - 1, previous.y), new Point(previous.x + 1, previous.y), new Point(previous.x, previous.y + 1), new Point(previous.x, previous.y - 1) }));
		next.sort(new Comparator<Point>() {
			@Override
			public int compare(Point p1, Point p2) {
				double distance1 = p1.distance(dest);
				double distance2 = p2.distance(dest);
				if(distance1 < distance2) {
					return -1;
				} else if(distance1 > distance2) {
					return 1;
				}
				return 0;
			}
		});
		for(Point p : next) {
			if(path.contains(p)) {
				continue;
			}
			if(!world.isOpen(p)) {
				continue;
			}
			path.add(p);
			if(calcPath(dest, path, triesLeft-1)) {
				return true;
			}
			path.remove(path.size()-1);
			triesLeft--;
		}
		return false;
	}

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
		world.addMessage("You hear a guard fall over.");
		//empTicks = 16;
		empTicks += 7;
	}

}
