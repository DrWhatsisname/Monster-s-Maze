package Game1;

import java.awt.Color;
import java.awt.Graphics2D;

//Holds a light with random parameters, used in DummyMaze for the intro screen
public class Torch implements LightSource {

	double x;
	double y;
	Light light;
	public Torch(double sx, double sy){
		x = sx;
		y = sy;
		light = new Light(Math.random() * 100 + 28, new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
	}
	
	public double getSourceX() {
		return x;
	}

	
	public double getSourceY() {
		return y;
	}

	
	public Light getSource() {
		return light;
	}

	
	public void update(long timePassed) {
		// TODO Auto-generated method stub

	}

	
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void translate(double dx, double dy) {
		x += dx;
		y += dy;

	}

	
	public double getWidth() {
		return 4;
	}

	
	public double getHeight() {
		return 4;
	}
	public int getCurrentMap(){
		return 0;
	}

}
