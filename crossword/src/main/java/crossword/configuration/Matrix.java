package crossword.configuration;

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

  private void addSpecialWordToMatrix(int col,int str,int colOrStr,String word,int w){
    if (colOrStr == 1) {
      for (int i = col; i < word.length()+col; i++) {
        matrixOfFreeCells[i][str] = false;//по столбцу добавление
        matrixOfWords[i][str].init(w, colOrStr, word.charAt(i-col));

      }

    } else {
      for (int i = col; i < word.length()+col; i++) {
        matrixOfFreeCells[str][i] = false;
        matrixOfWords[str][i].init(w, colOrStr, word.charAt(i-col));
      }
    }

  }

  private Scheme findScheme(int i, ArrayList<Integer> freeWords,Words words,int colOrStr){
    Scheme scheme = new Scheme();
    ArrayList<Integer> possibleWords;
    scheme.findPlace(this, i, colOrStr, 0);//0-str, 1- column
    possibleWords = words.getFittedWords(scheme, freeWords);
    int pop = 1;
    while ((possibleWords==null||(possibleWords.size()==0)) && (scheme.constrains.size() >= 1)&&(pop<size))//сдвигаем схему вправо
    {
      scheme.findPlace(this, i, colOrStr, pop);//0-str, 1- column
      possibleWords = words.getFittedWords(scheme, freeWords);
      pop++;
    }
    return scheme;
  }

  private boolean addHorizontalWord(Words words, ArrayList<Integer> freeWords, long startTime, int numOfStr){
    boolean stop=false;
    Scheme scheme;
    ArrayList<Integer> freeWordsOut;
    boolean found=false;
    for (int j = numOfStr + 1; j < size && !freeWords.isEmpty() && !stop; j++) {
      scheme=findScheme(j,freeWords,words,0);
      ArrayList<Integer> possibleWords = words.getFittedWords(scheme, freeWords);

      if ((scheme.constrains.size() >= 1)&&(possibleWords!=null)&&(possibleWords.size()!=0)) {
        Matrix matrOfVariants = new Matrix(size);
        Matrix bestVariant = new Matrix(size);
        bestVariant.copyMatrix(this);
        int maxAmount = this.amountOfWords;
        for (int var = 0; var < possibleWords.size() && !freeWords.isEmpty() && !found; var++) {
          stop = true;
          matrOfVariants.copyMatrix(this);
          freeWordsOut = new ArrayList<Integer>(freeWords);
          found = matrOfVariants.addWord(words, possibleWords.get(var), j, scheme.pos, 0, freeWordsOut, startTime, j);
          if (matrOfVariants.amountOfWords > maxAmount) {
            maxAmount = matrOfVariants.amountOfWords;
            bestVariant.copyMatrix(matrOfVariants);
          }
        }
        this.copyMatrix(bestVariant);
        if (found) return true;
      }
    }
    return found;
  }

  public boolean addWord(Words words, int w, int str, int col, int colOrStr, ArrayList<Integer> freeWords, long startTime, int numOfStr) {
    String word = words.words[w];
    addSpecialWordToMatrix(col,str,colOrStr,word,w);
    if(colOrStr==1) {
      int tmp = col;
      col = str;
      str = tmp;
    }
    amountOfWords++;
    Integer r = w;
    freeWords.remove(r);

    if ((str == size - 1) && (colOrStr == 0))
      return false;
    if ((amountOfWords == words.words.length) && ((System.currentTimeMillis() - startTime) < 300000))
      return true;

    if(freeWords.isEmpty())
      return false;
    boolean found = false;
    ArrayList<Integer> freeWordsOut;
    int i;
    Scheme scheme;
    if (colOrStr == 1)
      i = col + 1;//следующее
    else
      i = col;//первое
    //идём по слову
    boolean stop = false;
    while (i < size && !this.matrixOfFreeCells[numOfStr][i] && !freeWords.isEmpty() && !stop) {
      scheme=findScheme(i,freeWords,words,1);
      ArrayList<Integer> possibleWords = words.getFittedWords(scheme, freeWords);

      if ((scheme.constrains.size() >= 1)&&(possibleWords!=null)&&(possibleWords.size()!=0))
      {
        Matrix matrOfVariants = new Matrix(size);
        Matrix bestVariant = new Matrix(size);
        bestVariant.copyMatrix(this);

        int maxAmount = this.amountOfWords;
        for (int var = 0; var < possibleWords.size() && !found; var++) {
          stop = true;
          matrOfVariants.copyMatrix(this);
          freeWordsOut = new ArrayList<Integer>(freeWords);
          found = matrOfVariants.addWord(words, possibleWords.get(var), i, scheme.pos, 1, freeWordsOut, startTime, numOfStr);
          if (matrOfVariants.amountOfWords > maxAmount) {
            maxAmount = matrOfVariants.amountOfWords;
            bestVariant.copyMatrix(matrOfVariants);
          }
        }
        this.copyMatrix(bestVariant);
        if (found)
          return found;
      }
      i++;
    }

    //add horizontal
    if(!stop)
      found = addHorizontalWord(words,freeWords, startTime, numOfStr );
    return found;
  }
}

