package general;
import javax.swing.JFrame;

public class Game {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		World world = new World();
		frame.add(new Panel(world));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("Running");
	}
}
