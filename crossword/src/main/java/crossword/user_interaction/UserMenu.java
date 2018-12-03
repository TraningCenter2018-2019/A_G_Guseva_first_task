package crossword.user_interaction;

import crossword.modification.PrintVSlogic;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class UserMenu {

  public CrosswordFactory cr;//null while not initialized
  public Game gm;
  Terminal terminal;
  public int option;
  public boolean stop;

  public UserMenu() throws IOException {
    this.terminal = new DefaultTerminalFactory().createTerminal();
    option = -1;
  }

  void crosswordConfig() {
    option = 1;
  }

  void playGame() {
    option = 2;
  }

  void printOptions(int first) throws IOException {
    final Screen screen = new TerminalScreen(terminal);
    screen.startScreen();
    MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);
    textGUI.setEOFWhenNoWindows(true);
    final BasicWindow window = new BasicWindow("Menu");
    Panel contentArea = new Panel();
    contentArea.setLayoutManager(new LinearLayout(Direction.VERTICAL));
    contentArea.addComponent(new Button("Сконфигурировать кроссворд", new Runnable() {
      public void run() {
        crosswordConfig();
        window.close();
      }
    }));
    switch (first) {
      case 1: {
        contentArea.addComponent(new Button("Играть", new Runnable() {
          public void run() {
            playGame();
            window.close();
          }
        }));

        contentArea.addComponent(new Button("Инструкция", new Runnable() {
          public void run() {
            try {
              printHelp();
            } catch (IOException e) {
              e.printStackTrace();
            }
            //window.close();
          }
        }));
        break;
      }
      case 0: {
        contentArea.addComponent(new EmptySpace(new TerminalSize(5, 1)));

        contentArea.addComponent(new Button("Инструкция", new Runnable() {
          public void run() {
            try {
              printHelp();
            } catch (IOException e) {
              e.printStackTrace();
            }
            //window.close();
          }
        }));
        break;
      }
      default: {
        break;
      }
    }

    contentArea.addComponent(new EmptySpace(new TerminalSize(5, 1)));
    contentArea.addComponent(new Button("Exit", new Runnable() {
      public void run() {
        option = -1;
        stop = true;
        try {
          screen.stopScreen();
        } catch (IOException e) {
          e.printStackTrace();
        }

      }
    }));
    window.setComponent(contentArea);
    textGUI.addWindowAndWait(window);
  }

  void printHelp() throws IOException {
    Terminal t = new DefaultTerminalFactory().createTerminal();
    final Screen screen = new TerminalScreen(t);
    screen.startScreen();
    MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);
    textGUI.setEOFWhenNoWindows(true);
    final BasicWindow window = new BasicWindow("Help");
    window.setPosition(new TerminalPosition(40, 10));
    Panel contentArea = new Panel();
    contentArea.setLayoutManager(new LinearLayout(Direction.VERTICAL));
    contentArea.addComponent(new Label(
            "При первичном запуске вы можете только сконфигурировть кроссворд'\n" +
                    "Для возврата в меню нажмите Exit"));
    contentArea.addComponent(new Button("Exit", new Runnable() {
      public void run() {
        try {
          screen.stopScreen();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }));
    window.setComponent(contentArea);
    textGUI.addWindowAndWait(window);
  }

  void printResult(boolean res) throws IOException {
    Terminal t = new DefaultTerminalFactory().createTerminal();
    final Screen screen = new TerminalScreen(t);
    screen.startScreen();
    MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);
    textGUI.setEOFWhenNoWindows(true);
    final BasicWindow window = new BasicWindow("Результат");
    window.setPosition(new TerminalPosition(40, 10));
    Panel contentArea = new Panel();
    contentArea.setLayoutManager(new LinearLayout(Direction.VERTICAL));
    contentArea.addComponent(new Label(res ? "Кроссворд решён верно! ПОЗДРАВЛЯЕМ!" :
            "Кроссворд решён неверно. Не расстраивайтесь, попробуйте ещё раз"));
    contentArea.addComponent(new Button("ОК", new Runnable() {
      public void run() {
        try {
          screen.stopScreen();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }));
    window.setComponent(contentArea);
    textGUI.addWindowAndWait(window);
  }


  public void chooseMode(PrintVSlogic printCr) throws IOException {
    if (cr == null)
      printOptions(0);
    else
      printOptions(1);
    if (option == 1) {
      cr = new CrosswordFactory();
      cr.start(terminal);
    }
    if (option == 2) {
      printCr.makeDescription(cr.listOfDescriptions);
      gm = new Game(printCr);
      gm.start(terminal);
      printResult(printCr.CheckCrossword(gm.crosswordForCheck));
    }
  }


}
