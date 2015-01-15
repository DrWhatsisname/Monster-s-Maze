package Game1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

//Main class, handles display and the loop
public class Maze extends JFrame implements KeyListener{

	private static final long serialVersionUID = -5538662245630400175L;
	private static final int FRAME_LIMIT = 1000;
	
	public static final int WIND_HEIGHT = 600;
	public static final int WIND_WIDTH  = 800;
	
	private static boolean running = true;
	
	private static GameControl game;
	
	//Main function, gives control to the Maze class
	public static void main(String[] args){
		new Maze();
	}
	
	//Main loop, runs until the game is stopped
	private void loop() {
		long timePassed = 0;
		long lastTime = System.currentTimeMillis();
		while(running){
			//Computes the amount of time that has passed since the last frame
			timePassed = System.currentTimeMillis() - lastTime;
			
			//Regulates the frame rate
			while(timePassed < 1000/FRAME_LIMIT){timePassed = System.currentTimeMillis() - lastTime;}
			lastTime = System.currentTimeMillis();
			
			//Sends time passed to all the objects, which update their status
			update(timePassed);
			
			//Creates a graphics object for the window
			Graphics2D g = (Graphics2D)this.getBufferStrategy().getDrawGraphics();
			
			
			//Sends graphics object to all the objects
			draw(g);
			//Switches the buffer
			this.getBufferStrategy().show();
			//Dispose graphics object
			g.dispose();
			
			if(!this.isFocused()) game.pause();
			
		}
		//Disposes the window once running stops
		this.dispose();
		
	}
	
	//Constructor, sets title of window, calls init, passes control to the loop
	public Maze(){
		super("Maze");
		init();
		loop();
	}
	
	//Initializes window and GameControl object
	private void init(){
		super.setSize(WIND_WIDTH,WIND_HEIGHT);
		super.setFocusTraversalKeysEnabled(false);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setResizable(false);
		super.addKeyListener(this);
		
		super.setVisible(true);
		super.createBufferStrategy(2);
		
		Graphics2D g = (Graphics2D) this.getBufferStrategy().getDrawGraphics();
		g.fillRect(0, 0, WIND_WIDTH, WIND_HEIGHT);
		g.setColor(Color.WHITE);
		g.drawString("Loading...", WIND_WIDTH/2, WIND_HEIGHT/2);
		g.dispose();
		this.getBufferStrategy().show();
		
		game = new GameControl();
		

	}
	
	//Passes timePassed to game objects
	private static void update(long timePassed){
		game.update(timePassed);
	}
	
	//Draws a background color to cover up previous frames, then passes Graphics obect to the rest of the objects
	private void draw(Graphics2D g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		game.draw(g);
		
	}
	
	//Ends game
	public static void stop(){
		game.dispose();
		running = false;
	}
	
	//Passes keyPressed events to Control class
	@Override
	public void keyPressed(KeyEvent e) {
		Controls.keyPressed(e);
		e.consume();
	}
	
	//Passes keyReleased events to Control class
	@Override
	public void keyReleased(KeyEvent e) {
		Controls.keyReleased(e);
		e.consume();
	}

	//Ignores keyTyped events
	@Override
	public void keyTyped(KeyEvent e) {
		e.consume();
		
	}
	
}
