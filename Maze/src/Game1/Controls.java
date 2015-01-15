package Game1;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

//Handles keyboard input
public class Controls {
	
	private static ArrayList<Integer> keys = new ArrayList<Integer>();
	
	//Adds keys that are currently pressed to the keys ArrayList
	public static void keyPressed(KeyEvent e){
		if (!keys.contains(e.getKeyCode())) keys.add(e.getKeyCode());
	}
	
	//Removes keys that are released from the list
	public static void keyReleased(KeyEvent e){
		keys.remove(new Integer(e.getKeyCode()));
	}
	
	//Checks if the key with the keycode i is currently pressed
	public static boolean isPressed(int i){
		return keys.contains(i);
	}
	
	public static void removeKey(int i){
		keys.remove(new Integer(i));
	}
	//Checks if any keys are pressed
	public static boolean isPressed(){
		return keys.size() != 0;
	}
	public static void removeKeys(){
		keys= new ArrayList<Integer>();
	}
	
}
