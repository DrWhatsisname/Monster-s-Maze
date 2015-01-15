package Game1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//Represents the player
public class Player implements GameObject, LightSource{

	private CharacterMotor motor;
	private SpriteSheet spriteSheet;
	private SpriteSheet withSword;
	private SpriteSheet noSword;
	private Light light;
	private static final int WIDTH = 16;
	private static final int HEIGHT = 32;
	
	private int currentMap;
	
	private static Clip swordGet;
	
	private double health;
	private boolean hit;
	
	private boolean isDead;
	private boolean hasSword;
	
	//Constructor, initializes CharacterMotor, SpriteSheet, and sets the active animation to the first index
	public Player(double sx, double sy){
		motor = new CharacterMotor(sx,sy);
		
		//SpriteSheet constructor
		noSword = new SpriteSheet("char", WIDTH, HEIGHT);
		withSword = new SpriteSheet("charSword", WIDTH,HEIGHT);
		spriteSheet = noSword;
		spriteSheet.setAnimation(4);
		light = new Light(80, Color.ORANGE);
		
		health = 1;
		if (swordGet == null){
			
			try {
				swordGet = AudioSystem.getClip();
				swordGet.open(AudioSystem.getAudioInputStream(new File("res/sounds/swordGet.wav")));
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int getCurrentMap(){
		return currentMap;
	}
	public void changeCurrentMap(int m){
		currentMap = m;
	}
	
	
	//Checks for movement controls, sends them to the motor, updates the active animation
	@Override
	public void update(long timePassed) {
		boolean[] moveStatus = motor.getMoveStatus();
		if (moveStatus[0] && !Controls.isPressed('W') && !Controls.isPressed(KeyEvent.VK_UP)){
			spriteSheet.setAnimation(7);
		}
		if (moveStatus[1] && !Controls.isPressed('S') && !Controls.isPressed(KeyEvent.VK_DOWN)){
			spriteSheet.setAnimation(4);
		}
		if (moveStatus[2] && !Controls.isPressed('A') && !Controls.isPressed(KeyEvent.VK_LEFT)){
			spriteSheet.setAnimation(6);
		}
		if (moveStatus[3] && !Controls.isPressed('D') && !Controls.isPressed(KeyEvent.VK_RIGHT)){
			spriteSheet.setAnimation(5);
		}
		motor.resetDirections();
		if (Controls.isPressed('W') || Controls.isPressed(KeyEvent.VK_UP)) {
			motor.goUp();		
		}
		if (Controls.isPressed('S') || Controls.isPressed(KeyEvent.VK_DOWN)){
			motor.goDown();
		}
		if (Controls.isPressed('A') || Controls.isPressed(KeyEvent.VK_LEFT)){
			motor.goLeft();
		}
		if (Controls.isPressed('D') || Controls.isPressed(KeyEvent.VK_RIGHT)){
			motor.goRight();
		}
		motor.update(timePassed);
		moveStatus = motor.getMoveStatus();
		
		if (moveStatus[2]){
			spriteSheet.setAnimation(2);
		}
		if (moveStatus[3]){
			spriteSheet.setAnimation(1);
		}
		if (moveStatus[0] && !(moveStatus[2] || moveStatus[3])){
			spriteSheet.setAnimation(3);
		}
		if (moveStatus[1] && !(moveStatus[3] || moveStatus[2])){
			spriteSheet.setAnimation(0);
		}
		spriteSheet.update(timePassed);
		
		if (health <= 0) isDead = true;
		if (!hit && health < 1000){
			health += timePassed/3.0;
			if (health > 1000) health = 1000;
		}
		hit = false;
		
	}
	
	//Lose health if the monster is over top of the player
	public void hit(double amount){
		health -= amount;
		hit = true;
	}
	
	public boolean hasSword(){
		return hasSword;
	}
	
	
	public boolean isDead(){
		return isDead;
	}
	
	public double getHealth(){
		return health;
	}
	
	//draws the active animation
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(spriteSheet.getImage(), (int)motor.getX(), (int)motor.getY(), null);
	}
	
	//translates the character 
	@Override
	public void translate(double dx, double dy) {
		motor.translate(dx, dy);
		
	}

	//Returns x coordinate
	@Override
	public double getX() {
		return motor.getX();
	}

	//Returns y coordinate
	@Override
	public double getY() {
		return motor.getY();
	}

	//Returns width
	@Override
	public double getWidth() {
		return WIDTH;
	}

	//Returns height
	@Override
	public double getHeight() {
		return HEIGHT;
	}

	//LightSource methods
	@Override
	public double getSourceX() {
		return motor.getX() + 8;
	}


	@Override
	public double getSourceY() {
		return motor.getY();
	}


	@Override
	public Light getSource() {
		return light;
	}

	
	public void getSword() {
		hasSword = true;
		spriteSheet = withSword;
		swordGet.stop();
		swordGet.setFramePosition(0);
		swordGet.start();
		
	}

	public void dispose() {
		swordGet.close();
		
	}



	

}
