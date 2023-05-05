import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Egbor Osebhulimen
 * This dictionary is implemented using the  {<a href="https://en.wikipedia.org/wiki/Trie">trie data structure</a>}
 * Algorithm	Average	Worst case
 * Space	O(n)	O(n)
 * Search	O(n)	O(n)
 * Insert	O(n)	O(n)
 * Delete	O(n)	O(n)
 */
public class Dictionary
{
    File dictionary;
    TrieNode root;

    static class TrieNode{
        LinkedHashMap<Character, TrieNode> suffix;
        boolean isPossibleEnd;
        char currChar;
        public TrieNode()
        {
            suffix = new LinkedHashMap<>();
            isPossibleEnd = false;
        }

        public TrieNode add(char c,TrieNode next) {
            next.currChar = c;
            suffix.putIfAbsent(c, next);
            return suffix.get(c);
        }
    }

    public Dictionary()
    {
        String dictionaryPath = "src/english3.txt";
//        String dictionaryPath = "C:\\Users\\egbor\\Videos\\WordPuzzleSolver\\src\\english3.txt";
        dictionary = new File(dictionaryPath);
        root = new TrieNode();
        fillDictionary();
    }

    /**
     * Fills map with the words in the dictionary
     */
    private void fillDictionary()
    {
        int i = 0;
        try(Scanner input = new Scanner(dictionary);){
            while(input.hasNextLine()){
                String currentWord = input.nextLine();
                if (currentWord.length() >= 1) {
                    addToDictionary(currentWord,0,root);
                }
            }
        }catch (Exception e){
            System.out.println("Dictionary txt not found");
            e.printStackTrace();
        }
    }

    /**
     * Adds each character of the letter into the tree recursively
     * @param currentWord The word you want added to the dictionary
     * @param index The indexOf the word to be added
     * @param currentLevel  Level of the tree to insert into
     */
    private void addToDictionary(String currentWord, int index, TrieNode currentLevel)
    {
        int currentIndex = currentWord.length() - index;
//        System.out.print(currentWord.charAt(currentIndex));
        // adds to the current level
        char currentChar = Character.toLowerCase(currentWord.charAt(index));
        TrieNode insertedVal = currentLevel.add(currentChar, new TrieNode());

        // If reached the end of the word end
        if (index == currentWord.length()-1){
            insertedVal.isPossibleEnd = true;
            return ;
        }

        addToDictionary(currentWord, ++index, insertedVal);
    }

    /**
     * Called by user to print the dictionary that was added
     */
    public void printDictionary()
    {
        printDictionaryHelper(root.suffix,new StringBuilder());
    }

    /**
     * Helper function that prints the dictionary
     *
     * @param currLevelList Map containing options for each level in the tree
     * @param wordToPrint Sequence of word that is being printed
     */
    private void printDictionaryHelper(LinkedHashMap<Character, TrieNode> currLevelList, StringBuilder wordToPrint)
    {
        for (Map.Entry<Character, TrieNode> currLevelOptions : currLevelList.entrySet()){
            TrieNode currPosition = currLevelOptions.getValue();
            wordToPrint.append(currPosition.currChar);

            if (currPosition.isPossibleEnd){
                System.out.println(wordToPrint);
            }

            printDictionaryHelper(currPosition.suffix, wordToPrint);
//            printDictionary();
        }
        // If no more words can be formed on that level of the tree
        if (wordToPrint.length()>0){
            wordToPrint.replace(wordToPrint.length() - 1, wordToPrint.length(), "");
        }
    }

}
