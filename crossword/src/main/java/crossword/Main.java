package crossword;

import java.io.IOException;

import crossword.configuration.Words;
import crossword.configuration.Configuration;
import crossword.modification.PrintVSlogic;
import crossword.user_interaction.UserMenu;

public class Main {
  public static void main(String[] args) throws IOException {
    UserMenu mode = new UserMenu();
    mode.chooseMode(null);
    PrintVSlogic printCr = null;
    while (!mode.stop) {
      if (mode.option == 1) {
        //creation
        printCr = new PrintVSlogic();
        printCr.getWordsForConfig(mode);
        Words listOfWords = new Words(printCr.size);
        listOfWords.createList(printCr.listOfWords);
        Configuration crossword = new Configuration(listOfWords.sizeOfField);
        printCr.setFieldForPrint(crossword.findOptimalMatrix(listOfWords));
      }

      mode.chooseMode(printCr);
    }

  }

}
