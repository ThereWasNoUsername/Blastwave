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
	
	public final BufferedImage floor;
	public final BufferedImage wall;
	
	private final String TILES_DIR = "./src/tile/";
	public Tiles() {
		size = 24;
		
		InputStream input = getClass().getResourceAsStream("/font/Inconsolata-Regular.TTF");
		Font f = null;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(Font.PLAIN, size);
		} catch (FontFormatException | IOException e) {
			f = new Font("Consolas", Font.PLAIN, size);
			e.printStackTrace();
		}
		font = f;
		
		BufferedImage floor = generateTile('.');
		try {
			floor = ImageIO.read(new File(TILES_DIR + "floor.png"));
		} catch(Exception e) {
			System.out.println("Using default floor tile");
		}
		this.floor = floor;
		
		BufferedImage wall = generateTile('#');
		try {
			wall = ImageIO.read(new File(TILES_DIR + "wall.png"));
		} catch(Exception e) {
			System.out.println("Using default wall tile");
		}
		this.wall = wall;
	}
	public BufferedImage generateTile(char c) {
		BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = result.getGraphics();
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString("" + c, size/4, size * 4/5);
		
		return result;
	}
}
