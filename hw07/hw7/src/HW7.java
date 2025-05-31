import java.util.*;

/**
 * the representation of the problem is as follows:
 * the grid has 6 columns, numbered 0 to 5 from left to right
 * and 6 rows, numbered 0 to 5 from top to bottom
 *
 * there are nbCars cars, numbered from 0 to nbCars-1
 * for each car i:
 * - color[i] gives its color
 * - horiz[i] indicates if it is a horizontal car
 * - len[i] gives its length (2 or 3)
 * - moveOn[i] indicates on which line it moves for a horizontal car
 * and on which column for a vertical car
 *
 * the car 0 is the one that must exit, so we have
 * horiz[0]==true, len[0]==2, moveOn[0]==2
 */
class RushHour {
	int nbCars;
	String[] color;
	boolean[] horiz;
	int[] len;
	int[] moveOn;

	public RushHour(int nbCars, String[] color, boolean[] horiz, int[] len, int[] moveOn) {
		this.nbCars = nbCars;
		this.color = color;
		this.horiz = horiz;
		this.len = len;
		this.moveOn = moveOn;
	}

	/** return the list of possible moves from s */
	LinkedList<State> moves(State s) {
		boolean [][] free = s.free();
		LinkedList<State> res = new LinkedList<>();
		for (int i = 0; i < nbCars; i++) {
			int p = s.pos[i];
			if (horiz[i]) {
				// Move left
				if (p > 0 && free[moveOn[i]][p - 1]) {
					res.add(new State(s, i, -1));
				}
				// Move right
				if (p + len[i] < 6 && free[moveOn[i]][p + len[i]]) {
					res.add(new State(s, i, 1));
				}
			} else {
				// Move up
				if (p > 0 && free[p - 1][moveOn[i]]) {
					res.add(new State(s, i, -1));
				}
				// Move down
				if (p + len[i] < 6 && free[p + len[i]][moveOn[i]]) {
					res.add(new State(s, i, 1));
				}
			}
		}
		return res;
	}

	State solveDFS(State s) {
		HashSet<State> visited = new HashSet<>();
		Stack<State> stack = new Stack<>();
		stack.push(s);
		while (!stack.isEmpty()) {
			State current = stack.pop();
			if (current.success()) {
				return current;
			}
			if (!visited.contains(current)) {
				visited.add(current);
				for (State next : moves(current)) {
					if (!visited.contains(next)) {
						stack.push(next);
					}
				}
			}
		}
		return null;
	}

	/** search for a solution from state s */
	State solveBFS(State s) {
		Queue<State> queue = new LinkedList<>();
		HashSet<State> visited = new HashSet<>();
		queue.add(s);
		while (!queue.isEmpty()) {
			State current = queue.poll();
			if (current.success()) {
				return current;
			}
			if (!visited.contains(current)) {
				visited.add(current);
				for (State next : moves(current)) {
					if (!visited.contains(next)) {
						queue.add(next);
					}
				}
			}
		}
		return null;
	}

	/** print the solution */
	void printSolution(State s) {
		if (s == null) {
			System.out.println("No solution found.");
			return;
		}

		LinkedList<State> path = new LinkedList<>();
		State curr = s;
		while (curr.prev != null) {
			path.addFirst(curr);
			curr = curr.prev;
		}

		System.out.println(path.size() + " trips");

		// Print each move
		for (State state : path) {
			String color = state.plateau.color[state.c];
			String direction;
			if (state.plateau.horiz[state.c]) {
				direction = state.d == 1 ? "to the right" : "to the left";
			} else {
				direction = state.d == 1 ? "down" : "upwards";
			}
			System.out.println("we move the " + color + " vehicle " + direction);
		}
	}

}

/**
 * given the position of each car, with the following convention:
 * for a horizontal car it is the column of its leftmost square
 * for a vertical car it is the column of its topmost square
 * (recall: the leftmost column is 0, the topmost row is 0)
 */
class State {
	RushHour plateau;
	int[] pos;

	/** we remember which move led to this state, for the display of the solution */
	State prev;
	int c;
	int d;

	/** construct an initial state (c, d and prev are not significant) */
	public State(RushHour plateau, int[] pos) {
		this.plateau = plateau;
		this.pos = pos;
	}

	/** construct a state obtained from s by moving car c by d (-1 or +1) */
	public State(State s, int c, int d) {
		this.plateau = s.plateau;
		this.pos = s.pos.clone();
		this.pos[c] += d; // move the car c by d
		this.prev = s;
		this.c = c;
		this.d = d;
	}

	/** winning ? */
	public boolean success() {
		return pos[0] == 4;
	}

	/** what are the free places */
	public boolean[][] free() {
		boolean [][] free = new boolean[6][6];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				free[i][j] = true;
			}
		}
		for (int i = 0; i < plateau.nbCars; i++) {
			int p = pos[i];
			if (plateau.horiz[i]) { // horizontal car
				for (int j = 0; j < plateau.len[i]; j++) {
					free[plateau.moveOn[i]][p + j] = false;
				}
			} else { // vertical car
				for (int j = 0; j < plateau.len[i]; j++) {
					free[p + j][plateau.moveOn[i]] = false;
				}
			}
		}
		return free;
	}

	/** test of equality of two states */
	public boolean equals(Object o) {
		State s = (State) o;
		if (s == null || s.plateau != plateau || s.pos.length != pos.length)
			return false;
		for (int i = 0; i < pos.length; i++) {
			if (s.pos[i] != pos[i])
				return false;
		}
		return true;
	}

	/** hash code of the state */
	public int hashCode() {
		int h = 0;
		for (int i = 0; i < pos.length; i++)
			h = 37 * h + pos[i];
		return h;
	}

}
