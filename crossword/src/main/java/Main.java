import java.io.IOException;

import Configuration.Words;
import Configuration.Configuration;
import Modification.PrintVSlogic;
import UserInteraction.UserMenu;

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
