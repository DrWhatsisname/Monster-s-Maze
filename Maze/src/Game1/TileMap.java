package Game1;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//Loads level from an array of tile IDs with indicated tile set
public class TileMap {
	
	public static final int TILE_SIZE = 32;

	private int[][] tiles;
	private BufferedImage[] tileSet;
	
	//Constructor, loads tile IDs to an array of images
	public TileMap(int[][] map, String tileSetPath){
		try {
			tileSet = parse(ImageIO.read(new File("res/tileSets/" + tileSetPath + ".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tiles = map;
	}
	
	private BufferedImage[] parse(BufferedImage img){
		BufferedImage[] result = new BufferedImage[(img.getWidth() / TILE_SIZE) * (img.getHeight() / TILE_SIZE)];
		for (int y = 0; y < img.getHeight(); y += TILE_SIZE){
			for (int x = 0; x < img.getWidth(); x += TILE_SIZE){
				result[(x/TILE_SIZE) + ((y/TILE_SIZE) * (img.getWidth()/TILE_SIZE))] = img.getSubimage(x, y, TILE_SIZE, TILE_SIZE);
			}
		}
		return result;
	}
	
	public void clearTile(int x, int y){
		if (x > 0 && y > 0 && x < tiles.length && y < tiles[x].length){
			tiles[x][y] = 1;
		}
	}
	
	//Returns map's dimensions for crowd generation
	public int getMapWidth(){
		return tiles.length;
	}
	public int getMapHeight(){
		return tiles[0].length;
	}
	
	//Draws each tile in their positions, Graphics object is translated in Level class before this method is called
	public void draw(Graphics g, int xLim, int yLim){
	
		for (int x = (xLim)/TILE_SIZE; x < (xLim + Maze.WIND_WIDTH + 48)/TILE_SIZE; x++){
			for (int y = (yLim)/TILE_SIZE; y < (yLim + Maze.WIND_HEIGHT + 48)/TILE_SIZE; y++){
				if(x >= 0 && y >= 0 && x < tiles.length && y < tiles[x].length) g.drawImage(tileSet[tiles[x][y]], x * TILE_SIZE, y * TILE_SIZE, null);
			}
			
		}
	
			
	}
	public void draw(Graphics g){
		for (int x = 0; x < tiles.length; x++){
			for (int y = 0; y < tiles[x].length; y++){
				g.drawImage(tileSet[tiles[x][y]],x*TILE_SIZE, y*TILE_SIZE,null);
			}
		}
	}
	
	
	
	
}
