package general;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class World {
	private final	int width, height;
	private final	Entity[][] map;
	public final	Player player;
	private final	int[][] lighting;		//Intensity of light on each tile, 0-255
	public final	Tiles tiles;			//Contains images for the tileset
	private final	boolean[][] visible;
	private final List<String> messages;
	public World() {
		width = 128;
		height = 128;
		tiles = new Tiles();
		map = new Entity[width][height];
		lighting = new int[width][height];
		visible = new boolean[width][height];
		player = new Player(this);
		messages = new ArrayList<String>();
		addEntity(player, new Point(12, 12));
		addEntity(new Wall(this), new Point(10, 10));
	}
	public void update() {
		messages.clear();
		List<Entity> entities = new ArrayList<>();
		for(Entity[] row : map) {
			for(Entity e : row) {
				if(e != null) {
					entities.add(e);
				}
			}
		}
		for(Entity e : entities) {
			e.update();
		}
	}
	private void addMessage(String message) {
		messages.add(message);
	}
	public void addEntity(Entity e, Point pos) {
		map[pos.x][pos.y] = e;
	}
	public boolean isOpen(Point pos) {
		return map[pos.x][pos.y] == null;
	}
	public void move(Entity e, Point pos) {
		Point pos_previous = e.getPos();
		map[pos_previous.x][pos_previous.y] = null;
		e.setPos(pos);
		map[pos.x][pos.y] = e;
	}
	public void paint(JPanel panel, Graphics g) {
		int tileSize = tiles.size;
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
		int halfDrawWidthTiles = halfDrawWidth/tileSize;
		int halfDrawHeightTiles = halfDrawHeight/tileSize;
		
		//The center of our drawing area
		int drawCenterX = panel.getWidth()/2;
		int drawCenterY = panel.getHeight()/2 - panel.getHeight()/6;
		
		int drawDistance = 30;
		
		for(int xOffset = -halfDrawWidthTiles; xOffset < halfDrawWidthTiles+1; xOffset--) {
			for(int yOffset = -halfDrawHeightTiles; yOffset < halfDrawHeightTiles; yOffset++) {
				//Skip tiles that are beyond draw distance
				if((xOffset*xOffset) + (yOffset*yOffset) > (drawDistance*drawDistance)) {
					continue;
				}
				Point pos = new Point(playerPos.getX() + xOffset, playerPos.getY() + yOffset);
				
				//Draw floor if empty
				BufferedImage tile = null;
				if(map[pos.x][pos.y] == null) {
					tile = tiles.floor;
				} else {
					tile = map[pos.x][pos.y].getTile();
				}
				g.drawImage(tile, drawCenterX + (xOffset * tileSize), drawCenterY - (yOffset * tiles.size), null);
			}
		}
		
		int textDrawX = 0;
		int textDrawY = drawCenterY + halfDrawHeight;
		for(String message : messages) {
			g.setFont(tiles.font_small);
			g.setColor(Color.BLACK);
			g.drawString(message, textDrawX, textDrawY);
			textDrawY += tiles.font_small.getSize();
		}
	}
}
