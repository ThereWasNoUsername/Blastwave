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
	
	public final BufferedImage floor, floor_dark;
	public final BufferedImage wall, wall_dark;
	public final BufferedImage player, player_dark;
	
	public final BufferedImage light, light_emp;
	
	public final BufferedImage camera, camera_dark, camera_emp, camera_dark_emp;
	public final BufferedImage relay, relay_dark, relay_emp, relay_dark_emp;
	public final BufferedImage patrol, patrol_dark, patrol_emp, patrol_dark_emp;
	
	public final BufferedImage elevator, elevator_dark, elevator_emp, elevator_dark_emp;
	
	public final BufferedImage exit;
	
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
		floor_dark = loadDarkTile("floor_dark.png", '.');
		wall = loadTile("wall.png", '#');
		wall_dark = loadDarkTile("wall_dark.png", '#');
		player = loadTile("player.png", '@');
		player_dark = loadDarkTile("player_dark.png", '@');
		
		light = loadTile("light.png", '*');
		light_emp = loadDarkTile("light_emp.png", '-');
		
		camera = loadTile("camera.png", '!');
		camera_dark = loadDarkTile("camera_dark.png", '!');
		camera_emp = loadTile("camera_emp.png", '|');
		camera_dark_emp = loadDarkTile("camera_dark_emp.png", '|');
		
		relay = loadTile("relay.png", '%');
		relay_dark = loadDarkTile("relay_dark.png", '%');
		relay_emp = loadTile("relay_emp.png", '/');
		relay_dark_emp = loadDarkTile("relay_dark_emp.png", '/');
		
		patrol = loadTile("patrol.png", 'A');
		patrol_dark = loadDarkTile("patrol_dark.png", 'A');
		patrol_emp = loadTile("patrol_emp.png", 'a');
		patrol_dark_emp = loadDarkTile("patrol_dark_emp.png", 'a');
		
		elevator = loadTile("elevator.png", 'E');
		elevator_dark = loadDarkTile("elevator.png", 'E');
		
		elevator_emp = loadTile("elevator_emp", 'L');
		elevator_dark_emp = loadDarkTile("elevator_emp", 'L');
		
		exit = loadTile("exit.png", 'X');
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
	public BufferedImage loadDarkTile(String filename, char defaultChar) {
		try {
			return ImageIO.read(new File(TILES_DIR + filename));
		} catch(Exception e) {
			System.out.println("Using " + defaultChar + " in place of " + filename);
			return generateDarkTile(defaultChar);
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
	public BufferedImage generateDarkTile(char c) {
		BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = result.getGraphics();
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("" + c, size/4, size * 4/5);
		
		return result;
	}
}
