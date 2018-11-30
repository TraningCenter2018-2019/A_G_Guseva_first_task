package Modification;

import Configuration.Matrix;
import UserInteraction.UserMenu;

import java.util.ArrayList;

public class PrintVSlogic {
  public boolean[][] field;
  public String[][] letters;
  private char[][] forCheck;
  public ArrayList<String> listOfWords;
  public ArrayList<String> listOfDescriptions;
  private ArrayList<Integer> vertDescr;
  private ArrayList<Integer> horDescr;
  public int amount;
  public int size;

  public PrintVSlogic() {
    //this.field = new boolean[][]{{true,true,false,true},{true,false,false,true},{false,true,false,true},{false,false,false,true}};
  }

  ArrayList<Integer> addNew(ArrayList<Integer> l, int num) {
    if (l.contains(num))
      return l;
    l.add(num);
    return l;
  }

  private void setCell(Matrix m,int i,int j){
    if (m.matrixOfFreeCells[i][j])
      letters[i][j] = "";
    else {
      forCheck[i][j] = m.matrixOfWords[i][j].letter;
      if ((m.matrixOfWords[i][j].hor >= 0) && (m.matrixOfWords[i][j].vert >= 0)) {
        letters[i][j] = "x";
        vertDescr = addNew(vertDescr, m.matrixOfWords[i][j].vert);
        horDescr = addNew(horDescr, m.matrixOfWords[i][j].hor);
      } else if (m.matrixOfWords[i][j].hor >= 0) {
        letters[i][j] = m.matrixOfWords[i][j].hor + "";
        horDescr = addNew(horDescr, m.matrixOfWords[i][j].hor);
      } else {
        letters[i][j] = m.matrixOfWords[i][j].vert + "";
        vertDescr = addNew(vertDescr, m.matrixOfWords[i][j].vert);
      }
    }
  }

  public void setFieldForPrint(Matrix m) {
    vertDescr = new ArrayList<Integer>();
    horDescr = new ArrayList<Integer>();

    field = new boolean[m.size][m.size];
    for (int i = 0; i < m.size; i++)
      for (int j = 0; j < m.size; j++)
        field[i][j] = !m.matrixOfFreeCells[i][j];

    forCheck = new char[size][size];
    letters = new String[m.size][m.size];
    for (int i = 0; i < m.size; i++)
      for (int j = 0; j < m.size; j++)
        setCell(m,i,j);
  }

  public void makeDescription(ArrayList<String> d) {
    listOfDescriptions = new ArrayList<String>();
    if (!horDescr.isEmpty()) {
      listOfDescriptions.add("ГОРИЗОНТАЛЬНО:");
      for (int w : horDescr) {
        listOfDescriptions.add(w + ". " + d.get(w));
      }
      listOfDescriptions.add(" ");
    }
    if (!vertDescr.isEmpty()) {
      listOfDescriptions.add("ВЕРТИКАЛЬНО:");
      for (int w : vertDescr) {
        listOfDescriptions.add(w + ". " + d.get(w));
      }
    }
  }

  public boolean CheckCrossword(char[][] crosswordForCheck) {
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        if (field[i][j])
          if (crosswordForCheck[i][j] != forCheck[i][j])
            return false;
    return true;
  }

  public void getWordsForConfig(UserMenu mode) {
    listOfWords = new ArrayList<String>();
    for (String s : mode.cr.listOfWords) {
      listOfWords.add(s);
    }
    amount = listOfWords.size();
    size = mode.cr.size;
  }

}
