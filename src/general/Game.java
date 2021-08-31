package general;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFrame;

public class Game {
	public static void main(String[] args) {
		try {
		System.setProperty("sun.java2d.opengl", "true");
		JFrame frame = new JFrame("Blastwave");
		
		World world = new World("./Blastwave Levels/Level1.txt");
		
		Panel panel = new Panel(world);
		frame.add(panel);
		frame.addKeyListener(panel);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("Running");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
