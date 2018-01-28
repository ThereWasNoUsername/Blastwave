package general;
import javax.swing.JFrame;

public class Game {
	public static void main(String[] args) {
		try {
		System.setProperty("sun.java2d.opengl", "true");
		JFrame frame = new JFrame("Brain Waves by Alex Chen");
		World world;
		
		//User can specify a custom level here
		if(args.length == 1) {
			world = new World(args[0]);
		} else {
			world = new World("./src/general/Level1");
		}
		
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
