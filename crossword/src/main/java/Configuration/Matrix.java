package Configuration;

import java.util.ArrayList;

public class Matrix {
  public Cell[][] matrixOfWords;
  public Boolean[][] matrixOfFreeCells;
  public int size;
  int amountOfWords;

  Matrix(int n) {
    size = n;
    matrixOfWords = new Cell[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        matrixOfWords[i][j] = new Cell();
    matrixOfFreeCells = new Boolean[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        matrixOfFreeCells[i][j] = true;
  }

  public void copyMatrix(Matrix b) {
    this.amountOfWords = b.amountOfWords;
    this.size = b.size;
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++) {
        this.matrixOfFreeCells[i][j] = b.matrixOfFreeCells[i][j];
        this.matrixOfWords[i][j].copyCell(b.matrixOfWords[i][j]);
      }
  }

  public boolean addWord(Words words, int w, int str, int col, int colOrStr, ArrayList<Integer> freeWords, long startTime, int numOfStr) {
    String word = words.words[w];
    if (colOrStr == 1) {

      for (int i = col; i < word.length(); i++) {
        matrixOfFreeCells[i][str] = false;//по столбцу добавление
        matrixOfWords[i][str].init(w, colOrStr, word.charAt(i));

      }
      int tmp = col;
      col = str;
      str = tmp;
    } else {
      for (int i = col; i < word.length(); i++) {
        matrixOfFreeCells[str][i] = false;
        matrixOfWords[str][i].init(w, colOrStr, word.charAt(i));
      }
    }

    amountOfWords++;
    Integer r = w;
    freeWords.remove(r);

    if ((str == size - 1) && (colOrStr == 0))
      return false;
    if ((amountOfWords == words.words.length) && (System.currentTimeMillis() - startTime < 30000))
      return true;

    boolean found = false;
    if (!freeWords.isEmpty()) {
      ArrayList<Integer> freeWordsOut;
      int i;
      Scheme scheme;
      if (colOrStr == 1)
        i = col + 1;//следующее
      else
        i = col;//первое
      //идём по слову
      boolean stop = false;
      if (i < size) {
        while (i < size && !this.matrixOfFreeCells[numOfStr][i] && !freeWords.isEmpty() && !stop) {
          scheme = new Scheme();
          scheme.findPlace(this, i, 1, 0);//0-str, 1- column
          found = false;
          ArrayList<Integer> possibleWords = words.getFittedWords(scheme, freeWords);
          int pop = 1;
          while (possibleWords == null && scheme.constrains.size() > 1)//сдвигаем схему вправо
          {
            scheme = new Scheme();
            scheme.findPlace(this, i, 1, pop);//0-str, 1- column
            pop++;
          }

          if (possibleWords != null) {
            Matrix matrOfVariants = new Matrix(size);
            Matrix bestVariant = new Matrix(size);
            bestVariant.copyMatrix(this);

            int maxAmount = this.amountOfWords;
            for (int var = 0; var < possibleWords.size() && !found; var++) {
              if (freeWords.contains(possibleWords.get(var)))//возможно убрать
              {
                stop = true;
                matrOfVariants.copyMatrix(this);
                freeWordsOut = new ArrayList<Integer>(freeWords);
                found = matrOfVariants.addWord(words, possibleWords.get(var), i, scheme.pos, 1, freeWordsOut, startTime, numOfStr);
                if (matrOfVariants.amountOfWords > maxAmount) {
                  maxAmount = matrOfVariants.amountOfWords;
                  bestVariant.copyMatrix(matrOfVariants);
                }
              }

            }
            this.copyMatrix(bestVariant);
            if (found)
              return found;
          }
          i++;
        }
      }

      //доавляем горизонтальные
      stop = false;
      for (int j = numOfStr + 1; j < size && !freeWords.isEmpty() && !stop && !found; j++) {
        scheme = new Scheme();
        scheme.findPlace(this, j, 0, 0);//0-str, 1- column
        ArrayList<Integer> possibleWords = words.getFittedWords(scheme, freeWords);
        int pop = 1;
        while (possibleWords == null && scheme.constrains.size() > 1)//сдвигаем схему вправо
        {
          scheme = new Scheme();
          scheme.findPlace(this, i, 1, pop);//0-str, 1- column
          pop++;
        }
        if (possibleWords != null) {
          Matrix matrOfVariants = new Matrix(size);
          Matrix bestVariant = new Matrix(size);
          bestVariant.copyMatrix(this);
          int maxAmount = this.amountOfWords;
          for (int var = 0; var < possibleWords.size() && !freeWords.isEmpty() && !found; var++) {
            if (freeWords.contains(possibleWords.get(var))) {
              stop = true;
              matrOfVariants.copyMatrix(this);
              freeWordsOut = new ArrayList<Integer>(freeWords);
              found = matrOfVariants.addWord(words, possibleWords.get(var), j, scheme.pos, 0, freeWordsOut, startTime, j);
              if (matrOfVariants.amountOfWords > maxAmount) {

                maxAmount = matrOfVariants.amountOfWords;
                bestVariant.copyMatrix(matrOfVariants);
              }
            }
          }
          this.copyMatrix(bestVariant);
          if (found) return found;
        }

      }
    }
    return found;
  }
}

