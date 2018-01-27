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
		addKeyListener(this);
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
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
