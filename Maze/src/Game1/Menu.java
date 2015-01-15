package Game1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

//Represents a menu with options
public class Menu {

	//Some standard choice IDs
	public static final int EXIT = 0;
	public static final int RESUME = 1;
	public static final int STORY = 2;
	public static final int CONTROLS = 3;
	public static final int BACK = 4;
	
	
	private ArrayList<Option> options;
	private int index;
	private Option selected;
	private Font font;
	private String title;
	
	public Menu(String title){
		this.title = title;
		options = new ArrayList<Option>();
		selected = null;
		font = new Font("Arial", Font.BOLD, 40);
		index = 0;
	}
	
	//Updates the currently selected option
	public void update(long timePassed){
		if (Controls.isPressed(KeyEvent.VK_UP) || Controls.isPressed('W')){
			Controls.removeKey(KeyEvent.VK_UP);
			Controls.removeKey('W');
			if (index > 0) index--;
		}
		if (Controls.isPressed(KeyEvent.VK_DOWN) || Controls.isPressed('S')){
			Controls.removeKey(KeyEvent.VK_DOWN);
			Controls.removeKey('S');
			if (index < options.size()-1) index++;
		}
		if (Controls.isPressed(KeyEvent.VK_ENTER)){
			Controls.removeKey(KeyEvent.VK_ENTER);
			selected = options.get(index);
		}
	}
	
	//Draws the menu, emphasizing the currently selected option
	public void draw(Graphics2D g){
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(font.deriveFont(60f));
		g.setColor(Color.WHITE);
		g.drawString(title, 20, 100);
		int pos = 180;
		for (int i = 0; i < options.size(); i++, pos+=font.getSize() + 5){
			if (i == index) g.setFont(font.deriveFont(50f));
			else g.setFont(font.deriveFont(20));
			g.drawString(options.get(i).getText(), 20, pos);
			
		}
	}
	
	public Option getSelected(){
		return selected;
	}
	public void clearSelected(){
		selected = null;
	}
	public void addOption(String text, int ID){
		options.add(new Option(text, ID));
	}
	
	//Represents a menu option, holds display text and the integer ID for the action to be performed when it is selected
	public class Option{
	
		
		private String text;
		private int ID;
		public Option(String s, int i){
			text = s;
			ID = i;
		}
		
		public String getText(){
			return text;
		}
		public int getID(){
			return ID;
		}

		
	}
}
