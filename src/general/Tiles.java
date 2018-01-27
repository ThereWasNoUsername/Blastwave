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
	public final Font font;
	
	public final BufferedImage floor;
	public final BufferedImage wall;
	public final BufferedImage player;
	
	public final BufferedImage camera;
	public final BufferedImage relay;
	public final BufferedImage patrol;
	
	private final String TILES_DIR = "./src/tile/";
	public Tiles() {
		size = 24;
		
		InputStream input = getClass().getResourceAsStream("/Inconsolata-Regular.ttf");
		Font f = null;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(Font.BOLD, size + 6);
		} catch (FontFormatException | IOException e) {
			System.out.println("Using default font");
			f = new Font("Consolas", Font.PLAIN, size);
			e.printStackTrace();
		}
		font = f;
		
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
			return ImageIO.read(new File(TILES_DIR + filename));
		} catch(Exception e) {
			System.out.println("Using " + defaultChar + " in place of " + filename);
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
