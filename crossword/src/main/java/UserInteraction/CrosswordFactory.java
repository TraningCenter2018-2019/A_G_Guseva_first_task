package UserInteraction;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;

public class CrosswordFactory {

  public ArrayList<String> listOfWords;
  public ArrayList<String> listOfDescriptions;
  public int size;

  public CrosswordFactory() {
    listOfWords = new ArrayList<String>();
    listOfDescriptions = new ArrayList<String>();
    size = 0;
  }

  private boolean isContains(String w) {
    for (int i = 0; i < listOfWords.size(); i++)
      if (listOfWords.get(i).equals(w))
        return true;
    return false;
  }

  boolean addWord(String s) {
    if (s.length() > size || s.length() == 0)
      return false;
    if (isContains(s))
      return false;
    listOfWords.add(s);
    return true;
  }


  private void readSize(TextGraphics tg, Terminal terminal, Screen screen) throws IOException {
    tg.putString(0, 0, "Конфигурация");
    tg.putString(0, 2, "Введите размер и нажмите Enter");
    screen.setCursorPosition(new TerminalPosition(0, 3));
    screen.refresh();

    boolean keeprunning = true;
    StringBuilder sb = new StringBuilder();
    while (keeprunning) {
      KeyStroke keyPressed = terminal.pollInput();
      if (keyPressed != null) {
        System.out.println(keyPressed);
        switch (keyPressed.getKeyType()) {
          case Enter: {
            if ((size == 0) || (size > 25)) {
              size = 0;
              sb = new StringBuilder();
              screen.clear();
              tg.putString(0, 0, "Конфигурация");
              tg.putString(0, 2, "Такой размер не доступен. Введите другой размер и нажмите Enter");
              screen.setCursorPosition(new TerminalPosition(0, 3));
              screen.refresh();
              break;
            }
            keeprunning = false;
            screen.clear();
            screen.refresh();
            break;
          }
          case Backspace: {
            if (sb.length() != 0) {
              size = size / 10;
              sb.delete(sb.length() - 1, sb.length());
              tg.putString(0, 3, sb.toString() + " ", SGR.BOLD);
              screen.refresh();
            }
            break;
          }
          case Character: {
            try {
              Integer.parseInt(keyPressed.getCharacter().toString());
              size = size * 10 + Integer.parseInt(keyPressed.getCharacter().toString());
              sb.append(keyPressed.getCharacter());
              tg.putString(0, 3, sb.toString(), SGR.BOLD);
              screen.refresh();
              break;
            } catch (NumberFormatException e) {
              break;
            }
          }

          default:
            System.out.println("default-branch");

        }
      }
    }
  }

  private void printAfterDescription(TextGraphics tg, Screen screen, StringBuilder sb) throws IOException {
    listOfDescriptions.add(sb.toString());
    tg.putString(0, 0, "Конфигурация");
    tg.putString(0, 2, "Введите слово или нажмите enter чтобы закончить ввод");
    screen.setCursorPosition(new TerminalPosition(0, 3));
    screen.refresh();
  }

  private StringBuilder printAboutBadWord(Screen screen,TextGraphics tg,StringBuilder sb) throws IOException {
    screen.clear();
    tg.putString(0, 0, "Конфигурация");
    tg.putString(0, 2, "Это слово не подходит.Введите другое слово");
    screen.refresh();
    return sb;
  }

  private StringBuilder getCharacter(boolean keepRunningForWord,boolean keepRunningForDescription,StringBuilder sb,TextGraphics tg, KeyStroke keyPressed){
    if (!keepRunningForWord && !keepRunningForDescription) {
      sb.append(keyPressed.getCharacter());
      tg.putString(0, 3, sb.toString(), SGR.BOLD);
    }
    //description then
    else if (!keepRunningForWord) {
      sb.append(keyPressed.getCharacter());
      tg.putString(0, 5, sb.toString(), SGR.BOLD);
    }
    else {
      sb.append(keyPressed.getCharacter());
      tg.putString(0, 3, sb.toString(), SGR.BOLD);
    }
    return sb;
  }

  private StringBuilder functionsOfEnter(boolean keepRunningForWord,boolean keepRunningForDescription,Screen screen,StringBuilder sb, TextGraphics tg) throws IOException {
    if (!keepRunningForDescription) {
      screen.clear();
      screen.refresh();
    }
    else if (!keepRunningForWord) {
      screen.clear();
      printAfterDescription(tg,screen,sb);
      sb = new StringBuilder();
    }
    else if (addWord(sb.toString())) {
      sb = new StringBuilder();
      tg.putString(0, 4, "Введите описание");
    }
    else
      sb=printAboutBadWord(screen,tg,sb);
    return sb;
  }

  private void readWords(TextGraphics tg, Terminal terminal, Screen screen) throws IOException {
    boolean keeprunning = true;
    StringBuilder sb = new StringBuilder();

    tg.putString(0, 0, "Конфигурация");
    tg.putString(0, 2, "Введите слово");
    screen.setCursorPosition(new TerminalPosition(0, 3));
    screen.refresh();
    boolean keepRunningForWord = true;
    boolean keepRunningForDescription = true;
    while (keeprunning) {
      KeyStroke keyPressed = terminal.pollInput();
      if (keyPressed != null) {
        System.out.println(keyPressed);
        switch (keyPressed.getKeyType()) {
          case Enter: {
            sb=functionsOfEnter(keepRunningForWord, keepRunningForDescription, screen, sb, tg);
            if (!keepRunningForDescription) {
              keeprunning = false;
              break;
            }
            if (!keepRunningForWord) {
              keepRunningForDescription = false;
              break;
            }
            if (sb.length()==0) {
              screen.setCursorPosition(new TerminalPosition(0, 5));
              screen.refresh();
              keepRunningForWord = false;
            }
            else
              sb=new StringBuilder();
            break;
          }
          case Character: {
            sb=getCharacter(keepRunningForWord,keepRunningForDescription,sb,tg,keyPressed);
            if (!keepRunningForWord && !keepRunningForDescription) {
              keepRunningForWord = true;
              keepRunningForDescription = true;
            }
            screen.refresh();
            break;
          }
          case Backspace: {
            if (sb.length() == 0)
              break;
            sb.delete(sb.length() - 1, sb.length());
            if (!keepRunningForWord) {
                tg.putString(0, 5, sb.toString() + " ", SGR.BOLD);
                screen.refresh();
                break;
              }
            tg.putString(0, 3, sb.toString() + " ", SGR.BOLD);
            screen.refresh();
            break;
          }

          default:
            System.out.println("default-branch");

        }
      }
    }
  }

  void start(Terminal terminal) throws IOException {
    Screen screen = new TerminalScreen(terminal);
    screen.startScreen();
    TextGraphics tg = screen.newTextGraphics();
    screen.startScreen();

    readSize(tg, terminal, screen);
    readWords(tg, terminal, screen);
  }
}

