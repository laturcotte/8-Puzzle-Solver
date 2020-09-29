import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Node smallest;
    private Node smallestTwin;

    private static class Node implements Comparable<Node> {
        private Board b;
        private int moves;
        private Node previous;
        private int manhattanVal;
        private int priority;

        public Node(Board tiles, int m, Node prev) {
            b = tiles;
            previous = prev;
            moves = m;
            manhattanVal = b.manhattan();
            priority = manhattanVal + moves;
        }

        public int compareTo(Node that) {
            if (that.priority > this.priority) {
                return -1;
            } else if (this.priority > that.priority) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("");

        // the board being checked has normal variable names, while its twin
        // has the Twin suffix to all of its variables. If the normal board is
        // solved first, then it is solvable; if the twin is solved first, then
        // it isn't.
        MinPQ<Node> min = new MinPQ<Node>();
        MinPQ<Node> minTwin = new MinPQ<Node>();

        Node n = new Node(initial, 0, null);
        min.insert(n);

        Board twinBoard = initial.twin();
        Node nTwin = new Node(twinBoard, 0, null);
        minTwin.insert(nTwin);

        smallest = min.delMin();
        smallestTwin = minTwin.delMin();

        while (!smallest.b.isGoal() && !smallestTwin.b.isGoal()) {

            for (Board k : smallest.b.neighbors()) {
                // only add board/node if not the same as the previous board
                // of the current search node, or if first set of neighbors
                if (smallest.previous == null) {
                    Node current = new Node(k, smallest.moves + 1, smallest);
                    min.insert(current);
                } else if (!k.equals(smallest.previous.b)) {
                    Node current = new Node(k, smallest.moves + 1, smallest);
                    min.insert(current);
                }
            }

            for (Board l : smallestTwin.b.neighbors()) {
                // only add board/node if not the same as the previous board
                // of the current search node, or if first set of neighbors
                if (smallestTwin.previous == null) {
                    Node current = new Node(l, smallestTwin.moves + 1, smallestTwin);
                    minTwin.insert(current);
                } else if (!l.equals(smallestTwin.previous.b)) {
                    Node current = new Node(l, smallestTwin.moves + 1, smallestTwin);
                    minTwin.insert(current);
                }
            }

            smallest = min.delMin();
            smallestTwin = minTwin.delMin();
        }

        // try to clear unsolvable boards from memory
        if (smallest.b.isGoal()) {
            while (smallestTwin != null) {
                smallestTwin = smallestTwin.previous;
            }
        } else {
            while (smallest != null) {
                smallest = smallest.previous;
            }
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        // if solvable,
        if (smallest != null) {
            return true;
        }

        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return smallest.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Stack<Board> q = new Stack<Board>();

        // once get to goal, trace back using previous, adding to queue
        Node current = smallest;

        while (current != null) {
            q.push(current.b);
            current = current.previous;
        }

        return q;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
