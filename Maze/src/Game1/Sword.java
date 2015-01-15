package Game1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//Represents sword in Level
public class Sword implements LightSource{

	private int x;
	private int y;
	private int width;
	private int height;
	private BufferedImage img;
	private Light light;
	
	public Sword(int x, int y){
		this.x = x;
		this.y = y;
		width = 32;
		height = 32;
		try {
			img = ImageIO.read(new File("res/spriteSheets/sword.png"));
			width = img.getWidth();
			height = img.getHeight();
		} catch (IOException e) {
			img = null;
			e.printStackTrace();
		}
		light = new Light(24, Color.cyan);
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void draw(Graphics2D g){
		if (img != null)
			g.drawImage(img, x, y, null);
		else{
			g.setColor(Color.RED);
			g.fillRect(x, y, getWidth(), getHeight());
		}
	}

	@Override
	public double getSourceX() {
		return this.x + 16;
	}

	@Override
	public double getSourceY() {
		return this.y + 16;
	}

	@Override
	public Light getSource() {
		return light;
	}

	@Override
	public int getCurrentMap() {
		return 0;
	}
}
