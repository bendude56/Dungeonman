package com.bendude56.dungeonman.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bendude56.dungeonman.GameInstance;
import com.bendude56.dungeonman.entity.AIController;
import com.bendude56.dungeonman.entity.Entity;
import com.bendude56.dungeonman.entity.Entity.ActionType;
import com.bendude56.dungeonman.entity.EntityPlayer;
import com.bendude56.dungeonman.gfx.GraphicsPanel;
import com.bendude56.dungeonman.world.World;
import com.bendude56.dungeonman.world.WorldLocation;
import com.bendude56.dungeonman.world.tile.Tile;
import com.bendude56.dungeonman.world.tile.TileState;

public class GameFrame extends JFrame {
	private static final long	serialVersionUID	= 1L;
	
	public static GameFrame activeFrame;
	
	public JMenuBar mainMenu;
	
	public JMenu fileMenu;
	public JMenuItem newMenuButton;
	public JMenuItem loadMenuButton;
	public JMenuItem saveMenuButton;
	public JMenuItem saveAsMenuButton;
	public JMenuItem exitMenuButton;
	
	public JMenu windowMenu;
	public JMenuItem windowInventoryButton;
	public JMenuItem windowStatsButton;
	public JMenuItem windowTextLogButton;
	
	public GraphicsPanel gamePanel;
	
	public int lastKeyCode;
	public char lastKeyChar;
	public CountDownLatch keyLatch;
	
	public TextLogFrame activeLog;
	public String loggedMessages = "";
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeAndWait(new Runnable() {
			public void run() {
				try {
					activeFrame = new GameFrame();
					activeFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		activeFrame.mainLoop();
	}
	
	public GameFrame() {
		initComponents();
	}
	
	private void initComponents() {
		this.setTitle("Dungeonman v0.1.0");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 600, 500);
		
		initMenus();
		
		gamePanel = new GraphicsPanel();
		this.add(gamePanel);
	}
	
	private void initMenus() {
		mainMenu = new JMenuBar();
		this.setJMenuBar(mainMenu);
		
		initFileMenu();
		initWindowMenu();
	}
	
	private void initFileMenu() {
		fileMenu = new JMenu("File");
		mainMenu.add(fileMenu);
		
		newMenuButton = new JMenuItem("New Game");
		fileMenu.add(newMenuButton);
		
		loadMenuButton = new JMenuItem("Load...");
		fileMenu.add(loadMenuButton);
		
		fileMenu.addSeparator();
		
		saveMenuButton = new JMenuItem("Save");
		saveMenuButton.setEnabled(false);
		fileMenu.add(saveMenuButton);
		
		saveAsMenuButton = new JMenuItem("Save As...");
		saveAsMenuButton.setEnabled(false);
		fileMenu.add(saveAsMenuButton);
		
		fileMenu.addSeparator();
		
		exitMenuButton = new JMenuItem("Exit");
		fileMenu.add(exitMenuButton);
		
		newMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							NewGameFrame f = new NewGameFrame(GameFrame.this);
							f.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		
		exitMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameFrame.this.dispatchEvent(new WindowEvent(GameFrame.this, WindowEvent.WINDOW_CLOSING));
			}
		});
	}
	
	private void initWindowMenu() {
		windowMenu = new JMenu("Window");
		windowMenu.setEnabled(false);
		mainMenu.add(windowMenu);
		
		windowInventoryButton = new JMenuItem("Inventory");
		windowMenu.add(windowInventoryButton);
		
		windowStatsButton = new JMenuItem("Stats");
		windowMenu.add(windowStatsButton);
		
		windowTextLogButton = new JMenuItem("Text Log");
		windowMenu.add(windowTextLogButton);
		
		windowTextLogButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							if (activeLog == null) {
								activeLog = new TextLogFrame();
								activeLog.logTextBox.setText(loggedMessages);
								activeLog.setVisible(true);
							} else {
								activeLog.requestFocus();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
	
	public void logMessage(String message) {
		loggedMessages += message + "\n";
		if (activeLog != null) {
			activeLog.logTextBox.setText(activeLog.logTextBox.getText() + message + "\n");
			activeLog.logTextBox.select(activeLog.logTextBox.getText().length(), 0);
		}
	}
	
	private void mainLoop() throws InterruptedException {
		KeyListener keyListen = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				lastKeyCode = e.getKeyCode();
				lastKeyChar = e.getKeyChar();
				keyLatch.countDown();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
		};
		
		while (true) {
			if (GameInstance.getActiveInstance() == null || GameInstance.getActiveInstance().getPlayerEntity() == null) {
				Thread.sleep(500);
				continue;
			}
			
			World w = GameInstance.getActiveWorld();
			EntityPlayer p =  GameInstance.getActiveInstance().getPlayerEntity();
			
			gamePanel.centerX = p.getLocation().x;
			gamePanel.centerY = p.getLocation().y;
			gamePanel.drawGameWorld();
			gamePanel.repaint();
			
			keyLatch = new CountDownLatch(1);
			this.addKeyListener(keyListen);
			try {
				keyLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.removeKeyListener(keyListen);
			
			WorldLocation newLocation = p.getLocation();
			
			if (lastKeyCode == KeyEvent.VK_UP) {
				newLocation = newLocation.adjustLocation(0, 1);
			} else if (lastKeyCode == KeyEvent.VK_RIGHT) {
				newLocation = newLocation.adjustLocation(1, 0);
			} else if (lastKeyCode == KeyEvent.VK_DOWN) {
				newLocation = newLocation.adjustLocation(0, -1);
			} else if (lastKeyCode == KeyEvent.VK_LEFT) {
				newLocation = newLocation.adjustLocation(-1, 0);
			} else if (lastKeyCode == KeyEvent.VK_P) {
				List<Entity> entities = newLocation.world.getEntities(newLocation);
				boolean done = false;
				
				for (Entity e : entities) {
					if (e.doAction(ActionType.PICKUP, p)) {
						done = true;
						break;
					}
				}
				
				if (!done) {
					p.logMessage("There is nothing to pick up!");
				}
			} else if (lastKeyCode == KeyEvent.VK_V) {
				for (int y = p.getLocation().y + p.getViewDistance(); y >= p.getLocation().y - p.getViewDistance(); y--) {
					int deltaX = p.getViewDistance() - Math.abs(y - p.getLocation().y);
					for (int x = p.getLocation().x + deltaX; x >= p.getLocation().x - deltaX; x--) {
						if (AIController.checkVisibility(p.getLocation(), new WorldLocation(null, x, y))) {
							p.getLocation().world.setTile(x, y, Tile.stairs);
						}
					}
				}
			}
			
			TileState state = w.getTileState(newLocation);
			if (!newLocation.equals(p.getLocation()) && state.getTileType().onPlayerMove(state, p)) {
				List<Entity> entities = w.getEntities(newLocation);
				boolean cancelled = false;
				
				for (Entity e : entities) {
					if (!e.doAction(ActionType.MOVE, p)) {
						cancelled = true;
					}
				}
				
				if (!cancelled)
					p.setLocation(newLocation);
			}
			state.update();
			
			w.doTurn();
		}
	}
}
