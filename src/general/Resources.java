package general;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class Resources {
	public final int size;
	public final Font font;
	public final Font font_small;
	
	public final BufferedImage floor, floor_dark;
	public final BufferedImage wall, wall_dark;
	public final BufferedImage player, player_dark;
	
	public final BufferedImage light, light_emp;
	
	public final BufferedImage camera, camera_dark, camera_emp, camera_dark_emp;
	public final BufferedImage relay, relay_dark, relay_emp, relay_dark_emp;
	public final BufferedImage patrol, patrol_dark, patrol_emp, patrol_dark_emp;
	
	public final BufferedImage elevator, elevator_dark, elevator_emp, elevator_dark_emp;
	
	public final BufferedImage exit;
	
	private final String TILES_DIR = "./Brain Waves Tiles";
	
	private final String SOUNDS_DIR = "./src/Sounds";
	
	public final File sound_spotted = new File(SOUNDS_DIR + File.separator + "Spotted.wav");
	public final File sound_shoot = new File(SOUNDS_DIR + File.separator + "Shoot.wav");
	public final File sound_strike = new File(SOUNDS_DIR + File.separator + "Strike.wav");
	public final File sound_wave_blast = new File(SOUNDS_DIR + File.separator + "Wave Blast.wav");
	/*
	public final AudioInputStream sound_spotted;
	public final AudioInputStream sound_shoot;
	public final AudioInputStream sound_strike;
	public final AudioInputStream sound_wave_blast;
	*/
	public Resources() {
		File dir = new File(TILES_DIR);
		if(!dir.exists()) {
			dir.mkdir();
		}
		
		size = 24;
		InputStream input = getClass().getResourceAsStream("/Inconsolata-Regular.ttf");
		InputStream input2 = getClass().getResourceAsStream("/Inconsolata-Regular.ttf");
		Font f = null;
		Font f2 = null;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(Font.BOLD, size + 6);
			f2 = Font.createFont(Font.TRUETYPE_FONT, input2).deriveFont(Font.BOLD, size);
		} catch (FontFormatException | IOException e) {
			System.out.println("Using default font");
			f = new Font("Consolas", Font.PLAIN, size);
			f2 = new Font("Consolas", Font.PLAIN, size - 6);
			e.printStackTrace();
		}
		font = f;
		font_small = f;
		
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
		
		elevator_emp = loadTile("elevator_emp.png", 'L');
		elevator_dark_emp = loadDarkTile("elevator_dark_emp.png", 'L');
		
		exit = loadTile("exit.png", 'X');
		
		/*
		AudioInputStream sound_spotted = null, sound_shoot = null, sound_strike = null, sound_wave_blast = null;
		try {
			sound_spotted = AudioSystem.getAudioInputStream(new File(SOUNDS_DIR + File.separator + "Spotted.wav"));
			sound_shoot = AudioSystem.getAudioInputStream(new File(SOUNDS_DIR + File.separator + "Shoot.wav"));
			sound_strike = AudioSystem.getAudioInputStream(new File(SOUNDS_DIR + File.separator + "Strike.wav"));
			sound_wave_blast = AudioSystem.getAudioInputStream(new File(SOUNDS_DIR + File.separator + "Wave Blast.mp3"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.sound_spotted = sound_spotted;
		this.sound_shoot = sound_shoot;
		this.sound_strike = sound_strike;
		this.sound_wave_blast = sound_wave_blast;
		*/
	}
	//Loads a tile from the specified image filename (from src/tile) or defaults to the specified char
	public BufferedImage loadTile(String filename, char defaultChar) {
		try {
			return ImageIO.read(new File(TILES_DIR + File.separator + filename));
		} catch(Exception e) {
			System.out.println("Using " + defaultChar + " in place of " + filename);
			BufferedImage result = generateTile(defaultChar);
			File f = new File(TILES_DIR + File.separator + filename);
			if(!f.exists()) {
				try {
					ImageIO.write(result, "png", f);
					System.out.println(f.getAbsolutePath());
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			return result;
		}
	}
	public BufferedImage loadDarkTile(String filename, char defaultChar) {
		try {
			return ImageIO.read(new File(TILES_DIR + File.separator + filename));
		} catch(Exception e) {
			System.out.println("Using " + defaultChar + " in place of " + filename);
			BufferedImage result = generateDarkTile(defaultChar);
			
			File f = new File(TILES_DIR + File.separator + filename);
			if(!f.exists()) {
				try {
					ImageIO.write(result, "png", f);
					System.out.println(f.getAbsolutePath());
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			return result;
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
