package crossword.configuration;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MatrixTest {
  Matrix m;

  @Before
  public void setUp(){
    m = new Matrix(5);
  }

  @Test
  public void copyMatrix() {
    Matrix b = new Matrix(5);
    b.matrixOfFreeCells[1][1]=false;
    b.matrixOfWords[1][1].letter='h';
    m.copyMatrix(b);
    assertEquals(false, m.matrixOfFreeCells[1][1]);
    assertEquals('h',m.matrixOfWords[1][1].letter);
  }

  @Test
  public void addWord() {
    Words word=new Words(5);
    ArrayList<String> listOfWords=new ArrayList<String>();
    listOfWords.add("кошка");
    listOfWords.add("окно");
    listOfWords.add("не");
    word.createList(listOfWords);
    long startTime = System.currentTimeMillis();
    ArrayList<Integer> freeWords=new ArrayList<Integer>();
    freeWords.add(0);
    freeWords.add(1);
    freeWords.add(2);
    boolean res=m.addWord(word,0,0,0,0,freeWords,startTime,0);
    assertEquals(true,res);
  }
}