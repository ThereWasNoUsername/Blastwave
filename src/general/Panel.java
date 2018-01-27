package general;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class Panel extends JPanel implements KeyListener {
	private World world;
	int intro;
	
	public Panel(World world) {
		this.world = world;
		intro = 4;
		intro();
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
		case KeyEvent.VK_ENTER:
			intro();
			break;
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
	
	void intro() {
		switch(intro) {
		case 4:
			world.addMessage("You were living just another day of your normal life...");
			world.addMessage("Until a bunch of government agents came and took you away.");
			world.addMessage("You had no idea why such a thing would happen to you...");
			world.addMessage("Though you did overhear them talking about how you possessed...");
			world.addMessage("An uncanny ability to control radio transmissions and whatnot.");
			world.addMessage("Well, now you find yourself in a what is most likely...");
			world.addMessage("A very huge facility dedicated to housing someone just like you.");
			world.addMessage("[Press Enter]");
			intro--;
			repaint();
			return;
		case 3:
			world.clearMessages();
			world.addMessage("And this facility looks like one massive Faraday cage...");
			world.addMessage("Specially designed to inhibit your special ability.");
			world.addMessage("There are cameras to spot you if you dare to run...");
			world.addMessage("There are radio beacons to relay alerts from cameras...");
			world.addMessage("And there are scary guards to receive those alerts.");
			world.addMessage("Should you have the bravery (or idiocy) to run...");
			world.addMessage("Head for that exit right there.");
			//TO DO: Implement Exit marker
			world.addMessage("[Press Enter]");
			intro--;
			repaint();
			return;
		case 2:
			world.clearMessages();
			world.addMessage("And while you're at it, why don't you demonstrate proof...");
			world.addMessage("Of your supernatural control over the airwaves?");
			world.addMessage("Use the arrow keys to move.");
			world.addMessage("Press F to unleash an EMP blast that disables nearby lights...");
			//TO DO: Implement EMP blast
			world.addMessage("Along with nearby wireless communications.");
			world.addMessage("Remember: Stay in the dark and stay out of sight.");
			world.addMessage("[Press Enter]");
			intro--;
			repaint();
			return;
		case 1:
			world.clearMessages();
			world.addMessage("Your time is now.");
			world.addMessage("[Move]");
			intro--;
			repaint();
			return;
		case 0:
			repaint();
			break;
		}
	}
}
