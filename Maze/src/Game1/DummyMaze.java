package Game1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Dummy maze generation for the title screen effect
public class DummyMaze {

	private TileMap map;
	private ArrayList<LightSource> lights;
	private double fadeStatus;
	private boolean fadeDir;
	
	private static final int LEVEL_SIZE = 7;
	
	public DummyMaze(){
		//Generate new maze
		MazeGen maze = new MazeGen(LEVEL_SIZE,LEVEL_SIZE-2);
		maze.generate();
		int[][] tiles = maze.getTileMap();
		
		//Use addVariation() copied from Level
		addVariation(tiles);
		
		map = new TileMap(tiles, "mazeTiles");
		lights = new ArrayList<LightSource>();
		
		//Add lights to the scene
		int numLights = (int)(Math.random() * 5) +6;
		for (int i = 0; i < numLights; i++){
			lights.add(new Torch(Math.random() * 128 * LEVEL_SIZE, Math.random() * 128 * (LEVEL_SIZE-2)));
		}
		fadeStatus = 1;
		fadeDir = false;
	}
	
	//Add variation to the types of tiles in the level
	private void addVariation(int[][] tileMap){
		for (int x = 0; x < tileMap.length; x++){
			for (int y = 0; y < tileMap[x].length; y++){
				
				if (tileMap[x][y] == 0){
					double rand = Math.random();
					if (rand < .1) tileMap[x][y] = 2;
					else if (rand < .2) tileMap[x][y] = 3;
					else if (rand < .3) tileMap[x][y] = 4;
					else if (rand < .305) tileMap[x][y] = 5;
					else if (rand < .310) tileMap[x][y] = 6;
					else if (rand < .315) tileMap[x][y] = 7;
					else if (rand < .320) tileMap[x][y] = 8;
				}
				
				if (tileMap[x][y] == 1){
					double rand = Math.random();
					if (rand < .01) tileMap[x][y] = 9;
					else if (rand < .02) tileMap[x][y] = 10;
					else if (rand < .03) tileMap[x][y] = 11;
					else if (rand < .04) tileMap[x][y] = 12;
					else if (rand < .05) tileMap[x][y] = 13;
				}
			}
		}
		
	}
	
	//Used by GameControl to determine when to create a new DummyMaze
	public double getFadeStatus(){
		return fadeStatus;
	}
	
	//Updates fadeStatus which determines how opaque the black rectangle drawn over the screen is
	public void update(long timePassed){
		fadeStatus += (fadeDir ? -timePassed : timePassed);
		
		//If fadeStatus goes over 2000, switch fade direction
		fadeDir = fadeStatus >= 2000 || fadeDir;
		if (fadeStatus > 2000){
			fadeStatus = 2000;
		}
		if (fadeStatus < 0){
			fadeStatus = 0;
		}
	}
	
	public void draw(Graphics2D g2){
		Graphics2D g = (Graphics2D) g2.create();
		
		//Translate graphics context to center scene
		g.translate(Maze.WIND_WIDTH/2-LEVEL_SIZE*64, Maze.WIND_HEIGHT/2-(LEVEL_SIZE-2)*64);
		
		map.draw(g);
		
		//Lighting
		BufferedImage shading = new BufferedImage(Maze.WIND_WIDTH*2 + 32, Maze.WIND_HEIGHT*2 + 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D lighting = shading.createGraphics();
		lighting.setColor(new Color(0, 0, 0, 255));
		lighting.fillRect(0, 0, shading.getWidth(), shading.getHeight());
		lighting.dispose();
		BufferedImage hue = new BufferedImage(Maze.WIND_WIDTH*2 + 32, Maze.WIND_HEIGHT*2 + 32, BufferedImage.TYPE_INT_ARGB);
		
			for(LightSource l : lights){ 
				l.getSource().draw(shading, hue, l.getSourceX(), l.getSourceY());
			}
			g.drawImage(hue, 0,0,null);
			g.drawImage(shading, 0,0,null);
			
		g2.setColor(new Color(0,0,0, 255-(int)(fadeStatus/2000*255)));
		g2.fillRect(0,0, Maze.WIND_WIDTH, Maze.WIND_HEIGHT);
	}
}
