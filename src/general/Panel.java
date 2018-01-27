package general;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class Panel extends JPanel implements KeyListener {
	private World world;
	public Panel(World world) {
		this.world = world;
	}
	public void paint(Graphics g) {
		//Clear the screen
		g.clearRect(0, 0, getWidth(), getHeight());
		
		if(world.player.isReady()) {
			world.update();
		}
		world.paint(this, g);
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		System.out.println("Key pressed");
		switch(arg0.getKeyCode()) {
			case KeyEvent.VK_UP:
				System.out.println("Up");
				world.player.setDirection(Direction.UP);
				repaint();
				break;
			case KeyEvent.VK_RIGHT:
				System.out.println("Right");
				world.player.setDirection(Direction.RIGHT);
				repaint();
				break;
			case KeyEvent.VK_DOWN:
				System.out.println("Down");
				world.player.setDirection(Direction.DOWN);
				repaint();
				break;
			case KeyEvent.VK_LEFT:
				System.out.println("Left");
				world.player.setDirection(Direction.LEFT);
				repaint();
				break;
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
