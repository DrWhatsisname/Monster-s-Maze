package Game1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Represents a level in the game
public class Level{
	
	public static final int LEVEL_SIZE = 8;
	
	private ArrayList<LightSource> lights;
	private boolean lightsEnabled;
	private boolean monsterEnabled;
	
	private Map activeMap;
	private Map mazeMap;
	private Map underMap;
	
	private Player p;
	private Monster m;
	private Sword s;
	private ParticleSource digEffect = null;
	private ArrayList<Hole> holes;
	
	public Level(){
		//Generate Maze
		MazeGen maze = new MazeGen(LEVEL_SIZE, LEVEL_SIZE);
		maze.generate();
		
		//Initialize tile maps and collision maps
		int[][] tileMap = maze.getTileMap();
		boolean[][] collisionMap = new boolean[tileMap.length][tileMap[0].length];
		for (int x = 0; x < tileMap.length; x++){ 
			for (int y = 0; y < tileMap[x].length; y++){ 
				 collisionMap[x][y] = (tileMap[x][y] > 0 ? true : false); 
			} 
		}
		
		addVariation(tileMap);
		boolean[][] underCollision = new boolean[tileMap.length][tileMap[0].length];
		for (int x = 0; x < tileMap.length; x++){ 
			for (int y = 0; y < tileMap[x].length; y++){ 
				underCollision[x][y] = true;
			} 
		}
		
		mazeMap = new Map(new TileMap(tileMap, "mazeTiles"), collisionMap);
		underMap = new Map(new TileMap(new int[tileMap.length][tileMap[0].length], "undergroundTiles"), underCollision);
		activeMap = mazeMap;
		
		//Initialize game objects
		p = new Player((int)(Math.random() * LEVEL_SIZE) * 128 + (int)(Math.random() * 16 + 48),(int)(Math.random() * LEVEL_SIZE) * 128 + (int)(Math.random() * 16 + 48));
		m = new Monster(Math.random() * (LEVEL_SIZE * 4 - 2) * 32 + 32, Math.random() * (LEVEL_SIZE * 4 - 3) * 32 + 32, p);
		s = new Sword((int)(Math.random() * LEVEL_SIZE) * 128 + (int)(Math.random() * 16 + 48),(int)(Math.random() * LEVEL_SIZE) * 128 + (int)(Math.random() * 16 + 48));
		
		//Initialize light sources
		lights = new ArrayList<LightSource>();
		lights.add(p);
		lights.add(s);
		
		lightsEnabled = true;
		monsterEnabled = true;
		holes = new ArrayList<Hole>();
	}
	
	
	//Sends timePassed to GameObects, handles collisions
	public void update(long timePassed) {
		p.update(timePassed);
		//Sets activeMap to the player's current map
		if (p.getCurrentMap() == 0){
			activeMap = mazeMap;
		}
		else if (p.getCurrentMap() == 1){
			activeMap = underMap;
		}
		checkCollide(p, activeMap);
		
		//Logic for the player to pick up the sword
		if (s != null && activeMap == mazeMap && !(p.getX() > s.getX() +s.getWidth() || p.getX() + p.getWidth() < s.getX() || p.getY() > s.getY() +s.getHeight() || p.getY() + p.getHeight() < s.getY())){
			lights.remove(s);
			s = null;
			p.getSword();
		}
		
		if (digEffect != null) digEffect.update(timePassed);
		
		//Add a new hole if the monster is digging
		if (m.isDigging()){
			Hole hole = new Hole((int)m.getX(), (int)m.getY() + 16);
			holes.add(hole);
		}
		else{
			if (holes.size() > 0) holes.get(holes.size()-1).activate();
		}
		
		//Move the player underground if he steps on a hole
		for (Hole h : holes){
			if (p.getCurrentMap() == 0 && h.isOver(p)) p.changeCurrentMap(1);
		}
		
		//Updates monster if enabled, can be disabled in debug mode
		if (monsterEnabled){
			m.update(timePassed);
			if (m.getCurrentMap() == 0){
				checkCollide(m, mazeMap);
			}
			else if (m.getCurrentMap() == 1){
				//Clear all tiles that the monster is currently in, monster "digs" them out
				for(int x = (int) (m.getX()/32); x <= (int)((m.getX() + m.getWidth())/32); x++){
					for(int y = (int) (m.getY()/32); y <= (int)((m.getY() + m.getHeight())/32); y++){
						if (x != 0 && y != 0 && x != LEVEL_SIZE * 4 - 1 && y != LEVEL_SIZE * 4 -1){
							underMap.clearTile(x, y);
						}
						else{
							checkCollide(m,underMap);
						}
						
					}
				}
			}
		}
	}
	
	public double getPlayerHealth(){
		return p.getHealth();
	}
	
	public boolean playerIsDead(){
		return p.isDead();
	}
	
	//Used in GameControl for debug mode
	public void toggleLights(){
		lightsEnabled = !lightsEnabled;
	}
	public void toggleMonster(){
		monsterEnabled = !monsterEnabled;
	}

	
	//Adds variation to the tile map's tiles
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

	//Collisions
	private void checkCollide(GameObject o, Map map){
		boolean[][] collisionMap = map.getCollisionMap();
		
		//If object is outside level, throw exception
		if (o.getX() < 0 || o.getY() < 0 || o.getX() + o.getWidth() > LEVEL_SIZE * 128 || o.getY() +o.getHeight() > LEVEL_SIZE * 128) throw new ArrayIndexOutOfBoundsException("Object outside level!");
		
		///Sides
		
		//Left side
		if (collisionMap[(int)o.getX()/32][(int)o.getY()/32] && collisionMap[(int)o.getX()/32][(int)(o.getY() + o.getHeight())/32]){
			double transX = (int)o.getX()/32 * 32 + 32 - o.getX();
			o.translate(transX, 0);
		}
		
		//Top side
		if (collisionMap[(int)o.getX()/32][(int)o.getY()/32] && collisionMap[(int)(o.getX()+o.getWidth())/32][(int)(o.getY())/32]){
			double transY = (int)o.getY()/32 * 32 + 32 - o.getY();
			o.translate(0, transY);
		}
		
		//Right side
		if (collisionMap[(int)(o.getX()+o.getWidth())/32][(int)o.getY()/32] && collisionMap[(int)(o.getX()+o.getWidth())/32][(int)(o.getY() + o.getHeight())/32]){
			double transX = (int)(o.getX() + o.getWidth())/32 * 32 - (o.getX() + o.getWidth());
			o.translate(transX, 0);
		}
		
		//Bottom side
		if (collisionMap[(int)(o.getX())/32][(int)(o.getY() + o.getHeight())/32] && collisionMap[(int)(o.getX()+o.getWidth())/32][(int)(o.getY() + o.getHeight())/32]){
			double transY = (int)(o.getY() + o.getHeight())/32 * 32 - (o.getY() + o.getHeight());
			o.translate(0, transY);
		}
		
		///Corners (assume that all full side collisions have been resolved, only corner collisions left)
		
		//Top left
		if (collisionMap[(int)o.getX()/32][(int)o.getY()/32]){
			double transX = (int)o.getX()/32 * 32 + 32 - o.getX();
			double transY = (int)o.getY()/32 * 32 + 32 - o.getY();
			if (transX < transY) o.translate(transX, 0);
			else o.translate(0, transY);
		}
		
		//Top right
		if(collisionMap[(int)(o.getX()+o.getWidth())/32][(int)(o.getY())/32]){
			double transX = (int)(o.getX() + o.getWidth())/32 * 32 - (o.getX() + o.getWidth());
			double transY = (int)o.getY()/32 * 32 + 32 - o.getY();
			if (-transX < transY) o.translate(transX, 0);
			else o.translate(0, transY);
		}
		
		//Bottom left
		if(collisionMap[(int)(o.getX())/32][(int)(o.getY() + o.getHeight())/32]){
			double transX = (int)o.getX()/32 * 32 + 32 - o.getX();
			double transY = (int)(o.getY() + o.getHeight())/32 * 32 - (o.getY() + o.getHeight());
			if (transX < -transY) o.translate(transX, 0);
			else o.translate(0, transY);
		}
		
		//Bottom right
		if(collisionMap[(int)(o.getX()+o.getWidth())/32][(int)(o.getY() + o.getHeight())/32]){
			double transX = (int)(o.getX() + o.getWidth())/32 * 32 - (o.getX() + o.getWidth());
			double transY = (int)(o.getY() + o.getHeight())/32 * 32 - (o.getY() + o.getHeight());
			if (transX > transY) o.translate(transX, 0);
			else o.translate(0, transY);
		}
	}
	
	
	
	//Handles drawing
	public void draw(Graphics2D gOrig) {
		Graphics2D g = (Graphics2D)gOrig.create();
		
		//Translates graphics object so that player is in the center, and other objects are drawn relative to the player
		int transX = Maze.WIND_WIDTH/2 - (int)p.getX()- (int)p.getWidth()/2;
		int transY = Maze.WIND_HEIGHT/2 - (int)p.getY() - (int)p.getHeight()/2;
		if (p.getX() + p.getWidth()/2< Maze.WIND_WIDTH/2) transX = 0;
		else if (p.getX() + p.getWidth()/2 > (LEVEL_SIZE * 128) - (Maze.WIND_WIDTH/2)) transX = Maze.WIND_WIDTH - (LEVEL_SIZE * 128);
		if (p.getY() + p.getHeight()/2 + 25 < Maze.WIND_HEIGHT/2) transY = 25;
		else if (p.getY() + p.getHeight()/2 > (LEVEL_SIZE * 128) - (Maze.WIND_HEIGHT/2)) transY =  Maze.WIND_HEIGHT - (LEVEL_SIZE * 128);
		g.translate(transX, transY);
		
		//Draw the level
		activeMap.draw(g, (int)p.getX() - Maze.WIND_WIDTH/2, (int)p.getY() - Maze.WIND_HEIGHT/2);
		
		//Draw Holes
		if (activeMap == mazeMap){
			for (Hole h : holes){
				h.draw(g);
			}
		}
		
		
		if (s != null && activeMap == mazeMap) s.draw(g);
		
		//Draw the GameObects
		p.draw(g);
		if (monsterEnabled){
			if (m.getCurrentMap() == p.getCurrentMap()) m.draw(g);
			if (m.isDigging()){
				if (digEffect != null){
					digEffect.draw(g);
				}
				else digEffect = new ParticleSource(m.getX() +16, m.getY()+16, 500, 100, 100);
			}
			else digEffect = null;
		}
		
		
		
		
		//Create Lighting layer
		BufferedImage shading = new BufferedImage(Maze.WIND_WIDTH*2 + 32, Maze.WIND_HEIGHT*2 + 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D lighting = shading.createGraphics();
		lighting.setColor(new Color(0, 0, 0, 255));
		lighting.fillRect(0, 0, shading.getWidth(), shading.getHeight());
		lighting.dispose();
		BufferedImage hue = new BufferedImage(Maze.WIND_WIDTH*2 + 32, Maze.WIND_HEIGHT*2 + 32, BufferedImage.TYPE_INT_ARGB);
		//Lighting
		if (lightsEnabled){
			for(LightSource l : lights){ 
				if (l.getCurrentMap() == p.getCurrentMap())l.getSource().draw(shading, hue, l.getSourceX() - ((int)p.getX() - shading.getWidth()/2), l.getSourceY() - ((int)p.getY() - shading.getHeight()/2));
			}
			g.drawImage(hue, ((int)p.getX() - hue.getWidth()/2), ((int)p.getY() - hue.getHeight()/2), null);
			g.drawImage(shading, ((int)p.getX() - shading.getWidth()/2), ((int)p.getY() - shading.getHeight()/2), null);
		}
	}
	
	public boolean victory(){
		return m.isDead();
	}


	public void dispose() {
		p.dispose();
		m.dispose();
		
	}
	
}
