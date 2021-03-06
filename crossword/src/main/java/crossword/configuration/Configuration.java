package crossword.configuration;

import java.util.ArrayList;

public class Configuration {
  public int size;

  public Configuration(int size) {
    this.size = size;
  }


  private Matrix fillingMatrix(Words words) {
    Matrix m = new Matrix(size);
    m.amountOfWords = 0;
    long startTime = System.currentTimeMillis();
    boolean found = false;
    int amount = words.words.length;

    Scheme scheme;
    ArrayList<Integer> freeWords = new ArrayList<Integer>();
    ArrayList<Integer> freeWordsOut;
    for (int w = 0; w < amount; w++) {
      freeWords.add(w, w);
    }
    int i = 0;
    scheme = new Scheme();
    scheme.findPlace(m, i, 0, 0);//0-str, 1- column
    ArrayList<Integer> possibleWords = words.getFittedWords(scheme, freeWords);
    if (possibleWords != null) {
      Matrix matrOfVariants = new Matrix(size);
      Matrix bestVariant = new Matrix(size);
      bestVariant.copyMatrix(m);
      int maxAmount = m.amountOfWords;
      for (int var = 0; (var < words.words.length) &&(!found); var++) {
          matrOfVariants.copyMatrix(m);
          freeWordsOut = new ArrayList<Integer>(freeWords);
          found = matrOfVariants.addWord(words, possibleWords.get(var), 0, scheme.pos, 0, freeWordsOut, startTime, 0);
          if (matrOfVariants.amountOfWords > maxAmount) {
            maxAmount = matrOfVariants.amountOfWords;
            bestVariant.copyMatrix(matrOfVariants);
          }
        print(bestVariant);
      }
      m.copyMatrix(bestVariant);
    }
    return m;
  }

  private void printCell(Matrix m, int i,int j){
    if (m.matrixOfFreeCells[i][j] == true)
      System.out.print('*');
    else {
      if ((m.matrixOfWords[i][j].hor >= 0) && (m.matrixOfWords[i][j].vert >= 0))
        System.out.print('?');
      else if (m.matrixOfWords[i][j].hor >= 0)
        System.out.print(m.matrixOfWords[i][j].hor);
      else
        System.out.print(m.matrixOfWords[i][j].vert);
    }
  }

  private void printMatrixWithNums(Matrix m){
    for (int i = 0; i < m.size; i++) {
      for (int j = 0; j < m.size; j++)
        printCell(m,i,j);
      System.out.println();
    }
    System.out.println();
  }

  private void printMatrixWithLetters(Matrix m){
    for (int i = 0; i < m.size; i++) {
      for (int j = 0; j < m.size; j++)
        if (m.matrixOfFreeCells[i][j] == true)
          System.out.print('*');
        else {

          System.out.print(m.matrixOfWords[i][j].letter);
        }

      System.out.println();
    }
    System.out.println();
  }

  private void print(Matrix m) {
    printMatrixWithNums(m);
    printMatrixWithLetters(m);
    System.out.println("Количество слов: " + m.amountOfWords);
  }


  public Matrix findOptimalMatrix(Words words) {
    boolean found = false;
    Matrix m = new Matrix(size);
    m = fillingMatrix(words);
    return m;
  }
}
