package general;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import general.Player.PlayerState;

public class Panel extends JPanel implements KeyListener {
	private World world;
	int intro;
	boolean active;
	
	public Panel(World world) {
		initialize(world);
	}
	public void initialize(World world) {
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
		if(intro > 0 && arg0.getKeyCode() != KeyEvent.VK_L) {
			if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				intro();
			}
			return;
		}
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_BACK_SPACE:
			try {
				initialize(new World(world.filename));
			} catch (IOException e1) {
				world.addMessage("Failed to restart level.");
			}
			break;
		case KeyEvent.VK_L:
			System.out.println("Load");
			
			world.clearMessages();
			world.addMessage("Loading custom level");
			
			JFileChooser j = new JFileChooser();
			j.setFileFilter(new FileNameExtensionFilter("Level files", "txt"));
			j.setPreferredSize(getSize());
			
			j.setFileSelectionMode(JFileChooser.FILES_ONLY);
			setComponentsFont(j.getComponents(), world.res.font_small);
			j.setCurrentDirectory(new File(System.getProperty("user.dir")));
			if(j.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					world.addMessage("Custom level loaded");
					initialize(new World(j.getSelectedFile().getAbsolutePath()));
				} catch (IOException e) {
					world.addMessage("Failed to load custom level");
				}
			} else {
				world.addMessage("Load custom level cancelled");
			}
			repaint();
			
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
		case KeyEvent.VK_F:
			if(world.player.getState() == PlayerState.ACTIVE && world.player.getEMPCooldown() < 1) {
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
	//https://coderanch.com/t/342116/java/set-font-JFileChooser
	public static void setComponentsFont(Component[] comp, Font font) {
		for(int x = 0; x < comp.length; x++) {
			if(comp[x] instanceof Container) setComponentsFont(((Container)comp[x]).getComponents(), font);
			try{comp[x].setFont(font);}
			catch(Exception e){}//do nothing
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
			world.addMessage("\"Blastwave\"");
			world.addMessage("by Alex Chen");
			world.addMessage("A strategic stealth roguelike");
			world.addMessage("Made for Global Game Jam 2018.");
			world.addMessage("Theme: \"Transmission\"");
			world.addMessage("");
			world.addMessage("\"Service unavailable\" - Your Phone");
			world.addMessage("[Press Enter to start or press L to load a custom level]");
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
			world.addMessage("Press F to unleash a blastwave that disables nearby lights...");
			world.addMessage("Along with nearby wireless communications.");
			world.addMessage("It also creates a huge distraction and dazes guards.");
			world.addMessage("Remember: Stay dark, stay quiet, and stay out of red.");
			world.addMessage("Don't forget to use your blastwave to save yourself!");
			world.addMessage("[Press Enter]");
			intro--;
			repaint();
			return;
		case 1:
			world.clearMessages();
			world.addMessage("[Char]  [Object]");
			world.addMessage("@       Player");
			world.addMessage("* -     Light     Deactivated light");
			world.addMessage("! |     Camera    Deactivated Camera");
			world.addMessage("% /     Relay     Deactivated Relay");
			world.addMessage("A a     Guard     Stunned guard");
			world.addMessage("X       Exit");
			world.addMessage(".       Floor");
			world.addMessage("#       Wall");
			world.addMessage("");
			world.addMessage("Begin. Move with arrow keys and use a Blastwave with F");
			intro--;
			repaint();
			return;
		case 0:
			repaint();
			break;
		}
	}
}
