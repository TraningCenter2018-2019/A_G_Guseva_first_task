package UserInteraction;

import Modification.PrintVSlogic;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;

public class Game {

  boolean field[][];
  String crossword[][];
  ArrayList<String> descriptions;
  int size;
  char[][] crosswordForCheck;

  Game(PrintVSlogic p) {
    size = p.size;
    field = new boolean[size][size];
    crossword = new String[size][size];

    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++) {
        field[i][j] = p.field[i][j];
        crossword[i][j] = p.letters[i][j];
      }
    descriptions = p.listOfDescriptions;
  }

  TextGraphics printField(Screen screen, TextGraphics tg) {
    boolean posForCursos = false;
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++) {
        tg.putString(j, i, field[i][j] ? (crossword[i][j] == "x" ?
                String.valueOf(Symbols.SINGLE_LINE_CROSS) : crossword[i][j]) :
                String.valueOf(Symbols.SOLID_SQUARE));
        if (field[i][j] && !posForCursos) {
          screen.setCursorPosition(new TerminalPosition(j, i));
          posForCursos = true;
        }
      }
    return tg;
  }

  TextGraphics printDescription(TextGraphics tg) {
    int i = 0;
    for (int side = 6; side <= size && i < descriptions.size(); side++) {
      tg.putString(size + 2, side, descriptions.get(i));
      i++;
    }
    for (int str = size + 1; i < descriptions.size(); str++) {
      tg.putString(0, str, descriptions.get(i));
      i++;
    }
    return tg;
  }

  TerminalPosition moveCursor(Screen screen, int direction, int up) throws IOException {
    int col = screen.getCursorPosition().getColumn();
    int str = screen.getCursorPosition().getRow();
    boolean posForCursos = false;
    if (direction == 1) {
      for (int j = str + up; j < size && !posForCursos && j >= 0; j += up)
        if (field[j][col]) {
          posForCursos = true;
          str = j;
        }
    } else {
      for (int i = col + up; i < size && !posForCursos && i >= 0; i += up)
        if (field[str][i]) {
          posForCursos = true;
          col = i;
        }
    }
    return new TerminalPosition(col, str);
  }

  TextGraphics solveCrossword(Terminal terminal, TextGraphics tg, Screen screen) throws IOException {
    boolean keeprunning = true;
    StringBuilder sb = new StringBuilder();
    int col = screen.getCursorPosition().getColumn();
    int str = screen.getCursorPosition().getRow();
    crosswordForCheck = new char[size][size];
    while (keeprunning) {
      KeyStroke keyPressed = terminal.pollInput();
      if (keyPressed != null) {
        System.out.println(keyPressed);
        switch (keyPressed.getKeyType()) {
          case ArrowDown: {
            screen.setCursorPosition(moveCursor(screen, 1, 1));//1/0-col/str,1/-1-up/down
            col = screen.getCursorPosition().getColumn();
            str = screen.getCursorPosition().getRow();
            screen.refresh();
            break;
          }
          case ArrowUp: {
            screen.setCursorPosition(moveCursor(screen, 1, -1));//1/0-col/str,1/0-up/down
            col = screen.getCursorPosition().getColumn();
            str = screen.getCursorPosition().getRow();
            screen.refresh();
            break;
          }
          case ArrowLeft: {
            screen.setCursorPosition(moveCursor(screen, 0, -1));//1/0-col/str,1/0-up/down
            col = screen.getCursorPosition().getColumn();
            str = screen.getCursorPosition().getRow();
            screen.refresh();
            break;
          }
          case ArrowRight: {
            screen.setCursorPosition(moveCursor(screen, 0, 1));//1/0-col/str,1/0-up/down
            col = screen.getCursorPosition().getColumn();
            str = screen.getCursorPosition().getRow();
            screen.refresh();
            break;
          }
          case Enter: {
            keeprunning = false;
            screen.clear();
            screen.refresh();
            break;
          }
          case Backspace: {
            tg.putString(col, str, field[str][col] ? (crossword[str][col] == "x" ?
                    String.valueOf(Symbols.SINGLE_LINE_CROSS) : crossword[str][col]) :
                    String.valueOf(Symbols.SOLID_SQUARE));
            crosswordForCheck[str][col]=crossword[str][col].charAt(0);
            screen.refresh();
            break;
          }
          case Character: {
            try {
              tg.putString(col, str, keyPressed.getCharacter().toString());
              crosswordForCheck[str][col] = keyPressed.getCharacter().toString().charAt(0);
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

    return tg;
  }

  void start(Terminal terminal) throws IOException {
    Screen screen = new TerminalScreen(terminal);
    screen.startScreen();
    TextGraphics tg = screen.newTextGraphics();
    screen.startScreen();
    tg.setForegroundColor(TextColor.ANSI.YELLOW);
    tg.putString(size + 2, 0, "КРОССВОРД. Режим игры.");
    tg.setForegroundColor(TextColor.ANSI.DEFAULT);
    //screen.setCursorPosition(new TerminalPosition(1,1));
    tg = printField(screen, tg);
    tg = printDescription(tg);
    tg.setForegroundColor(TextColor.ANSI.MAGENTA);
    tg.putString(size + 2, 1, "Используйте стрелки для переджижения по клеткам");
    tg.putString(size + 2, 2, "Используйте Backspace чтобы стереть букву");
    tg.putString(size + 2, 3, "Нажмите Enter, когда захотите проверить");
    tg.putString(size + 2, 4, "Символ " + String.valueOf(Symbols.SINGLE_LINE_CROSS) +
            " означает пересечение слов");
    tg.setForegroundColor(TextColor.ANSI.DEFAULT);
    TerminalSize ts = screen.doResizeIfNecessary();
    screen.refresh();
    //terminal.addResizeListener(new SimpleTerminalResizeListener(new TerminalSize(100,100)));
    tg = solveCrossword(terminal, tg, screen);
  }
}
