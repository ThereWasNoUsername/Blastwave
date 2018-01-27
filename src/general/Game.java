package general;
import javax.swing.JFrame;

public class Game {
	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		JFrame frame = new JFrame();
		World world = new World();
		Panel panel = new Panel(world);
		frame.add(panel);
		frame.addKeyListener(panel);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("Running");
	}
}
