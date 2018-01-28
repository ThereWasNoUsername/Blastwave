package general;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import general.Player.PlayerState;

public class World {
	public final	int width, height;
	private final	Entity[][] map;
	public final	Player player;
	private			int[][] brightness;		//Intensity of light on each tile, 0-255
	public final	Resources res;			//Contains images for the tileset
	boolean[][] enemyVisible;				//If true, the player knows that the enemy sees this tile
	private	boolean[][] playerVisible;	//If true, the player can see this tile
	private final List<String> messages;
	private final List<Integer> messageTimes;
	private String deathMessage;
	
	public final String filename;
	/*
	public World() {
		width = 128;
		height = 128;
		tiles = new Tiles();
		map = new Entity[width][height];
		brightness = new int[width][height];
		playerVisible = new boolean[width][height];
		enemyVisible = new boolean[width][height];
		player = new Player(this);
		messages = new ArrayList<String>();
		messageTimes = new ArrayList<Integer>();
		addEntity(player, new Point(3, 3));
		
		addEntity(new Camera(this, 10), new Point(5, 15));
		addEntity(new Light(this, 204, 6), new Point(5, 5));
		addEntity(new Guard(this), new Point(12, 30));
		new Box(new Point(0, 0), new Point(127, 127)).create(this);
		
		new Box(new Point(0, 0), new Point(10, 10)).create(this);
		new Box(new Point(0, 10), new Point(10, 20)).create(this);
		new Box(new Point(0, 20), new Point(10, 30)).create(this);
		
		//Hallway
		new Box(new Point(10, 0), new Point(15, 40)).create(this);
		addEntity(new Light(this, 153, 4), new Point(11, 8));
		addEntity(new Light(this, 153, 4), new Point(11, 16));
		addEntity(new Light(this, 153, 4), new Point(11, 24));
		addEntity(new Light(this, 153, 4), new Point(11, 32));
		
		
		removeEntity(new Point(10, 5));
		removeEntity(new Point(10, 15));
	}
	*/
	public World(String filename) throws IOException {
		this.filename = filename;
		byte[] encoded = Files.readAllBytes(Paths.get(filename));
		String level = new String(encoded, StandardCharsets.UTF_8);
		String[] parts = level.split("\n");
		width = parts[0].length();
		height = parts.length;
		res = new Resources();
		map = new Entity[width][height];
		brightness = new int[width][height];
		playerVisible = new boolean[width][height];
		enemyVisible = new boolean[width][height];
		player = new Player(this);
		messages = new ArrayList<String>();
		messageTimes = new ArrayList<Integer>();
		
		for(int y = 0; y < parts.length; y++) {
			for(int x = 0; x < parts[y].length(); x++) {
				Point p = new Point(x, y);
				switch(parts[y].charAt(x)) {
				case '!':
					addEntity(new Camera(this, 10), p);
					break;
				case '#':
					addEntity(new Wall(this), p);
					break;
				case '*':
					addEntity(new Light(this, 153, 4), p);
					break;
				case '%':
					addEntity(new Relay(this, 40), p);
					break;
				case 'A':
					addEntity(new Guard(this), p);
					break;
				case '@':
					player.setPos(p);
					break;
				case 'E':
					addEntity(new Elevator(this), p);
					break;
				case 'X':
					addEntity(new Exit(this), p);
					break;
				}
			}
		}
		addEntity(player, player.getPos());
	}
	public void update() {
		
		System.out.println("Paint");
		clearMessages();
		
		List<Entity> entities = new ArrayList<>();
		for(Entity[] row : map) {
			for(Entity e : row) {
				if(e != null) {
					entities.add(e);
				}
			}
		}
		brightness = new int[width][height];
		
		playerVisible = new boolean[width][height];
		player.update();
		
		//If we escape this turn, then end
		if(player.getState() == PlayerState.ESCAPED) {
			return;
		}
		
		enemyVisible = new boolean[width][height];
		entities.remove(player);
		for(Entity e : entities) {
			//If the entity is no longer on the map at its position, then it's been killed so we don't update
			Point p = e.getPos();
			if(map[p.x][p.y] == e) {
				e.update();
			}
		}
		player.markVisibility();
		for(Entity e : entities) {
			if(playerVisible[e.getPos().x][e.getPos().y]) {
				e.markVisibility();
				if(e instanceof Camera) {
					addMessage("You see a Camera. Stay out of the red area or you will be seen.");
				} else if(e instanceof Light) {
					addMessage("You see a Light. Stay in the dark or you will be seen.");
				} else if(e instanceof Relay) {
					addMessage("You see a Relay. Don't be seen by a Camera or everyone will know.");
				} else if(e instanceof Guard) {
					addMessage("You see a Guard. Get away before they kill you.");
				}
			}
		}
		
		
		if(brightness[player.getPos().x][player.getPos().y] < 255 * 1/8) {
			addMessage("You are now a shadow in the darkness.");
		}
		
		if(player.getState() == PlayerState.DEAD) {
			clearMessages();
			addMessage(deathMessage);
			addMessage("You're dead now! Game over!");
			addMessage("Press Backspace to restart the level or press F to load a custom level.");
			return;
		}
	}
	public void setPlayerVisibility(Point pos) {
		playerVisible[pos.x][pos.y] = true;
	}
	public void setEnemyVisibility(Point pos) {
		enemyVisible[pos.x][pos.y] = true;
	}
	public void incBrightness(Point pos, int brightness) {
		this.brightness[pos.x][pos.y] += brightness;
	}
	public int getBrightness(Point pos) {
		return brightness[pos.x][pos.y];
	}
	public void alert(Point cameraPos, int range) {
		//Alert all entities in range
		for(int xOffset = -range; xOffset-1 < range; xOffset++) {
			for(int yOffset = -range; yOffset-1 < range; yOffset++) {
				Point posOffset = new Point(cameraPos.x + xOffset, cameraPos.y + yOffset);
				if(!isValid(posOffset)) {
					continue;
				}
				if(!isOpen(posOffset)) {
					getAt(posOffset).alert();
				}
			}
		}
	}
	public void addMessage(String message) {
		int index = messages.indexOf(message);
		if(index != -1) {
			messageTimes.set(index, messageTimes.get(index)+1);
			return;
		}
		messages.add(message);
		messageTimes.add(1);
	}
	public void clearMessages() {
		messages.clear();
		messageTimes.clear();
	}
	public void addEntity(Entity e, Point pos) {
		map[pos.x][pos.y] = e;
		e.setPos(pos);
	}
	public void removeEntity(Point pos) {
		map[pos.x][pos.y] = null;
	}
	public boolean isOpen(Point pos) {
		return map[pos.x][pos.y] == null;
	}
	public Entity getAt(Point pos) {
		return map[pos.x][pos.y];
	}
	public void move(Entity e, Point pos) {
		Point pos_previous = e.getPos();
		map[pos_previous.x][pos_previous.y] = null;
		e.setPos(pos);
		map[pos.x][pos.y] = e;
	}
	public void paint(JPanel panel, Graphics g) {
		int tileSize = res.size;
		g.clearRect(0, 0, panel.getWidth(), panel.getHeight());
		/*
		for(int x = 0; x < width; x++) {
			for(int y = height-1; y > -1; y--) {
				//Draw floor if empty
				BufferedImage tile = null;
				if(map[x][y] == null) {
					tile = tiles.floor;
				} else {
					tile = map[x][y].getTile();
				}
				g.drawImage(tile, x * tiles.size, y * tiles.size, null);
			}
		}
		*/
		Point playerPos = player.getPos();
		
		//The size of the area on which we can draw tiles
		int halfDrawWidth = panel.getWidth()/2;
		int halfDrawHeight = panel.getHeight()/3;
		int halfDrawWidthTiles = halfDrawWidth/tileSize - 1;
		int halfDrawHeightTiles = halfDrawHeight/tileSize;
		
		//The center of our drawing area
		int drawCenterX = panel.getWidth()/2;
		int drawCenterY = panel.getHeight()/2 - panel.getHeight()/6;
		
		int drawDistance = 50;
		
		//First pass draws objects
		for(int xOffset = -halfDrawWidthTiles; xOffset-1 < halfDrawWidthTiles+1; xOffset++) {
			for(int yOffset = -halfDrawHeightTiles; yOffset-1 < halfDrawHeightTiles; yOffset++) {
				Point pos = new Point(playerPos.x + xOffset, playerPos.y + yOffset);
				int drawX = drawCenterX + (xOffset * tileSize), drawY = drawCenterY - (yOffset * res.size);
				
				//Check out of bounds
				if(!isValid(pos)) {
					g.setColor(Color.BLACK);
					g.fillRect(drawX, drawY, tileSize, tileSize);
					continue;
				}
				
				
				int brightness = Math.min(this.brightness[pos.x][pos.y], 255);
				Color background = new Color(brightness, brightness, brightness, 255);
				if(!playerVisible[pos.x][pos.y]) {
					g.setColor(background);
					g.fillRect(drawX, drawY, tileSize, tileSize);
					continue;
				}
				
				//Draw floor if empty
				BufferedImage tile = null;
				if(map[pos.x][pos.y] == null) {
					tile = getBrightness(pos) > 128 ? res.floor : res.floor_dark;
				} else {
					tile = map[pos.x][pos.y].getTile();
				}
				
				g.setColor(background);
				g.fillRect(drawX, drawY, tileSize, tileSize);
				g.drawImage(tile, drawX, drawY, null);
				if(enemyVisible[pos.x][pos.y]) {
					g.setColor(new Color(255, 0, 0, brightness/2));
					g.fillRect(drawX, drawY, tileSize, tileSize);
				}
			}
		}
		
		//Second pass draws the EMP radius colors
		int empRadius = player.getEMPRadius();
		for(int xOffset = -empRadius; xOffset-1 < empRadius+1; xOffset++) {
			for(int yOffset = -empRadius; yOffset-1 < empRadius; yOffset++) {
				Point pos = new Point(playerPos.x + xOffset, playerPos.y + yOffset);
				int drawX = drawCenterX + (xOffset * tileSize), drawY = drawCenterY - (yOffset * res.size);
				Point displacement = new Point(Math.abs(pos.x - playerPos.x), Math.abs(pos.y - playerPos.y));
				
				if(displacement.x + displacement.y < player.getEMPRadius()) {
					Random x = new Random(pos.x + playerPos.y);
					Random y = new Random(pos.x + playerPos.x);
					g.setColor(new Color(0, x.nextInt(256), y.nextInt(256), 102));
					g.fillRect(drawX, drawY, tileSize, tileSize);
				}
			}
		}
		
		int textDrawX = tileSize;
		int textDrawY = drawCenterY + halfDrawHeight + tileSize + tileSize;
		for(int i = 0; i < messages.size(); i++) {
			g.setFont(res.font);
			g.setColor(Color.BLACK);
			String message = messages.get(i);
			int times = messageTimes.get(i);
			if(times > 1) {
				message = String.format("%-4s%s", "x" + times, message);
			} else {
				message = "    " + message;
			}
			
			g.drawString(message, textDrawX, textDrawY);
			textDrawY += res.font.getSize();
		}
	}
	public boolean isValid(Point p) {
		return -1 < p.x && p.x < width && -1 < p.y && p.y < height;
	}
	public void killPlayer(String deathMessage) {
		this.deathMessage = deathMessage;
		player.kill();
		removeEntity(player.getPos());
	}
}
