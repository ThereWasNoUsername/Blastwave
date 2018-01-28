package general;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class Panel extends JPanel implements KeyListener {
	private World world;
	int intro;
	boolean active;
	
	public Panel(World world) {
		this.world = world;
		world.player.setDirection(Direction.NONE);
		world.update();
		
		intro = 5;
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
		if(intro > 0) {
			if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				intro();
			}
			return;
		}
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
		case KeyEvent.VK_F:
			if(world.player.getEMPCooldown() < 1) {
				world.clearMessages();
				world.addMessage("The air begins to rumble as a minor headache descends upon you.");
				world.player.activateEMP();
				
			} else {
				world.addMessage("You should wait a while before doing that again...");
				world.addMessage("Given that you don't want your head to explode.");
				world.addMessage("Actually, you just need to wait for exactly " + world.player.getEMPCooldown() + " steps.");
			}
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
		case 5:
			world.clearMessages();
			world.addMessage("\"Mind Waves\"");
			world.addMessage("By Alex Chen");
			world.addMessage("A strategic stealth roguelike");
			world.addMessage("Made for Global Game Jam 2018.");
			world.addMessage("Theme: \"Transmission\"");
			world.addMessage("");
			world.addMessage("\"Service unavailable\" - Your Phone");
			world.addMessage("[Press Enter]");
			intro--;
			repaint();
			return;
		case 4:
			world.clearMessages();
			world.addMessage("You were living just another day of your normal life...");
			world.addMessage("Until a bunch of government agents came and took you away.");
			world.addMessage("You had no idea why such a thing would happen to you...");
			world.addMessage("Though you did overhear them talking about how you possessed...");
			world.addMessage("An uncanny ability to control light, radio transmissions, etc.");
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
			world.addMessage("There are scary guards to receive those alerts...");
			world.addMessage("And there are elevators to bring in yet more guards...");
			world.addMessage("Should you have the bravery (or idiocy) to run...");
			world.addMessage("You'll need to find an exit.");
			world.addMessage("[Press Enter]");
			intro--;
			repaint();
			return;
		case 2:
			world.clearMessages();
			world.addMessage("And while you're at it, why don't you demonstrate proof...");
			world.addMessage("Of your supernatural control over the airwaves?");
			world.addMessage("Use the arrow keys to move. If you find yourself trapped...");
			world.addMessage("Press F to unleash a wave blast that disables nearby lights...");
			world.addMessage("Along with nearby wireless communications.");
			world.addMessage("It also creates a huge distraction and dazes guards.");
			world.addMessage("Remember: Stay dark, stay quiet, and stay out of sight.");
			world.addMessage("Don't forget to use your wave blast to save yourself!");
			world.addMessage("[Press Enter]");
			intro--;
			repaint();
			return;
		case 1:
			world.clearMessages();
			world.addMessage("[Char]  [Object]");
			world.addMessage("@       Player");
			world.addMessage("*       Light");
			world.addMessage("!       Camera");
			world.addMessage("%       Relay");
			world.addMessage("A       Guard");
			world.addMessage(".       Floor");
			world.addMessage("#       Wall");
			world.addMessage("");
			world.addMessage("Begin. Move with arrow keys and use a wave blast with F");
			intro--;
			repaint();
			return;
		case 0:
			repaint();
			break;
		}
	}
}
