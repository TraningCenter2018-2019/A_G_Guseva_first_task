package crossword.configuration;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchemeTest {
  Scheme scheme;

  @Before
  public void setUp(){
    scheme=new Scheme();
  }

  @Test
  public void findPlace() {
    Matrix m=new Matrix(5);
    scheme.findPlace(m,0,1,0);
    assertEquals(5,scheme.maxSize);
    assertEquals(0,scheme.pos);
    assertEquals(0,scheme.constrains.size());

    m.matrixOfFreeCells[0][0]=false;
    m.matrixOfWords[0][0].letter='h';
    scheme.findPlace(m,0,0,0);
    assertEquals(1,scheme.constrains.size());
  }
}