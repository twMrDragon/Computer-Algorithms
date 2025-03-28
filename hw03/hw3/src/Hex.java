/* The Hex game
   https://en.wikipedia.org/wiki/Hex_(board_game)
   desigened by Jean-Christophe Filliâtre

   grid size : n*n

   playable cells : (i,j) with 1 <= i, j <= n

   blue edges (left and right) : i=0 or i=n+1, 1 <= j <= n
    red edges (top and bottom) : 1 <= i <= n, j=0 or j=n+1

      note: the four corners have no color

   adjacence :      i,j-1   i+1,j-1

                 i-1,j    i,j   i+1,j

                    i-1,j+1    i,j+1

*/

public class Hex {

  Player currentPlayer;
  int n;
  Player[][] grid;
  int labels[][];

  enum Player {
    NOONE, BLUE, RED
  }

  // create an empty board of size n*n
  Hex(int n) {
    this.n = n;
    currentPlayer = Player.BLUE;
    // init grid
    grid = new Player[n + 2][n + 2];
    for (int i = 0; i < n + 2; i++) {
      for (int j = 0; j < n + 2; j++) {
        grid[i][j] = Player.NOONE;
      }
    }
    for (int i = 1; i < n + 1; i++) {
      grid[i][0] = Player.RED;
      grid[i][n + 1] = Player.RED;
    }
    for (int j = 1; j < n + 1; j++) {
      grid[0][j] = Player.BLUE;
      grid[n + 1][j] = Player.BLUE;
    }
    // init link
    labels = new int[n + 2][n + 2];
    for (int i = 0; i < n + 2; i++) {
      for (int j = 0; j < n + 2; j++) {
        labels[i][j] = i + j * (n + 2); // link[i][j] = i*n + j
      }
    }
  }

  // return the color of cell i,j
  Player get(int i, int j) {
    return grid[i][j];
  }

  // update the board after the player with the trait plays the cell (i, j).
  // Does nothing if the move is illegal.
  // Returns true if and only if the move is legal.
  boolean click(int i, int j) {
    if (winner() != Player.NOONE)
      return false; // game is over
    if (i < 1 || i > grid.length - 2 || j < 1 || j > grid.length - 2)
      return false; // out of bounds
    if (grid[i][j] != Player.NOONE)
      return false; // already occupied
    grid[i][j] = currentPlayer; // update the board
    currentPlayer = (currentPlayer == Player.BLUE) ? Player.RED : Player.BLUE; // switch player
    unionFind();
    return true;
  }

  // return the player with the trait or Player.NOONE if the game is over
  // because of a player's victory.
  Player currentPlayer() {
    if (winner() != Player.NOONE)          
      return Player.NOONE; // game is over
    return currentPlayer;
  }

  // return the winning player, or Player.NOONE if no player has won yet
  Player winner() {
    // unionFind();
    if (find(0, 1) == find(n + 1, 1)) {
      return Player.BLUE;
    }
    if (find(1, 0) == find(1, n + 1)) {
      return Player.RED;
    }
    return Player.NOONE;
  }

  void unionFind() {
    for (int j = 0; j < grid.length; j++) {
      for (int i = 0; i < grid.length; i++) {
        if (grid[i][j] == Player.NOONE)
          continue;
        if (i + 1 < grid.length && grid[i][j] == grid[i + 1][j]) {
          union(i + 1, j, i, j);
        }
        if (j + 1 < grid.length && grid[i][j] == grid[i][j + 1]) {
          union(i, j + 1, i, j);
        }
        if (i - 1 >= 0 && j + 1 < grid.length && grid[i][j] == grid[i - 1][j + 1]) {
          union(i - 1, j + 1, i, j);
        }
      }
    }
  }

  int label(int i, int j) {
    return this.find(i, j);
  }

  int find(int i, int j) {
    int rij = this.labels[i][j];
    if (rij == i + j * (n + 2))
      return rij;
    int ri = rij % (n + 2);
    int rj = rij / (n + 2);
    return this.find(ri, rj);
  }

  void union(int i, int j, int k, int l) {
    int rij = this.find(i, j);
    int rkl = this.find(k, l);
    int ri = rij % (n + 2);
    int rj = rij / (n + 2);
    this.labels[ri][rj] = rkl;
  }

  public static void main(String[] args) {
    HexGUI.createAndShowGUI();
  }
}