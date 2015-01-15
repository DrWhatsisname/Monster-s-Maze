package Game1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//Game object, hole that the monster digs, player can fall through
public class Hole {

	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	
	private int x;
	private int y;
	private boolean active;
	private BufferedImage img;
	
	public Hole(int ix, int iy){
		x = ix;
		y = iy;
		active = false;
		
		try {
			img = ImageIO.read(new File("res/spriteSheets/hole.png"));
		} catch (IOException e) {
			img = null;
			e.printStackTrace();
		}
	}
	
	public boolean isOver(GameObject go){
		Rectangle hole = new Rectangle(x,y,WIDTH, HEIGHT);
		Rectangle other = new Rectangle((int)go.getX(), (int)(go.getY() + go.getHeight()*3/4), (int)go.getWidth(), (int)(go.getWidth()/4));
		if (active)	return hole.intersects(other);
		else return false;
	}
	
	public void activate(){
		active = true;
	}
	
	public boolean equals(Object o){
		if (o instanceof Hole){
			Hole h = (Hole) o;
			return this.x == h.x && this.y == h.y;
		}
		else return false;
		
		
	}
	
	public void draw(Graphics2D g){
		if (img == null){
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, WIDTH, HEIGHT);
		}
		else g.drawImage(img, x, y, null);
	}
	
}
