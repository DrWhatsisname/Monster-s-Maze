package Game1;

import java.awt.Graphics2D;

//Interface to allow polymorphism for game objects
public interface GameObject {

	public void update(long timePassed);
	public void draw(Graphics2D g);
	public double getX();
	public double getY();
	public void translate(double dx, double dy);
	public double getWidth();
	public double getHeight();
	public int getCurrentMap();
	public void hit(double amount);
}
