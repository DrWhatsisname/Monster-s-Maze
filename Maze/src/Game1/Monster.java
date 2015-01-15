package Game1;

import java.awt.Graphics2D;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//Represents the monster that chases the player
public class Monster implements GameObject{
	private static double DETECTION_RADIUS = 256;
	
	private static Clip[] sounds;
	
	private double x;
	private double y;
	private double velocity;
	private int currentMap;
	
	private Player target;
	private SpriteSheet spriteSheet;
	
	private boolean digging;
	private int digTime;
	
	private boolean fleeing;
	
	private int waitTime;
	private boolean waitInit;
	
	private boolean biasY;
	private boolean biasX;
	
	private boolean isDead;
	
	
	public Monster(double x, double y, Player target){
		this.x = x;
		this.y = y;
		this.target = target;
		currentMap = 1;
		this.velocity = 120;
		spriteSheet = new SpriteSheet("monster", 32, 48);
		digging = false;
		waitInit = false;
		biasY = false;
		biasX = false;
		isDead = false;
		fleeing = false;
		if (sounds == null){
			sounds = new Clip[6];
			try{
				sounds[0] = AudioSystem.getClip();
				sounds[0].open(AudioSystem.getAudioInputStream(new File("res/sounds/dig1.wav")));
				sounds[1] = AudioSystem.getClip();
				sounds[1].open(AudioSystem.getAudioInputStream(new File("res/sounds/dig2.wav")));
				sounds[2] = AudioSystem.getClip();
				sounds[2].open(AudioSystem.getAudioInputStream(new File("res/sounds/dig3.wav")));
				sounds[3] = AudioSystem.getClip();
				sounds[3].open(AudioSystem.getAudioInputStream(new File("res/sounds/monsterYell.wav")));
				sounds[4] = AudioSystem.getClip();
				sounds[4].open(AudioSystem.getAudioInputStream(new File("res/sounds/step1.wav")));
				sounds[5] = AudioSystem.getClip();
				sounds[5].open(AudioSystem.getAudioInputStream(new File("res/sounds/step2.wav")));
			}catch (Exception e){e.printStackTrace();}
		}
		
	}
	
	//Updates the monster's logic
	public void update(long timePassed){
		double distanceToTarget = Math.sqrt(Math.pow(target.getX() - this.getX(), 2) + Math.pow(target.getY() - this.getY(),2));

		//Handles sound effects logic
		if (distanceToTarget < DETECTION_RADIUS){
			if(currentMap == target.getCurrentMap()){
				if (spriteSheet.getCurrentFrame() == 0) playSoundEffect(sounds[4]);
				else if (spriteSheet.getCurrentFrame() == 4) playSoundEffect(sounds[5]);
			}
			if (digging){
				playSoundEffect(sounds[(int)(Math.random() * 3)]);
			}
		}
		//Changes the monster's logic if the player has the sword
		if (distanceToTarget <= DETECTION_RADIUS && target.hasSword() && currentMap == target.getCurrentMap() && distanceToTarget <= DETECTION_RADIUS){
				
				if (!fleeing) playSoundEffect(sounds[3]);
				fleeing = true;
		}
		else{
			fleeing = false;
		}
		
		//If the monster is in the fleeing state, the monster runs away from the player
		if (fleeing && !digging){
			moveTowards(2*this.x - target.getX(), 2*this.y - target.getY(), timePassed);
			if (currentMap != target.getCurrentMap()) fleeing = false;
		}
		//Otherwise it uses its normal logic
		else{
			/*
			 * Monster will move towards the player
			 * if the player is overground and outside a certain radius
			 * the monster will wait for three seconds before going underground to
			 * try and sneak up behind the player
			 * if the player is underground, the monster will continouously chase the player
			 * 
			 * if the monster is underground, it moves quickly and essentially moves through the walls of the maze
			 * once the monster is in line with tiles that are definately not walls and is within a certain distance of the player
			 * the monster will move overground
			 * 
			 * if the monster hits a wall while overground, it will move only in the x or y direction depending on the side of the wall it hits
			 */
			if (currentMap == 0){
				if (distanceToTarget <= DETECTION_RADIUS && target.getCurrentMap() == this.getCurrentMap() && (waitTime >= 0 || !waitInit)){
					digging = false;
					
					velocity = 120;
					moveTowards((target.getX() + target.getWidth()/2) - this.getWidth()/2, (target.getY() + target.getHeight()/2) - this.getHeight()/2, timePassed);
					if (waitInit && distanceToTarget > 86) waitTime -= timePassed;
					else{
						waitInit = true;
						waitTime = 3000;
					}
				}
				else if (digging){
					digTime-=timePassed;
					if (digTime <= 0){ 
						currentMap = 1;
						digging = false;
					}
				}
				else{
					digging = true;
					digTime = 500;
				}
			}
			else if (currentMap == 1){
				waitInit = false;
				if (target.getCurrentMap() == this.getCurrentMap()){
					digging = false;
					velocity = 120;
					moveTowards((target.getX() + target.getWidth()/2) - this.getWidth()/2, (target.getY() + target.getHeight()/2) - this.getHeight()/2, timePassed);
				}
				else if (digging){
					digTime -= timePassed;
					if (digTime <= 0) {
						currentMap = 0;
						digging = false;
					}
				}
				else if (distanceToTarget < DETECTION_RADIUS/4 &&
					((int)this.x / 32) % 4 > 0 && ((int)this.x / 32) % 4  < 3 && ((int)(this.x + this.getWidth()) / 32) % 4 < 3 && ((int)(this.x + this.getWidth()) / 32) % 4 > 0 &&
						((int)this.y / 32) % 4 > 0 && ((int)this.y / 32) % 4 < 3 && ((int)(this.y + this.getHeight()) / 32) % 4 < 3 && ((int)(this.y + this.getHeight()) / 32) % 4 > 0){
							digging = true;
							digTime = 500;
				}
				else{ 
					velocity = 160;
					moveTowards((target.getX() + target.getWidth()/2) - this.getWidth()/2, (target.getY() + target.getHeight()/2) - this.getHeight()/2, timePassed);
					digging = false;
				}
			}
		}
		if (distanceToTarget < 64 && this.getCurrentMap() == target.getCurrentMap()){
			if (!(getX() > target.getX() + target.getWidth() || getX() + getWidth() < target.getX() || getY() > target.getY() + target.getHeight() || getY() + getHeight() < target.getY())){ 
				if (target.hasSword()) this.die();
				else target.hit(timePassed);
			}
		}
		
		spriteSheet.update(timePassed);
	}
	
	private void playSoundEffect(Clip clip) {
		if (!clip.isActive()){
			clip.stop();
			clip.setFramePosition(0);
			clip.start();
		}
		
	}

	public void moveTowards(double tx, double ty, long timePassed){
		double d = velocity * (timePassed/1000.0);
		double theta = 0;
		if (this.x - tx  == 0){
			if (this.y > ty){
				theta = -Math.PI/2;
			}
			else if (this.y < ty){
				theta = Math.PI/2;
			}
		}
		else{
			theta = Math.atan((ty - this.y)/(tx - this.x));
			if (this.x > tx){
				if (theta >= 0) theta += Math.PI;
				else theta -= Math.PI;
			}
		}
		if(biasY){
			if((theta > 0 && theta < Math.PI) || (theta < -Math.PI && theta > -Math.PI * 2)) y += d;
			else y -= d;
			biasY = false;
			return;
		}
		else if (biasX){
			if(theta < Math.PI/2 && theta > -Math.PI/2) x+=d;
			else x-=d;
			biasX = false;
			return;
		}
		this.y += d * Math.sin(theta);
		this.x += d * Math.cos(theta);
		manageAnimation(theta);
		
	}
	
	//Sets the monster's current animation based on its current direction
	private void manageAnimation(double direction){
		if (direction <= Math.PI / 4 && direction > -Math.PI/4) spriteSheet.setAnimation(3); //Replace 3 with index of RIGHT animation
		else if ((direction <= Math.PI / 4 * 3 && direction > Math.PI/4) || (direction <= -Math.PI * 5 / 4 && direction > -Math.PI * 7 / 4)) spriteSheet.setAnimation(0); //Replace 0 with index of DOWN animation
		else if ((direction <= -Math.PI / 4 * 3 && direction > -Math.PI * 5 / 4) || (direction > Math.PI / 4 * 3 && direction <= Math.PI * 5 / 4)) spriteSheet.setAnimation(2); //Replace with index of LEFT animation
		else if ((direction <= -Math.PI / 4 && direction > -Math.PI * 3 / 4) || (direction <= Math.PI * 7 / 4 && direction > Math.PI * 5/4)) spriteSheet.setAnimation(1); //Replace 1 with index of UP animation
	}
	
	
	public void translate(double dx, double dy){
		this.x += dx;
		this.y += dy;
		if (Math.abs(dx) > 0 && dy == 0) biasY = true;
		if (Math.abs(dy) > 0 && dx == 0) biasX = true;
	}
	
	public int getCurrentMap(){
		return currentMap;
	}
	
	public boolean isDigging(){
		return digging;
	}
	
	public void draw(Graphics2D g){
		g.drawImage(spriteSheet.getImage(), (int)this.getX(), (int)this.getY(), null);
	}
	
	public double getX(){
		return this.x;
	}
	public double getY(){
		return this.y;
	}
	
	public double getWidth(){
		return 32;
	}
	public double getHeight(){
		return 48;
	}

	
	public void hit(double amount){
		
	}
	private void die(){
		isDead = true;
	}
	public boolean isDead(){
		return isDead;
	}
	public void setCurrentMap(int i){
		currentMap = i;
	}

	public void dispose() {
		for (int i = 0; i < sounds.length; i++){
			sounds[i].close();
		}
		
	}
}
