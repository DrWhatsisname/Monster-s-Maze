package Game1;

import java.awt.Graphics2D;

//Represents a tile map and a collision map packaged together for easier use in Level
public class Map {

	private TileMap tileMap;
	private boolean[][] collisionMap;
	
	public Map(TileMap tile, boolean[][] collision){
		tileMap = tile;
		collisionMap = collision;
	}
	
	public boolean[][] getCollisionMap(){
		return collisionMap;
	}
	
	public void draw(Graphics2D g, int xLim, int yLim){
		tileMap.draw(g, xLim, yLim);
	}
	
	public void clearTile(int x, int y){
		if (x > 0 && y > 0 && x < collisionMap.length && y < collisionMap[x].length){
			tileMap.clearTile(x, y);
			collisionMap[x][y] = false;
		}
	}
	
	public int getMapWidth(){
		return tileMap.getMapWidth();
	}
	public int getMapHeight(){
		return tileMap.getMapHeight();
	}
}
