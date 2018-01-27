package general;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Tiles {
	public final int size;
	private final Font font;
	private final Font font_small;
	
	public final BufferedImage floor;
	public final BufferedImage wall;
	public final BufferedImage player;
	
	public final BufferedImage camera;
	public final BufferedImage relay;
	public final BufferedImage patrol;
	
	private final String TILES_DIR = "./src/tile/";
	public Tiles() {
		size = 24;
		
		InputStream input = getClass().getResourceAsStream("/font/Inconsolata-Regular.TTF");
		Font f = null;
		Font f_small = null;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(Font.PLAIN, size);
			f_small = Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(Font.PLAIN, size);//Draw floor if empty
				BufferedImage tile = null;
				if(map[x][y] == null) {
					tile = tiles.floor;
				} else {
					tile = map[x][y].getTile();
				}
				g.drawImage(tile, x * tiles.size, y * tiles.size, null);
		} catch (FontFormatException | IOException e) {
			f = new Font("Consolas", Font.PLAIN, size);
			f_small = new Font("Consolas", FONT.PLAIN, size/2);
			e.printStackTrace();
		}
		font = f;
		font_small = f_small;
		
		floor = loadTile("floor.png", '.');
		wall = loadTile("wall.png", '#');
		player = loadTile("player.png", '@');
		
		camera = loadTile("camera.png", '*');
		relay = loadTile("relay.png", '%');
		patrol = loadTile("patrol.png", 'a');
	}
	//Loads a tile from the specified image filename (from src/tile) or defaults to the specified char
	public BufferedImage loadTile(String filename, char defaultChar) {
		try {
			return ImageIO.read(new File(TILE_DIR + filename));
		} catch(Exception e) {
			System.out.println("Using " defaultChar + " in place of " + filename);
			return generateTile(defaultChar);
		}
	}
	//Generates a 24x24 tile from the specified char
	public BufferedImage generateTile(char c) {
		BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = result.getGraphics();
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString("" + c, size/4, size * 4/5);
		
		return result;
	}
}
