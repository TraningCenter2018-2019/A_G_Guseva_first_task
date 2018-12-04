package crossword.configuration;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

public class ConfigurationTest {
  Configuration c;

  @Before
  public void setUp(){
    c = new Configuration(5);
  }

  @Test
  public void findOptimalMatrix() {
    Words word=new Words(5);
    ArrayList<String> listOfWords=new ArrayList<String>();
    listOfWords.add("крот");
    listOfWords.add("рис");
    listOfWords.add("сота");
    word.createList(listOfWords);
    Matrix res=c.findOptimalMatrix(word);
    Matrix b = new Matrix(5);
    b.matrixOfWords[0][0].letter='к';
    b.matrixOfFreeCells[0][0]=false;
    b.matrixOfWords[0][1].letter='р';
    b.matrixOfFreeCells[0][1]=false;
    b.matrixOfWords[0][2].letter='о';
    b.matrixOfFreeCells[0][2]=false;
    b.matrixOfWords[0][3].letter='т';
    b.matrixOfFreeCells[0][3]=false;
    b.matrixOfWords[1][1].letter='и';
    b.matrixOfFreeCells[1][1]=false;
    b.matrixOfWords[2][1].letter='с';
    b.matrixOfFreeCells[2][1]=false;
    b.matrixOfWords[2][2].letter='о';
    b.matrixOfFreeCells[2][2]=false;
    b.matrixOfWords[2][3].letter='т';
    b.matrixOfFreeCells[2][3]=false;
    b.matrixOfWords[2][4].letter='а';
    b.matrixOfFreeCells[2][4]=false;

    for(int i=0;i<5;i++)
      for(int j=0;j<5;j++)
        if(!res.matrixOfFreeCells[i][j])
          assertEquals(b.matrixOfWords[i][j].letter,res.matrixOfWords[i][j].letter);
  }
}