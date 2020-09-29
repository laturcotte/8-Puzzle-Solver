import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class Board {

    private int[][] board;
    private int boardSize;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // .length on 2d array is only 1 side of it
        boardSize = tiles.length;
        board = new int[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(boardSize + "\n");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return boardSize;
    }

    // number of tiles out of place
    public int hamming() {
        // equation for what the tile is supposed to be using i and j:
        // tile = boardSize * i + j + 1
        int equation;
        int count = 0;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                // ignore last elem
                if (i == boardSize - 1 && j == boardSize - 1) {
                    break;
                }
                equation = boardSize * i + j + 1;
                if (board[i][j] != equation) {
                    count++;
                }
            }
        }

        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // need to find ideal row and col of elem and find difference to current
        // row both row and col distance to count, and if either are negative, multiply by -1
        // row of ideal placement: elem / boardSize if elem % boardSize != 0
        // else elem / boardSize - 1
        // col of ideal placement: col = boardSize - 1 if elem % boardSize == 0
        // (last elem of row), else col = elem % boardSize - 1
        int elem;
        int rowDistance;
        int colDistance;
        int count = 0;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                elem = board[i][j];
                // ignore if elem is 0 (empty)
                if (elem != 0) {
                    if (elem % boardSize == 0) {
                        rowDistance = elem / boardSize - 1 - i;
                        colDistance = boardSize - 1 - j;
                    } else {
                        rowDistance = elem / boardSize - i;
                        colDistance = elem % boardSize - 1 - j;
                    }


                    if (rowDistance < 0) {
                        rowDistance *= -1;
                    }

                    if (colDistance < 0) {
                        colDistance *= -1;
                    }

                    count += rowDistance;
                    count += colDistance;
                }
            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        // uses same equation as hamming distance
        // tile = boardSize * i + j + 1
        int equation;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                // ignore last elem
                if (i == boardSize - 1 && j == boardSize - 1) {
                    break;
                }
                equation = boardSize * i + j + 1;
                if (board[i][j] != equation) {
                    return false;
                }
            }
        }

        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (this == y) {
            return true;
        }
        if (!(y instanceof Board)) {
            return false;
        }

        Board that = (Board) y;

        return (Arrays.deepEquals(this.board, that.board));
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();

        // find position of 0, and swap it with all neighbouring values, adding
        // each variant to queue (also swap back)
        int rowIndex = 0;
        int colIndex = 0;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 0) {
                    rowIndex = i;
                    colIndex = j;
                    i += boardSize; // make sure exists 1st loop as well
                    break;
                }
            }
        }

        int[][] boardCopy = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }

        // check above
        if (rowIndex - 1 >= 0) {
            // swap elem above with 0
            // elem above has index [rowIndex - 1][colIndex]
            boardCopy[rowIndex][colIndex] = boardCopy[rowIndex - 1][colIndex];
            boardCopy[rowIndex - 1][colIndex] = 0;

            Board b = new Board(boardCopy);
            q.enqueue(b);

            // swap back
            boardCopy[rowIndex - 1][colIndex] = boardCopy[rowIndex][colIndex];
            boardCopy[rowIndex][colIndex] = 0;
        }

        // check below
        if (rowIndex + 1 < boardSize) {
            // swap elem below with 0
            // elem above has index [rowIndex + 1][colIndex]
            boardCopy[rowIndex][colIndex] = boardCopy[rowIndex + 1][colIndex];
            boardCopy[rowIndex + 1][colIndex] = 0;

            Board b = new Board(boardCopy);
            q.enqueue(b);

            // swap back
            boardCopy[rowIndex + 1][colIndex] = boardCopy[rowIndex][colIndex];
            boardCopy[rowIndex][colIndex] = 0;
        }

        // check to the left
        if (colIndex - 1 >= 0) {
            // swap elem to left with 0
            // elem to left has index [rowIndex][colIndex - 1]
            boardCopy[rowIndex][colIndex] = boardCopy[rowIndex][colIndex - 1];
            boardCopy[rowIndex][colIndex - 1] = 0;

            Board b = new Board(boardCopy);
            q.enqueue(b);

            // swap back
            boardCopy[rowIndex][colIndex - 1] = boardCopy[rowIndex][colIndex];
            boardCopy[rowIndex][colIndex] = 0;
        }

        // check to the right
        if (colIndex + 1 < boardSize) {
            // swap elem to left with 0
            // elem to left has index [rowIndex][colIndex - 1]
            boardCopy[rowIndex][colIndex] = boardCopy[rowIndex][colIndex + 1];
            boardCopy[rowIndex][colIndex + 1] = 0;

            Board b = new Board(boardCopy);
            q.enqueue(b);

            // swap back
            boardCopy[rowIndex][colIndex + 1] = boardCopy[rowIndex][colIndex];
            boardCopy[rowIndex][colIndex] = 0;
        }

        return q;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] boardCopy = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }

        // can make any swap
        // make swap at first elem where elem to right exists and is not 0
        // impossible for this to not work if boardSize >= 2
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (boardCopy[i][j] != 0) {
                    if (j + 1 < boardSize) {
                        if (boardCopy[i][j + 1] != 0) {
                            int temp = boardCopy[i][j];
                            boardCopy[i][j] = boardCopy[i][j + 1];
                            boardCopy[i][j + 1] = temp;

                            // exit this loop, make sure exit next loop
                            i += boardSize;
                            break;
                        }
                    }
                }
            }
        }

        Board b = new Board(boardCopy);
        return b;
    }

    // unit testing
    public static void main(String[] args) {
        int[][] a = new int[3][3];
        /*
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                a[i][j] = 3 * i + j + 1;
            }
        }
        a[2][2] = 0; */


        a[0][0] = 8;
        a[0][1] = 1;
        a[0][2] = 3;
        a[1][0] = 4;
        a[1][1] = 0;
        a[1][2] = 2;
        a[2][0] = 7;
        a[2][1] = 6;
        a[2][2] = 5;

        Board c = new Board(a);

        /*
        System.out.println("Dim: " + b.dimension());
        System.out.println("ham: " + b.hamming());
        System.out.println("man: " + b.manhattan());
        System.out.println("goal: " + b.isGoal()); */

        a[0][0] = 1;
        a[0][1] = 0;
        a[0][2] = 3;
        a[1][0] = 4;
        a[1][1] = 2;
        a[1][2] = 5;
        a[2][0] = 7;
        a[2][1] = 8;
        a[2][2] = 6;

        Board b = new Board(a);

        Board d = b.twin();

        System.out.println(b);
        System.out.println(d);

        /*
        for (Board k : b.neighbors()) {
            System.out.println(k);
        } */
    }

}
