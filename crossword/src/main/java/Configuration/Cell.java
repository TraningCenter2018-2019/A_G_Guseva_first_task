package Configuration;

public class Cell {
  public char letter;
  public int hor;
  public int vert;

  public Cell() {
    this.hor = -1;
    this.vert = -1;
  }

  public void init(int num, int colOrStr, char c) {
    if (colOrStr == 0) {
      this.hor = num;
      this.letter = c;
    } else {
      this.vert = num;
      this.letter = c;
    }

  }

  public void copyCell(Cell c) {
    letter = c.letter;
    hor = c.hor;
    vert = c.vert;
  }

  public boolean isBlack() {
    return false;
  }

}
