package Game1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//Holds a level Object and passes timePassed and graphics object to level
public class GameControl{

	private Level l;
	private DummyMaze titleBg;
	private Clip bgMusic;
	private Clip winMusic;
	private BufferedImage controls;
	private BufferedImage story;
	private int state;
	private Menu pause;
	private Menu main;
	private Menu win;
	
	private int frameRate;
	private int frameCount;
	private int frameTime;
	
	private boolean devMode;
	
	
	public GameControl(){
		//Initialize music
		try{
			bgMusic = AudioSystem.getClip();
			bgMusic.open(AudioSystem.getAudioInputStream(new File("res/sounds/bgMusic.wav")));
			winMusic = AudioSystem.getClip();
			winMusic.open(AudioSystem.getAudioInputStream(new File("res/sounds/winMusic.wav")));
			controls = ImageIO.read(new File("res/misc/controls.png"));
			story = ImageIO.read(new File("res/misc/story.png"));
		}catch(Exception e){e.printStackTrace();}
		
		//Start background music loop
		bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
		
		devMode = false;
		state = 0;
		frameCount = 0;
		frameTime = 0;
		titleBg = new DummyMaze();
	}

	//Update objects based on current state
	public void update(long timePassed) {
		
		//Mute background music
		if (Controls.isPressed('M')){
			Controls.removeKey('M');
			if (bgMusic.isRunning()) bgMusic.stop();
			else bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
		}
		
		//Main menu state
		if (state == 0){
			if (main == null){
				main = new Menu("The Monster's Maze");
				main.addOption("START", Menu.RESUME);
				main.addOption("CONTROLS", Menu.CONTROLS);
				main.addOption("STORY", Menu.STORY);
				main.addOption("QUIT", Menu.EXIT);
			}
			main.update(timePassed);
			if (titleBg.getFadeStatus() < 1){
				titleBg = new DummyMaze();
			}
			titleBg.update(timePassed);
			Menu.Option selected = main.getSelected();
			int result = -1;
			if(selected != null) result = selected.getID();
			switch(result){
			case Menu.EXIT:
				bgMusic.stop();
				Maze.stop();
				break;
			case Menu.RESUME:
				state = 1;
				main = null;
				break;
			case Menu.CONTROLS:
				state = 10;
				main.clearSelected();
				break;
			case Menu.STORY:
				state = 11;
				main.clearSelected();
				break;
			}
		}
		
		//Controls and Story states
		if (state == 10 || state == 11){
			if (Controls.isPressed()){ state = 0;
			 Controls.removeKeys();
			}
		}
		
		//Game state
		if (state == 1){
			
			if (Controls.isPressed(KeyEvent.VK_ESCAPE)){
				Controls.removeKey(KeyEvent.VK_ESCAPE);
				state = 2;
			}
			if (l == null || l.playerIsDead()) l = new Level();
			l.update(timePassed);
			if (l.victory()) state = 3;
			if (Controls.isPressed(KeyEvent.VK_F1)){
				Controls.removeKey(KeyEvent.VK_F1);
				devMode = !devMode;
			}
			if (devMode){
				frameCount++;
				frameTime += timePassed;
				if (frameTime >= 1000){
					frameRate = frameCount/(frameTime/1000);
					frameCount = 0;
					frameTime = 0;
				}
				if (Controls.isPressed(KeyEvent.VK_SPACE)){
					Controls.removeKey(KeyEvent.VK_SPACE);
					l = new Level();
				}
				if (Controls.isPressed('L')){
					Controls.removeKey('L');
					l.toggleLights();
				}
				if (Controls.isPressed('N')){
					Controls.removeKey('N');
					l.toggleMonster();
				}
				
			}
		}
		
		//Pause menu state
		if (state == 2){
			if (pause == null){
				pause = new Menu("PAUSED");
				pause.addOption("RESUME", Menu.RESUME);
				pause.addOption("MAIN MENU", Menu.BACK);
				pause.addOption("QUIT", Menu.EXIT);
			}
			pause.update(timePassed);
			Menu.Option selected = pause.getSelected();
			int result = -1;
			if(selected != null) result = selected.getID();
			if (Controls.isPressed(KeyEvent.VK_ESCAPE)){
				Controls.removeKey(KeyEvent.VK_ESCAPE);
				if (result == -1) result = Menu.RESUME;
			}
			switch(result){
			case Menu.EXIT:
				Maze.stop();
				break;
			case Menu.RESUME:
				state = 1;
				pause = null;
				break;
			case Menu.BACK:
				state = 0;
				pause = null;
				l = null;
				break;
			}
			
			
			
		}
		
		//Win state
		if (state == 3){
			if (win == null){
				win = new Menu("You win!");
				win.addOption("PLAY AGAIN", Menu.RESUME);
				win.addOption("QUIT", Menu.EXIT);
			}
			win.update(timePassed);
			if (bgMusic.isRunning()){
				bgMusic.stop();
				winMusic.start();
			}
			
			Menu.Option selected = win.getSelected();
			int result = -1;
			if(selected != null) result = selected.getID();
			switch(result){
			case Menu.EXIT:
				Maze.stop();
				break;
			case Menu.RESUME:
				l=new Level();
				winMusic.stop();
				winMusic.setFramePosition(0);
				bgMusic.setFramePosition(0);
				bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
				state = 1;
				win = null;
				break;
			}
		}
	}

	//Draw based on current state
	public void draw(Graphics2D g) {
		//Main menu state
		if (state == 0){
			titleBg.draw(g);
			if (main != null) main.draw(g);
		}
		
		//Game state and pause state, in pause state, the level is drawn but not updated to appear paused
		if (state == 1 || state == 2){
			l.draw(g);
			int alpha = 255-(int)(l.getPlayerHealth()/1000*255);
			if (alpha < 0) alpha = 0;
			if (alpha > 255) alpha = 255;
			g.setColor(new Color(0,0,0,alpha));
			g.fillRect(0, 0, Maze.WIND_WIDTH, Maze.WIND_HEIGHT);
			
			g.setColor(Color.WHITE);
			if (devMode){
				g.drawString("FrameRate: "+frameRate, 10, 40);
				g.drawString("Player Health: " + l.getPlayerHealth(), 10, 60);
			}
		}
		//paused state menu drawing
		if (state == 2){
			if (pause != null) pause.draw(g);
		}
		
		//Win state menu drawing
		if (state == 3){
			if (win != null) win.draw(g);
		}
		
		//Controls state
		if (state == 10){
			g.drawImage(controls,0,0,null);
		}
		
		//Controls state
		if (state == 11){
			g.drawImage(story, 0,0,null);
		}
		
		
		
	}
	
	//Used in main class (Maze) to pause the game when the window loses focus
	public void pause(){
		if (state != 0) state = 2;
	}
	
	//Dispose audio resources
	public void dispose(){
		bgMusic.close();
		winMusic.close();
		l.dispose();
	}
}
