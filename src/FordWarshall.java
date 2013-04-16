import java.util.HashMap;
import java.util.Set;

public class FordWarshall implements Runnable {
	private long array[][];
	private Nodes[][] squares;
	private int width;
	private int height;
	private int counter = 0;
	private int startx;
	private int starty;
	private HashMap<Integer, Vertex> map = new HashMap<Integer, Vertex>();
	private HashMap<Vertex, Integer> map2 = new HashMap<Vertex, Integer>();
	private int goalx, goaly;

	public FordWarshall(Nodes[][] squares, int width, int height, int goalx,
			int goaly, int startx, int starty) {
		this.startx = startx;
		this.starty = starty;
		this.squares = squares;
		this.width = width;
		this.height = height;
		array = new long[width * height][width * height];
		this.goalx = goalx;
		this.goaly = goaly;
		Associate();
		
		
	}

	private void init() {
		for (int i = 0; i < counter; ++i) {
			for (int j = 0; j < counter; ++j) {
				if (i != j)
					array[i][j] = Integer.MAX_VALUE - counter;
				
			}
		}
	}

	public void Associate() {

		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				Vertex vert = new Vertex(i, j);
				map.put(counter, vert);
				map2.put(vert, counter);
				++counter;
			}
		}

	}

	public void setCostofNeighbors() {
		for (int i = 0; i < counter; ++i) {
			for (int j = 0; j < counter; ++j) {
				Vertex vert = map.get(i);
				Vertex[] vertArray = getNeighbors(vert);
				for (Vertex vertex : vertArray) {
					if (vertex == null)
						continue;
					vertex.setCost(squares[vertex.i][vertex.j].cost);
					array[map2.get(vert)][map2.get(vertex)] = vertex.cost;
					
				}
			}
			
		}
	}

	public long[][] returnArray() {
		return array;
	}

	public long returnDist(int i, int j) {
		int count = i * height + j;
		int countgoal = height * goalx + goaly;
		return array[count][countgoal];
	}

	public boolean Algorithm() {

		for (int k = 0; k < counter; ++k) {
			if (k == (startx * width + starty))
				continue;
			if (k == (goalx * width + goaly))
				continue;
			for (int i = 0; i < counter; ++i) {
				if (i == (goalx * width + goaly))
					continue;
				for (int j = 0; j < counter; ++j) {
					if (j == (startx * width + starty))
						continue;
					
					if (array[i][j] > array[i][k] + array[k][j]) {
						
						array[i][j] = array[i][k] + array[k][j];
					}
				}
			}
		}
		return true;
	}

	public Vertex[] getNeighbors(Vertex v) {
		int x = v.getPositionX();
		int y = v.getPositionY();
		int count = map2.get(v);
		Vertex[] vert = new Vertex[4];
		if (x + 1 < width)
			vert[0] = map.get(count + height);
		if (x - 1 >= 0)
			vert[1] = map.get(count - height);
		if (y - 1 >= 0)
			vert[2] = map.get(count - 1);
		if (y + 1 < height)
			vert[3] = map.get(count + 1);
		return vert;
	}

	private class Vertex {
		private int i;
		private int j;
		private int cost = 0;
		private String name;

		public Vertex(int x, int y) {
			i = x;
			j = y;
			name = squares[i][j].label;
		}

		public void setCost(int cost) {
			this.cost = cost;

		}

		public int getPositionX() {
			return i;
		}

		public int getPositionY() {
			return j;
		}
	}

	public void printArray() {

		for (int i = 0; i < counter; ++i) {
			System.out.print(array[i] + " ");
			for (int j = 0; j < counter; ++j) {
				System.out.println(array[i][j]);
			}
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		init();
		setCostofNeighbors();
		Algorithm();

	}

}
