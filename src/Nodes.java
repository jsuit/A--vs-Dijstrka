import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class Nodes extends JButton implements Comparable<Nodes> {
	int xp;
	int yp;
	Nodes prev;
	int cost;
	int distance = Integer.MAX_VALUE - 101;
	String label;
	long heuristic;

	public Nodes(int x, int y, String label) {
		super(label);
		this.xp = x;
		this.yp = y;
		this.label = label;
		heuristic = 0;
	}

	public void setCost(int i) {
		cost = i;
	}

	public void setPrev(Nodes node) {
		prev = node;
	}

	public int compareTo(Nodes node2) {
		String s = this.label;
		String s2 = node2.label;
		if (distance + heuristic < node2.distance + node2.heuristic)
			return -1;
		else if (distance + heuristic == node2.distance + node2.heuristic) {
			if (heuristic < node2.heuristic)
				return -1;
			else if (heuristic > node2.heuristic)
				return 1;
			return 0;
		} else
			return 1;

	}

}
