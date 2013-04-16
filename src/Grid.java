import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

import java.util.List;
import java.util.PriorityQueue;

import java.util.Random;

import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.SwingUtilities;

public class Grid extends JFrame implements ActionListener, Runnable {
	
	boolean random;
	boolean reset;
	private int width;
	private int height;
	private JFrame frame;

	private Nodes[][] squares;
	private Random rand;
	public int goalx;
	int goaly;
	private int startx;
	private int starty;
	private int counter = 0;
	private JPanel gridHolder;
	private JPanel top;
	private JButton startButton;
	private PriorityQueue<Nodes> unknown = new PriorityQueue<Nodes>();
	private Set<Nodes> settled = new HashSet<Nodes>();
	private JButton Pause;
	private JButton AStar;
	private JButton Reset;
	private FordWarshall ford;
	private long array[][];
	Thread f;
	public Grid(int width, int height, boolean random) {
		super();
		reset = false;
		this.width = width;
		this.height = height;
		rand = new Random();
		goalx = rand.nextInt(width);
		// System.out.println(goalx);
		// goalx = 0;

		goaly = rand.nextInt(height);
		// System.out.println(goaly);
		// goaly = 0;

		startx = rand.nextInt(width);
		// System.out.println(startx);
		// startx = 2;

		starty = rand.nextInt(height);
		// System.out.println(starty);
		// starty = 0;
		while(goalx == startx && starty == goaly){
			startx = rand.nextInt(width);
		}
		squares = new Nodes[width][height];

		setLayout(new BorderLayout());
		this.random = random;
		display();

		setSize(600, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		ford = new FordWarshall(squares, width, height, goalx, goaly, startx,
				starty);
		f = new Thread(ford);
	
	
	}

	public Grid(int width, int height, int startx, int starty, int goalx,
			int goaly, Nodes[][] squares, FordWarshall ford) {
		super();

		this.ford = ford;
		setSize(600, 650);
		// reset = false;
		this.width = width;
		this.height = height;
		this.goalx = goalx;
		// System.out.println(goalx);
		// goalx = 0;

		this.goaly = goaly;
		// System.out.println(goaly);
		// goaly = 0;

		this.startx = startx;
		// System.out.println(startx);
		// startx = 9;

		this.starty = starty;
		// System.out.println(starty);
		// starty = 3;
		this.squares = squares;
		setLayout(new BorderLayout());
		display2();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public Grid() {

	}

	public void display2() {
		random = false;
		gridHolder = new JPanel(new GridLayout(width, height));
		Reset = new JButton("Reset");
		Reset.addActionListener(this);
		top = new JPanel();
		startButton = new JButton("Dijsktrka");
		startButton.addActionListener(this);
		top.add(startButton);
		AStar = new JButton("A*");
		top.add(AStar);
		top.add(Reset);
		AStar.addActionListener(this);
		this.getContentPane().add(top);
		add(top, BorderLayout.NORTH);
		int counter = 0;
		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {
				if (i == startx && j == starty) {
					++counter;
					squares[i][j].setText("START");
					this.squares[i][j].distance = 0;
					squares[i][j].setForeground(Color.red);
					squares[i][j].heuristic = 0;
					gridHolder.add(squares[i][j]);
					squares[i][j].setOpaque(true);
				} else if (goaly == j && goalx == i) {
					this.squares[i][j].distance = Integer.MAX_VALUE - 101;
					squares[i][j].setText("GOAL");
					gridHolder.add(squares[i][j]);
					squares[i][j].setForeground(Color.RED);
					squares[i][j].setOpaque(true);
					++counter;
				} else {
					this.squares[i][j].distance = Integer.MAX_VALUE - 101;
					squares[i][j].setText("" + counter++);
					gridHolder.add(squares[i][j]);
					squares[i][j].setForeground(Color.BLACK);
					squares[i][j].setOpaque(false);
					squares[i][j].heuristic = 0;
				}
			}
		}
		this.getContentPane().add(gridHolder, BorderLayout.CENTER);
		setVisible(true);
		gridHolder.repaint();
	}

	public void display() {
		
		gridHolder = new JPanel(new GridLayout(width, height));
		Reset = new JButton("Reset");
		Reset.addActionListener(this);
		top = new JPanel();
		startButton = new JButton("Dijsktrka");
		startButton.addActionListener(this);
		top.add(startButton);
		AStar = new JButton("A*");
		top.add(AStar);
		top.add(Reset);
		AStar.addActionListener(this);
		this.getContentPane().add(top);

		add(top, BorderLayout.NORTH);
		
		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {
				if (i == startx && j == starty) {
					squares[i][j] = new Nodes(i, j, "START");
					squares[i][j].setCost(0);
					squares[i][j].distance = 0;
					squares[i][j].setForeground(Color.red);
					gridHolder.add(squares[i][j]);
					squares[i][j].setOpaque(true);
					squares[i][j].setBackground(Color.GRAY);
					++counter;
				} else if (goaly == j && goalx == i) {
					squares[i][j] = new Nodes(i, j, "GOAL");
					++counter;
					setCosts(i, j);
					squares[i][j].setBackground(Color.GRAY);
					squares[i][j].setForeground(Color.RED);
					gridHolder.add(squares[i][j]);
					squares[i][j].setOpaque(true);

				} else {
					squares[i][j] = new Nodes(i, j, "" + counter++);
					setCosts(i, j);
					squares[i][j].setBackground(Color.GRAY);
					squares[i][j].setText(" " + squares[i][j].cost);
					gridHolder.add(squares[i][j]);
					squares[i][j].setOpaque(true);

				}

			}

		}

		this.getContentPane().add(gridHolder, BorderLayout.CENTER);
		setVisible(true);
	}

	public void setCosts(int i, int j) {

		if (random == true) {
			squares[i][j].cost = rand.nextInt(50);
		} else {
			squares[i][j].cost = 2;
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Dijsktrka")) {
			Thread dijstrka = new Thread(this, "Dijstrka");
			dijstrka.start();

		} else if (e.getActionCommand().equals("A*")) {
			Thread Astar = new Thread(this, "A*");
			Astar.start();
		
			
		} else if (e.getActionCommand().equals("Reset")) {
			Thread reset = new Thread(this, "Reset");
			reset.start();
		}
	}

	public List<Nodes> getRoute(int x, int y) {
		List<Nodes> list = new ArrayList<Nodes>();
		if (x != startx || y != starty) {
			list.addAll(getRoute(squares[x][y].prev.xp, squares[x][y].prev.yp));
		}
		list.add(squares[x][y]);

		return list;

	}

	public void printRoute(List<Nodes> list) {
		String s = "The path is: \n";
		StringBuffer sb = new StringBuffer(s);

		for (Nodes nodes : list) {
			sb.append("\n" + nodes.label + " has COST of " + nodes.cost + " and has a DISTANCE of " + nodes.distance + "\n");
			
			nodes.setForeground(Color.RED);
		}
		s = sb.toString();

		DisplayMessage(s);
	}

	public static void main(String[] args) throws InterruptedException {

		Grid fake = new Grid();
		String s = fake.UserInput(
				"Enter Width and Height \n           Enter Width First",
				"Width and Height");
		boolean Randvalue = fake
				.questions("Click Yes if you want to randomly assign costs to vertices; \n Click No if you want a default cost of 10");

		while (s == null) {
			s = fake.UserInput(
					"Enter Width and Height  \n             Enter Width First",
					"Width and Height");
		}

		fake = null;
		Scanner scan = new Scanner(s);

		Grid myGrid = new Grid(scan.nextInt(), scan.nextInt(), Randvalue);
		scan.close();

	}

	public void Dijstrka(boolean more, boolean useFord) {
	
		settled.add(squares[startx][starty]);
		try {
			SwingUtilities.invokeAndWait(new UpdateButtons(startx, starty));
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		getNeighbors(squares[startx][starty], more, useFord);

		if (unknown == null) {
			System.out.println("unknown is null");

		}
		while (!unknown.isEmpty()) {
//			System.out.println("PQ:");
//			for (Nodes node : unknown) {
//				System.out
//						.println(node.label + " node distance = "
//								+ node.distance + " node heuristic = "
//								+ node.heuristic);
//			}
			Nodes curr = unknown.poll();
			int x = curr.xp;
			int y = curr.yp;

			if (settled.add(curr)) {
				try {
					SwingUtilities.invokeAndWait(new UpdateButtons(x, y));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				continue;

			if (x == goalx && y == goaly) {
				List<Nodes> list = getRoute(goalx, goaly);
				printRoute(list);
				System.out.println("Size of settled is " + settled.size());
				settled.clear();
				unknown.clear();
				break;
			}

			getNeighbors(curr, more, useFord);

		}

	}

	@Override
	public void run() {
		if (Thread.currentThread().getName().equals("Dijstrka"))
			Dijstrka(false, false);
		else if (Thread.currentThread().getName().equals("A*")) {
			boolean useFord = questions("Click Yes to use Ford-Warshall algorithm. No: use Manhattan method");
			if(useFord){
				try {
					if(f == null) f = new Thread(ford);
					f.start();
					Thread.sleep(1000);
					f.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Dijstrka(
					true, useFord);
					
		} else {
			if (questions("Do you want to a new graph")) {

				if (questions("Do you want to enter new width and height?")) {
					String s = UserInput(
							"Enter New Width and Height \n                        Enter Width First",
							"Width and Height");
					if (s == null) {
						System.out.println("Error!");
						return;
					}
					Scanner tokenizer = new Scanner(s);
					random = questions("Click Yes If You Want to Assign Random Costs\n");
					new Grid(tokenizer.nextInt(), tokenizer.nextInt(), random);
					return;
				}
				this.random = questions("Random Costs?");
				new Grid(width, height, this.random);
			} else {
				new Grid(width, height, startx, starty, goalx, goaly, squares,
						ford);
			}

		}

	}

	public void getNeighbors(Nodes start, boolean more, boolean useFord) {

		int x = start.xp;
		int y = start.yp;

		if (x - 1 >= 0 && !settled.contains(squares[x - 1][y])) {
			updateDistance(start, x - 1, y, more, useFord);
			unknown.add(squares[x - 1][y]);
		}
		if (y - 1 >= 0 && !settled.contains(squares[x][y - 1])) {
			updateDistance(start, x, y - 1, more, useFord);
			unknown.add(squares[x][y - 1]);
		}
		if (x + 1 < width && !settled.contains(squares[x + 1][y])) {
			updateDistance(start, x + 1, y, more, useFord);
			unknown.add(squares[x + 1][y]);

		}
		if (y + 1 < height && !settled.contains(squares[x][y + 1])) {
			updateDistance(start, x, y + 1, more, useFord);
			unknown.add(squares[x][y + 1]);
		}

	}

	public void updateDistance(Nodes start, int x, int y, boolean more,
			boolean useFord) {

		if (more)
			CalculateH(x, y, useFord); // A*star

		if (start.distance + squares[x][y].cost + squares[x][y].heuristic < squares[x][y].distance
				+ squares[x][y].heuristic) {
			squares[x][y].distance = start.distance + squares[x][y].cost;
			squares[x][y].setPrev(start);
		}

	}

	public void CalculateH(int x, int y, boolean useFord) {

		long i = -100;
		if (useFord)
			i = ford.returnDist(x, y);
		else {
			i = Math.abs(x - goalx) + Math.abs(y - goaly);
		}
		squares[x][y].heuristic = i;
		return;
	}

	private class UpdateButtons implements Runnable {

		int xpoint;
		int ypoint;

		public UpdateButtons(int x, int y) {
			xpoint = x;
			ypoint = y;
		}

		@Override
		public void run() {
			squares[xpoint][ypoint].setForeground(Color.BLUE);
			squares[xpoint][ypoint].setBackground(Color.GREEN);
			squares[xpoint][ypoint].setText(" "
					+ squares[xpoint][ypoint].distance);
			squares[xpoint][ypoint].setOpaque(true);
		}
	}

	public static void DisplayMessage(String s) {
		JOptionPane.showMessageDialog(null, s, "Path", JOptionPane.OK_OPTION);
		System.out.println(s);
	}

	public static String UserInput(String infoMessage, String title) {

		String s = JOptionPane.showInputDialog(infoMessage, null);
		return s;
	}

	public boolean questions(String question) {
		return (JOptionPane.showConfirmDialog(null, question, "Request",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);

	}
}
