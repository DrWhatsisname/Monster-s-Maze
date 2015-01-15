package Game1;

//Generates a maze for Level and DummyMaze
public class MazeGen {
	
	private Cell[][] cells;

	public MazeGen(int width, int height){
		cells = new Cell[width][height];
		for (int x = 0; x < cells.length; x++){
			for (int y = 0; y < cells[x].length; y++){
				cells[x][y] = new Cell();
			}
		}
	}
	
	//Generates a maze using the depth first search algorithm
	public void generate(){
		depthFirst((int)(Math.random() * cells.length), (int)(Math.random() * cells[0].length));
	}
	
	//Returns a tile map based on the state of the cells
	public int[][] getTileMap(){
		int[][] map = new int[cells.length * 4][cells[0].length * 4];
		for (int x = 0; x < cells.length; x++){
			for (int y = 0; y < cells[x].length; y++){
				for(int tileX = x * 4; tileX < (x + 1) * 4; tileX++){
					for (int tileY = y * 4; tileY < (y + 1) * 4; tileY++){
						if (tileY % 4 == 3 && cells[x][y].wallUp()) map[tileX][tileY] = 1;
						else if (tileY % 4 == 0 && cells[x][y].wallDown()) map[tileX][tileY] = 1;
						else if (tileX % 4 == 0 && cells[x][y].wallLeft()) map[tileX][tileY] = 1;
						else if (tileX % 4 == 3 && cells[x][y].wallRight()) map[tileX][tileY] = 1;
						else map[tileX][tileY] = 0;
					}
				}
				map[x * 4][y * 4] = 1;
				map[x * 4 + 3][y * 4] = 1;
				map[x * 4 + 3][y * 4 + 3] = 1;
				map[x * 4][y * 4 + 3] = 1;
			}
		}
		return map;
	}
	
	//Maze generation code
	private void depthFirst(int x, int y){
		int nextCell = (int)(Math.random() * 4 + 1);
		if (nextCell == 1){
			if (y < cells[x].length - 1 && !cells[x][y+1].isVisited()){
				cells[x][y].removeUp();
				cells[x][y+1].removeDown();
				depthFirst(x, y+1);
			}
			if(x < cells.length - 1 && !cells[x+1][y].isVisited()){
				cells[x][y].removeRight();
				cells[x+1][y].removeLeft();
				depthFirst(x+1, y);
			}
			if(y > 0 && !cells[x][y-1].isVisited()){
				cells[x][y].removeDown();
				cells[x][y-1].removeUp();
				depthFirst(x, y-1);
			}
			if (x > 0 && !cells[x-1][y].isVisited()){
				cells[x][y].removeLeft();
				cells[x-1][y].removeRight();
				depthFirst(x-1, y);
			}
		}
		else if (nextCell == 2){
			if(x < cells.length - 1 && !cells[x+1][y].isVisited()){
				cells[x][y].removeRight();
				cells[x+1][y].removeLeft();
				depthFirst(x+1, y);
			}
			if(y > 0 && !cells[x][y-1].isVisited()){
				cells[x][y].removeDown();
				cells[x][y-1].removeUp();
				depthFirst(x, y-1);
			}
			if (x > 0 && !cells[x-1][y].isVisited()){
				cells[x][y].removeLeft();
				cells[x-1][y].removeRight();
				depthFirst(x-1, y);
			}
			if (y < cells[x].length - 1 && !cells[x][y+1].isVisited()){
				cells[x][y].removeUp();
				cells[x][y+1].removeDown();
				depthFirst(x, y+1);
			}
		}
		else if (nextCell == 3){
			if(y > 0 && !cells[x][y-1].isVisited()){
				cells[x][y].removeDown();
				cells[x][y-1].removeUp();
				depthFirst(x, y-1);
			}
			if (x > 0 && !cells[x-1][y].isVisited()){
				cells[x][y].removeLeft();
				cells[x-1][y].removeRight();
				depthFirst(x-1, y);
			}
			if (y < cells[x].length - 1 && !cells[x][y+1].isVisited()){
				cells[x][y].removeUp();
				cells[x][y+1].removeDown();
				depthFirst(x, y+1);
			}
			if(x < cells.length - 1 && !cells[x+1][y].isVisited()){
				cells[x][y].removeRight();
				cells[x+1][y].removeLeft();
				depthFirst(x+1, y);
			}
			
		}
		else if (nextCell == 4){
			if (x > 0 && !cells[x-1][y].isVisited()){
				cells[x][y].removeLeft();
				cells[x-1][y].removeRight();
				depthFirst(x-1, y);
			}
			if (y < cells[x].length - 1 && !cells[x][y+1].isVisited()){
				cells[x][y].removeUp();
				cells[x][y+1].removeDown();
				depthFirst(x, y+1);
			}
			if(x < cells.length - 1 && !cells[x+1][y].isVisited()){
				cells[x][y].removeRight();
				cells[x+1][y].removeLeft();
				depthFirst(x+1, y);
			}
			if(y > 0 && !cells[x][y-1].isVisited()){
				cells[x][y].removeDown();
				cells[x][y-1].removeUp();
				depthFirst(x, y-1);
			}
		}
	}
	public Cell[][] getCells(){
		return cells;
	}
	
	//Class that represents a cell of the maze, has four possible walls which can be removed
	public class Cell{
		private boolean[] walls;
		public Cell(){
			walls = new boolean[4];
			for (int i = 0; i < 4; i++){
				walls[i] = true;
			}
		}
		public boolean wallUp(){
			return walls[0];
		}
		public boolean wallRight(){
			return walls[1];
		}
		public boolean wallDown(){
			return walls[2];
		}
		public boolean wallLeft(){
			return walls[3];
		}
		
		public void removeLeft(){
			walls[3] = false;
		}
		public void removeRight(){
			walls[1] = false;
		}
		public void removeUp(){
			walls[0] = false;
		}
		public void removeDown(){
			walls[2] = false;
		}
		
		public boolean isVisited(){
			for (int i = 0; i < 4; i++){
				if (!walls[i]) return true;
			}
			return false;
		}
	}
}
