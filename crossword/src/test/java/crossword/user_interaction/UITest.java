package crossword.user_interaction;

import com.googlecode.lanterna.terminal.Terminal;
import crossword.modification.PrintVSlogic;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UITest {

  UserMenu u= spy(new UserMenu());
  private PrintVSlogic printCr;

  public UITest() throws IOException {
  }

  @Before
  public void runT() throws IOException {
    printCr = null;
  }

  @Test
  public void UserMenuTest() throws IOException {
    PrintVSlogic printCr = null;
    u.chooseMode(printCr);
    verify(u).printOptions(0);
    if(u.option==1) {
      u.stop = true;
      verify(u,times(1)).crosswordConfig();
    }
    else {
      u.chooseMode(printCr);
      verify(u).printOptions(1);
    }
  }


  @Test
  public void playGame() {
  }

  @Test
  public void printHelp() throws IOException {
  }

  @Test
  public void printResult() {
  }

}