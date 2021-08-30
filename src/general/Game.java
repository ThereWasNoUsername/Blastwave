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
		
		String path = "./src/general/Level1.txt";
		
		File f = new File("./Blastwave Levels/Level1.txt");
		if(!f.exists()) {
			f.getParentFile().mkdir();
			f.createNewFile();
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			String level = new String(encoded, StandardCharsets.UTF_8);
			BufferedWriter w = new BufferedWriter(new FileWriter(f));
			w.write(level);
			w.close();
		}
		
		World world = new World(path);
		
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
