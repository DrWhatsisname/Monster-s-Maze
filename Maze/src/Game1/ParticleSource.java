package Game1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

//Particle effect for when the monster is digging
public class ParticleSource {
	
	private static final double VDECAY = 1;

	private double x;
	private double y;
	private double rate;
	private double maxV;
	private int maxAmount;
	private ArrayList<Particle> particles;
	
	public ParticleSource(double initX, double initY, double rate, double v, int maxAmount){
		x = initX;
		y = initY;
		this.rate = rate;
		maxV = v;
		particles = new ArrayList<Particle>();
		this.maxAmount = maxAmount;
	}
	
	
	public void update(long timePassed) {
		
		for (Particle p : particles){
			p.update(timePassed);
		}
		
		double modifiedRate = rate * timePassed / 1000.0;
		if (modifiedRate < 1){
			if (Math.random() < modifiedRate){
				particles.add(new Particle(x, y, Math.random() * maxV * 2 - maxV, Math.random() * maxV * 2 - maxV));
			}
		}
		else{
			int amount = (int) (Math.random() * modifiedRate + 1);
			for (int i = 0; i < amount; i++){
				particles.add(new Particle(x,y, Math.random() * maxV * 2 - maxV, Math.random() * maxV * 2 - maxV));
			}
		}
		
		
		while (particles.size() > maxAmount){
			particles.remove(0);
		}

	}

	
	public void draw(Graphics2D g) {
		
		for (Particle p : particles){
			p.draw(g);
		}

	}

	
	private class Particle{
		private double x;
		private double y;
		private double vx;
		private double vy;
		
		public Particle(double initX, double initY, double initVX, double initVY){
			x = initX;
			y = initY;
			vx = initVX;
			vy = initVY;
		}
		
		public void update(long timePassed){
			x += vx * timePassed/1000;
			y += vy * timePassed/1000;
			if (vx > 0) vx -= ParticleSource.VDECAY * timePassed / 1000.0;
			else if (vx < 0) vx += ParticleSource.VDECAY * timePassed/ 1000.0;
			if (vy > 0) vy -= (ParticleSource.VDECAY * timePassed/ 1000.0);
			else if (vy < 0) vy += ParticleSource.VDECAY * timePassed/ 1000.0;
		}
		
		public void draw(Graphics2D g){
			g.setColor(new Color(115, 70, 22));
			g.fillRect((int)x, (int)y, 10, 10);
		}
		
	}

}
