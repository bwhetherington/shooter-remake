import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;


public class Game {
	public static final int 
		FRAME_SAMPLE_SIZE = 50,
		TARGET_FRAME_RATE = 60,
		TARGET_FRAME_DELAY = 1000 / TARGET_FRAME_RATE,
		UI_PADDING = 10;
	
	private static double gameSpeed = 1;
	
	public static void main(String[] args) throws Exception {
//		BufferedImage image = Util.loadImage("player-ship.png");
//		Animation[] anims = {
//				new Animation(0, 0, true),
//				new Animation(1, 2, true)
//		};
//		int width = 144, height = 144, count = 3;
//		SpriteSheet sheet = new SpriteSheet(image, width, height, count, anims);
//		SpriteIO.write(new File("resources/sprites/player-ship2.sprite"), sheet);
		
//		SpriteSheet loaded = SpriteIO.read(new File("resources/sprites/player-ship.sprite"));
//		System.out.println(loaded.getAnimations()[1]);
		
		init();
	}
	
	private static void init() {
		Util.preloadResources();
		
		JFrame frame = new JFrame("Game");
	
		Level l = new Level();
		PlayerShip p = new PlayerShip();
		p.attachEntity(new GunTurret(), Entity.WEAPON_L);
		p.attachEntity(new GunTurret(), Entity.WEAPON_R);
		
		Camera c = new Camera(p);
		l.addEntity(p);

		for (int i = 0; i < 5; i++) {
			l.addEntity(new Banshee());
		}
		
		l.setCamera(c);

		l.setTexture(Util.loadImage("starry-bg-alt.png"));
		
		Controller co = new TurningController(c);
		frame.addKeyListener(co);

		KeyListener gameController = new KeyListener() {
			public boolean isFullScreen;
			public int state, width, height;
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				int key = arg0.getKeyCode();
				if (key == KeyEvent.VK_F11) {
					if (!isFullScreen) {
						width = frame.getWidth();
						height = frame.getHeight();
						state = frame.getExtendedState();
						frame.dispose();
						frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
						frame.setUndecorated(true);
						isFullScreen = true;
						frame.setVisible(true);
					} else {
						frame.dispose();
						frame.setSize(width, height);
						state = frame.getExtendedState();
						frame.setExtendedState(state);
						frame.setUndecorated(false);
						isFullScreen = false;
						frame.setVisible(true);
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		};
		frame.addKeyListener(gameController);
		
		LevelRenderer r = new LevelRenderer(l);
		
		frame.getContentPane().add(r);
		frame.getContentPane().setPreferredSize(new Dimension(640, 480));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		long[] frameTimes = new long[FRAME_SAMPLE_SIZE], times = new long[2];
		int[] time = new int[2];
		double[] speed = new double[1];
		
		
		GController ur = new GController();
		GFillComponent fill = new GFillComponent(ur);
		ur.addMouseMotionListener(co);
		ur.addMouseListener(co);
		ur.addGComponent(fill);
		
		GLifeBar lifeBarComponent = new GLifeBar(p);
		lifeBarComponent.setShowLife(true);
		GRadar radar = new GRadar(6000, p, l);
		radar.setShowCoordinates(true);
		GDebugger debugger = new GDebugger(l);
		GWeaponTemperatureBar weaponBarComponent = new GWeaponTemperatureBar(p.getTurret().getWeapon());
		
		debugger.setVisible(false);
		
		
		boolean[] isSlow = {false};
		GLabel menuLabel1 = new GLabel("Space"), menuLabel2 = new GLabel("Adventure");
		
		GButton slowButton = new GToggleButton(80, 24, "Slow");
		slowButton.addGButtonListener(new GButtonAdapter() {
			public void buttonPressed(GButtonEvent e) {
				isSlow[0] = !isSlow[0];
				gameSpeed = isSlow[0] ? 0.25 : 1;
			}
		});
		
		GButton exitButton = new GButton(80, 24, "Exit", GButton.CANCEL_COLOR);
		exitButton.addGButtonListener(new GButtonAdapter() {
			public void buttonPressed(GButtonEvent e) {
				System.exit(0);
			}
		});
		
		int padding = 8, innerPadding = 5;
		
		GWindow menu = new GWindow(400, 300, "This is a test of the window label: pjg");
		menu.setVisible(false);
		
		GContainer menuContent = menu.getContentPane();
		
		menuContent.addGComponent(menuLabel1, 0, padding, Anchor.TOP_CENTER);
		menuContent.addGComponent(menuLabel2, 0, menuLabel1.getY() + menuLabel1.getHeight() + innerPadding, Anchor.TOP_CENTER);
		
		menuContent.addGComponent(slowButton, 0, menuLabel2.getY() + menuLabel2.getHeight() + innerPadding, Anchor.TOP_CENTER);
		
		menuContent.addGComponent(exitButton, padding, padding, Anchor.BOTTOM_RIGHT);
		
		GButton menuButton = new GButton(80, 24, "Menu");
		menuButton.addGButtonListener(new GButtonAdapter() {
			public void buttonPressed(GButtonEvent e) {
				menu.setVisible(!menu.isVisible());
			}
		});
		
		GFillComponent menuFill = new GFillComponent(ur);
//		menuFill.setBackgroundColor(Colors.DARK_FILL);
//		menuFill.setBorderColor(Colors.TRANSPARENT);
		menuFill.setBackgroundPainted(true);
		menuFill.setVisible(true);
		
		menu.addGComponentListener(new GComponentAdapter() {
			public void componentOpened(GComponentEvent event) {
				l.setPaused(true);
				menuFill.setVisible(true);
			}
			
			public void componentClosed(GComponentEvent event) {
				l.setPaused(false);
				menuFill.setVisible(false);
			}
		});
		
		GButton shipButton = new GToggleButton(80, 24, "Debug");
		shipButton.addGButtonListener(new GButtonAdapter() {
			public void buttonPressed(GButtonEvent e) {
				debugger.setVisible(!debugger.isVisible());
			}
		});
		
		GTextArea textArea = new GTextArea(300);
		textArea.append(new GLabel("This is a test"));
		textArea.append(new GLabel("This is a test 2"));
		
		GButton spawnButton = new GButton(80, 24, "Spawn", GButton.CANCEL_COLOR);
		spawnButton.addGButtonListener(new GButtonAdapter() {
			public void buttonPressed(GButtonEvent e) {
				l.addEntity(new Mothership());
//				textArea.append(new GLabel("This is a test"));
			}
		}); 
		
		
		GSlidePanel sp = new GSlidePanel(textArea, 300);
//		ur.addGComponent(sp, 15, 15, Anchor.TOP_RIGHT);
		
		Color fc = new Color(0, 0, 0, 64);
		Color fb = new Color(0, 0, 0, 0);
		
		GComponent fill1 = new GComponent(radar.getWidth() + 2 * UI_PADDING, radar.getHeight() + 2 * UI_PADDING);
		fill1.setBackgroundColor(fc);
		fill1.setBorderColor(fb);
		
		GComponent fill2 = new GComponent(lifeBarComponent.getWidth() + 2 * UI_PADDING, UI_PADDING + 64 + weaponBarComponent.getHeight() + UI_PADDING);
		fill2.setBackgroundColor(fc);
		fill2.setBorderColor(fb);
		
		weaponBarComponent.setAutoShown(false);
		weaponBarComponent.setVisible(true);
		
		ur.addGComponent(fill1, 0, 0, Anchor.BOTTOM_RIGHT);
		ur.addGComponent(fill2, 0, 0, Anchor.BOTTOM_LEFT);
		
		ur.addGComponent(radar, UI_PADDING, UI_PADDING, Anchor.BOTTOM_RIGHT);
		ur.addGComponent(shipButton, UI_PADDING + 88, UI_PADDING, Anchor.BOTTOM_LEFT);
		ur.addGComponent(spawnButton, UI_PADDING + 176, UI_PADDING, Anchor.BOTTOM_LEFT);
		ur.addGComponent(lifeBarComponent, UI_PADDING, UI_PADDING + 32, Anchor.BOTTOM_LEFT);
		ur.addGComponent(weaponBarComponent, UI_PADDING, UI_PADDING + 64, Anchor.BOTTOM_LEFT);
		ur.addGComponent(debugger, UI_PADDING, UI_PADDING, Anchor.TOP_LEFT);
		
		ur.addGComponent(menuFill);
		
		ur.addGComponent(menu, 0, -100, Anchor.CENTER);
		ur.addGComponent(menuButton, UI_PADDING, UI_PADDING, Anchor.BOTTOM_LEFT);

		frame.setGlassPane(ur);
		ur.setVisible(true);
		
		boolean[] hasUpdate = {false};
		javax.swing.Timer update = new javax.swing.Timer(15, (e) -> {
			// Framerate determination and speed calculation
			time[0]++;
			if (time[1] < FRAME_SAMPLE_SIZE) {
				time[1]++;
			}
			time[0] %= FRAME_SAMPLE_SIZE;
			times[0] = System.nanoTime() / 1000;
		    frameTimes[time[0]] = times[1] == 0 ? 0 : (times[0] - times[1]);
		    times[1] = System.nanoTime() / 1000;
		    speed[0] = ((double) getAverageFrameTime(frameTimes, time[1])) / (TARGET_FRAME_DELAY * 1000 / gameSpeed);
		    l.setTimeScale(speed[0]);
		    
//		    System.out.println("Framerate: " + TARGET_FRAME_RATE / speed[0]);
		    
		    // Updating
			l.update();
		    co.update();
		    Timer.updateGlobal();
		    Timer.setTimeScaleGlobal(speed[0]);
		    hasUpdate[0] = true;
		});
		
		javax.swing.Timer frameUpdate = new javax.swing.Timer(15, (e) -> {
			if (hasUpdate[0]) {
				r.repaint();
				hasUpdate[0] = false;
			}
		});
		
		update.start();
		frameUpdate.start();
	}
	
	private static long getAverageFrameTime(long[] frameTimes, int size) {
		long sum = 0;
		for (long time : frameTimes) {
			sum += time;
		}
		return sum / size;
	}
}
