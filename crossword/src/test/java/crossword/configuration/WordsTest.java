package crossword.configuration;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class WordsTest {
  Words w;

  @Before
  public void setUp(){
    w = spy(new Words(5));
  }

  @Test
  public void createList() {
    ArrayList<String> listOfWords=new ArrayList<String>();
    listOfWords.add("кошка");
    listOfWords.add("окно");
    w.createList(listOfWords);
    assertEquals(w.words.length,2);
    verify(w).createList(listOfWords);
  }

  @Test
  public void getFittedWords() throws Exception {
    ArrayList<String> listOfWords=new ArrayList<String>();
    listOfWords.add("кошка");
    listOfWords.add("окно");
    w.createList(listOfWords);
    Scheme scheme=new Scheme();
    scheme.maxSize=5;
    ArrayList<Integer> freeWords = new ArrayList<Integer>();
    freeWords.add(0);
    freeWords.add(1);
    ArrayList<Integer> res=w.getFittedWords(scheme,freeWords);
    ArrayList<Integer> tr = new ArrayList<Integer>();
    tr.add(0);
    tr.add(1);
    assertEquals(res,tr);

    tr.remove(1);
    Constrains c=new Constrains(0,'к');
    scheme.constrains.add(c);
    c=new Constrains(2,'ш');
    scheme.constrains.add(c);
    ArrayList<Integer> trForMock = new ArrayList<Integer>();
    res=w.getFittedWords(scheme,freeWords);
    assertEquals(res,tr);

    freeWords.remove(0);
    tr.remove(0);
    res=w.getFittedWords(scheme,freeWords);
    assertEquals(res,tr);
  }
}