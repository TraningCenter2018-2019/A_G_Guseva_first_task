package crossword.configuration;

import java.util.ArrayList;
import java.util.HashMap;

public class Words {
  String[] words;
  HashMap<Character, HashMap<Integer, ArrayList<Integer>>> BigMap;
  private int numOfWord;
  public int sizeOfField;


  public Words(int n) {
    sizeOfField = n;
    numOfWord = 0;
    BigMap = new HashMap<Character, HashMap<Integer, ArrayList<Integer>>>();
  }


  public void createList(ArrayList<String> listOfWords) {
    int n = 0;
    this.words = new String[listOfWords.size()];
    for (String w : listOfWords) {
      addWord(w);
    }
  }

  private void addWord(String word) {
    this.words[numOfWord] = word;

    for (int i = 0; i < word.length(); i++) {
      char f = word.charAt(i);
      if (BigMap.get(word.charAt(i)) == null) {
        HashMap<Integer, ArrayList<Integer>> letter = new HashMap<Integer, ArrayList<Integer>>();
        ArrayList<Integer> letterWord = new ArrayList<Integer>();
        letterWord.add(numOfWord);
        letter.put(i, letterWord);
        BigMap.put(word.charAt(i), letter);
      } else {
        if (BigMap.get(word.charAt(i)).get(i) == null) {
          ArrayList<Integer> letterWord = new ArrayList<Integer>();
          letterWord.add(numOfWord);
          BigMap.get(word.charAt(i)).put(i, letterWord);
        } else
          BigMap.get(word.charAt(i)).get(i).add(numOfWord);
      }
    }
    numOfWord++;
  }

  private ArrayList<Integer> getWordsForMultipleConstrain(Scheme scheme){
    ArrayList<Integer> specialWords1 = BigMap.get(scheme.constrains.get(0).letter).get(scheme.constrains.get(0).pos);
    ArrayList<Integer> specialWords2 = new ArrayList<Integer>();
    if ((specialWords1 != null)&&(specialWords1.size()!=0)) {
      for (Constrains cs : scheme.constrains) {
        specialWords2 = BigMap.get(cs.letter).get(cs.pos);
        if (specialWords2 != null)
          specialWords1.retainAll(specialWords2);
        else {
          specialWords1 = null;
          break;
        }
      }
    }
    return specialWords1;
  }

  public ArrayList<Integer> getFittedWords(Scheme scheme, ArrayList<Integer> freeWords) {
    ArrayList<Integer> specialWords1 = new ArrayList<Integer>();
    if (!scheme.constrains.isEmpty()) {
      if (scheme.constrains.size() != 1) {
        specialWords1 = getWordsForMultipleConstrain(scheme);
      }
      else
        specialWords1 = BigMap.get(scheme.constrains.get(0).letter).get(scheme.constrains.get(0).pos);
    } else {
      for (int i = 0; i < words.length; i++)
        specialWords1.add(i);
    }

    ArrayList<Integer> specialWords = new ArrayList<Integer>();
    if (specialWords1 != null) {
      for (int var = 0; var < specialWords1.size(); var++) {
        if ((freeWords.contains(specialWords1.get(var))) && (words[specialWords1.get(var)].length() <= scheme.maxSize)) {
          specialWords.add(specialWords1.get(var));
        }
      }
    }

    return specialWords;
  }
}
