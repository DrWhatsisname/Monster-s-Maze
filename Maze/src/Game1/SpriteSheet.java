package Game1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//Automatically loads indicated sprite sheet
public class SpriteSheet {

	private BufferedImage sheet;
	private Animation[] animations;
	private int active;
	private int spriteWidth;
	private int spriteHeight;
	
	//Constructor, opens spriteSheet image, loads animations
	public SpriteSheet(String name, int width, int height){
		try {
			sheet = ImageIO.read(new File("res/spriteSheets/" + name + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		spriteWidth = width;
		spriteHeight = height;
		
		loadSheet();
		active = 0;
	}
	
	//Sets loads each row as a separate animation, saves to an index in animations[], each animation must have equal number of frames
	private void loadSheet(){
		animations = new Animation[sheet.getHeight()/spriteHeight];
		for (int y = 0; y < animations.length; y++){
			animations[y] = new Animation();
			for (int x = 0; x < sheet.getWidth() / spriteWidth; x++){
				animations[y].addFrame(100, sheet.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight));
			}
		}
	}
	
	//Switches active animation
	public void setAnimation(int index){
		if (active != index){
			animations[active].resetAnimation();
			active = index;
		}
		
	}
	
	public BufferedImage getImage(){
		return animations[active].getImage();
	}
	
	public void update(long timePassed){
		animations[active].update(timePassed);
	}
	
	public int getCurrentFrame(){
		return animations[active].getCurrentFrame();
	}
}
