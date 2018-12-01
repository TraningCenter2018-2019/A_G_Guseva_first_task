package crossword.configuration;

import java.util.ArrayList;

public class Scheme {
  ArrayList<Constrains> constrains;
  int pos;
  int maxSize;

  public Scheme() {
    constrains = new ArrayList<Constrains>();
  }

  private void addConstrain(int pos, char letter) {
    Constrains c = new Constrains(pos, letter);
    constrains.add(c);

  }

  public void findPlace(Matrix m, int num, int colOrStr, int position) {
    pos = position;
    int maxSize1 = 0;
    if (colOrStr == 0)//по строке
    {
      for (int i = position; i < m.size; i++) {
        maxSize1++;
        if (!m.matrixOfFreeCells[num][i]) {
          addConstrain(i, m.matrixOfWords[num][i].letter);
        }
      }
    } else {
      for (int i = position; i < m.size; i++) {
        maxSize1++;
        if (!m.matrixOfFreeCells[i][num]) {
          addConstrain(i, m.matrixOfWords[i][num].letter);
        }
      }
    }
    maxSize = maxSize1;
  }
}

