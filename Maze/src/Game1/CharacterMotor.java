package Game1;

//Manages character movement
public class CharacterMotor{
	
	private static final double STANDARD_VELOCITY = 140;
	private static final double SQRT2 = Math.sqrt(.5);
	
	private double x;
	private double y;
	private double velocity;
	
	private boolean controlUp;
	private boolean controlDown;
	private boolean controlLeft;
	private boolean controlRight;
	
	//Constructor without set velocity, sets velocity to STANDRD_VELOCITY
	public CharacterMotor(double sx, double sy){
		x = sx;
		y = sy;
		velocity = STANDARD_VELOCITY;
		
		controlUp = false;
		controlDown = false;
		controlLeft = false;
		controlRight = false;
	}
	
	//Constructor with velocity
	public CharacterMotor(double sx, double sy, double sVelocity){
		x = sx;
		y = sy;
		velocity = sVelocity;
		
		controlUp = false;
		controlDown = false;
		controlLeft = false;
		controlRight = false;
	}
	
	//Sets character to move in each direction for the next frame
	public void goRight(){
		controlRight = true;
	}
	public void goLeft(){
		controlLeft = true;
	}
	public void goUp(){
		controlUp = true;
	}
	public void goDown(){
		controlDown = true;
	}
	public void goTowards(double tx, double ty){
		if (tx > this.x) controlRight = true;
		else controlLeft = true;
		if (ty > this.y) controlDown = true;
		else controlUp = true;
	}
	
	//Returns an array that holds the status of each direction
	public boolean[] getMoveStatus(){
		boolean[] result = {controlUp, controlDown, controlLeft, controlRight};
		return result;
	}
	
	//Moves the CharacterMotor over by dx in the x direction and by dy in the y direction
	public void translate(double dx, double dy){
		x += dx;
		y += dy;
	}
	
	//Returns x and y coordinates
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	
	//Updates character motor, leaves room for expansion of functionality
	public void update(long timePassed){
		this.updateControls(timePassed);
		
	}
	
	//Handles the controls, makes sure that the character moves with equal velocity in all 8 directions, also handles multiple key presses
	public void updateControls(long timePassed){
		double d = velocity * (timePassed/1000.0);
		
		if (controlUp && controlDown){
			controlUp = false;
			controlDown = false;
		}
		if (controlLeft && controlRight){
			controlLeft = false;
			controlRight = false;
		}
		
		if (controlUp && !controlDown && !controlLeft && !controlRight){
			y -= d;
		}
		else if (!controlUp && controlDown && !controlLeft && !controlRight){
			y += d;
		}
		else if (!controlUp && !controlDown && controlLeft && !controlRight){
			x -= d;
		}
		else if (!controlUp && !controlDown && !controlLeft && controlRight){
			x += d;
		}
		else if (controlUp && !controlDown && controlLeft && !controlRight){
			y -= SQRT2 * d;
			x -= SQRT2 * d;
		}
		else if (controlUp && !controlDown && !controlLeft && controlRight){
			y -= SQRT2 * d;
			x += SQRT2 * d;
		}
		else if (!controlUp && controlDown && controlLeft && !controlRight){
			y += SQRT2 * d;
			x -= SQRT2 * d;
		}
		else if (!controlUp && controlDown && !controlLeft && controlRight){
			y += SQRT2 * d;
			x += SQRT2 * d;
		}
	}
	
	public void resetDirections(){
		controlUp = false;
		controlDown = false;
		controlLeft = false;
		controlRight = false;
	}

	
	
}
