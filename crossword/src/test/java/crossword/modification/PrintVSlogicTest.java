package crossword.modification;

import crossword.configuration.Configuration;
import crossword.configuration.Matrix;
import crossword.configuration.Words;
import crossword.user_interaction.CrosswordFactory;
import crossword.user_interaction.UserMenu;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PrintVSlogicTest {
  PrintVSlogic pl;

  @Before
  public void setUp() throws Exception {
    pl=new PrintVSlogic();
  }

  @Test
  public void addNew() {
    ArrayList<Integer> exp = new ArrayList<Integer>();
    ArrayList<Integer> res = pl.addNew(new ArrayList<Integer>(),5);
    exp.add(5);
    assertEquals(exp, res);
    res = pl.addNew(res,5);
    assertEquals(1,res.size());
  }

  @Test
  public void setFieldForPrint() throws IOException {
    ArrayList<String> listOfWords = new ArrayList<String>();
    listOfWords.add("земля");
    listOfWords.add("енот");
    Words word = new Words(5);
    word.createList(listOfWords);
    Configuration c = new Configuration(5);
    Matrix b=c.findOptimalMatrix(word);
    UserMenu mode=new UserMenu();
    mode.cr=new CrosswordFactory();
    mode.cr.listOfWords=new ArrayList<String>();
    mode.cr.listOfWords.add("земля");
    mode.cr.listOfWords.add("енот");
    mode.cr.size=5;
    pl.getWordsForConfig(mode);
    pl.setFieldForPrint(b);
    ArrayList<String> d=new ArrayList<String>();
    d.add("живём здесь");
    d.add("животное");
    pl.makeDescription(d);
    assertEquals(5,pl.listOfDescriptions.size());
    assertEquals(listOfWords,pl.listOfWords);

    char[][] cr=new char[5][5];
    boolean res2 = pl.CheckCrossword(cr);
    assertEquals(false,res2);
  }


}