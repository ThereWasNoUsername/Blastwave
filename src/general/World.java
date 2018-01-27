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
	public World() {
		width = 128;
		height = 128;
		tiles = new Tiles();
		map = new Entity[width][height];
		lighting = new int[width][height];
		visible = new boolean[width][height];
		player = new Player(this);
		addEntity(player, new Point(12, 12));
		addEntity(new Wall(this), new Point(10, 10));
	}
	public void update() {
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
	public void addEntity(Entity e, Point pos) {
		map[pos.x][pos.y] = e;
	}
	public void move(Entity e, Point pos) {
		Point pos_previous = e.getPos();
		map[pos_previous.x][pos_previous.y] = null;
		e.setPos(pos);
		map[pos.x][pos.y] = e;
	}
	public void paint(JPanel panel, Graphics g) {
		g.clearRect(0, 0, panel.getWidth(), panel.getHeight());
		for(int x = 0; x < width; x++) {
			for(int y = height-1; y > -1; y--) {
				//Draw floor if empty
				int tileSize = tiles.size;
				BufferedImage tile = null;
				if(map[x][y] == null) {
					tile = tiles.floor;
				} else {
					tile = map[x][y].getTile();
				}
				g.drawImage(tile, x * tiles.size, y * tiles.size, null);
				
			}
		}
	}
	
}
