import java.io.File;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * @author Egbor Osebhulimen
 * @date 2023-05-02
 * This dictionary is implemented using the  {<a href="https://en.wikipedia.org/wiki/Trie">trie data structure</a>}
 * Algorithm	Average	Worst case
 * Space	O(n)	O(n)
 * Search	O(n)	O(n)
 * Insert	O(n)	O(n)
 * Delete	O(n)	O(n)
 */
// TODO add delete functionality
public class Dictionary
{
    private final File dictionaryTxt; // File that stores the words if the dictionary in a txt file
    private final TrieNode root; // Top level of the dictionary

    //  Stores a single instance of the dictionary object
    private static final Dictionary DICTIONARY_INSTANCE = new Dictionary();

    /**
     * Represents a node in a trie data structure for storing a characters and its preceding character options.
     */
    static class TrieNode{

        // A mapping from characters to the next nodes in the trie
        LinkedHashMap<Character, TrieNode> charSuffixMap;

        // Whether this node represents a possible end of a string
        boolean isPossibleEnd;

        // The character represented by this node
        char currentChar;

        /**
         * Constructs a new trie node.
         */
        public TrieNode() {
            charSuffixMap = new LinkedHashMap<>();
            isPossibleEnd = false;
        }

        /**
         * Adds a new character to the trie below this node, creating new nodes as needed.
         * Returns the next node in the trie after adding the character.
         *
         * @param c the character to add
         * @param next the next node to link to this one for the given character
         * @return the next node in the trie after adding the character
         */
        public TrieNode add(char c, TrieNode next) {
            next.currentChar = c;
            charSuffixMap.putIfAbsent(c, next);
            return charSuffixMap.get(c);
        }

        /**
         * Returns the next node in the trie for the given character, if one exists.
         *
         * @param c the character to look for
         * @return an {@code Optional} containing the next node in the trie for the given character,
         *         or {@code Optional.empty()} if no such node exists
         */
        public TrieNode getNextCharNode(char c){return charSuffixMap.get(c);}

    }



    /**
     * Private constructor to prevent direct instantiation of the class from outside.
     * Initializes the dictionary by reading the words from a text file and adding them to the trie tree.
     */
    private Dictionary()
    {
        String currentDirectory = System.getProperty("user.dir"); // Uses this to get the base dir eg "~/.../WordPuzzleSolver"
        if (!currentDirectory.endsWith("src"))
            currentDirectory+="/src";
        String dictionaryPath = currentDirectory+"/english3.txt";
        dictionaryTxt = new File(dictionaryPath);
        root = new TrieNode();
        fillDictionary();
    }


    /**
     * Returns the single instance of the dictionary.
     * This method is thread-safe and ensures only one instance is created across all threads.
     * @return The single instance of the dictionary.
     */
    public static Dictionary getDictionaryInstance() {
        return DICTIONARY_INSTANCE;
    }

    /**
     * Fills map with the words in the dictionary
     */
    private void fillDictionary()
    {
        try(Scanner input = new Scanner(dictionaryTxt)){
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
        printDictionaryHelper(root,new StringBuilder());
    }

    /**
     * Used to check if a word is int the dictionary
     * @return true if word is found and false if not
     */
    public boolean searchDictionary(String word){
        return word.length() > 0 && searchDictionaryHelper(word.toLowerCase(), 0, root);
    }


    // -------------------------- Helper Functions --------------------------------------//

    /**
     * Helper function that searches for words in the dictionary
     *
     * @param word String you are looking for
     * @param currentSearchIndex Current index checking in its respective level
     * @param currLevel The level of the tree you currently are
     */
    private boolean searchDictionaryHelper(String word, int currentSearchIndex, TrieNode currLevel) {
        char currChar = word.charAt(currentSearchIndex);

        // Word not found
        TrieNode currCharNode = currLevel.getNextCharNode(currChar);
        if (currCharNode == null) return false;
        else {  // Word end found
            if (currentSearchIndex==word.length()-1) {
                return currCharNode.isPossibleEnd;
            }
            return searchDictionaryHelper(word,++currentSearchIndex,currCharNode);
        }
    }

    /**
     * Helper function that prints the dictionary
     *
     * @param currLevelList Map containing options for each level in the tree
     * @param wordToPrint Sequence of word that is being printed
     */
    private void printDictionaryHelper(TrieNode currLevelList, StringBuilder wordToPrint)
    {
        currLevelList.charSuffixMap.forEach((currChar, currNodePosition) -> {
            wordToPrint.append(currNodePosition.currentChar);

            if (currNodePosition.isPossibleEnd){
                System.out.println(wordToPrint);
            }
            printDictionaryHelper(currNodePosition, wordToPrint);
        });
        // If there are no more words remaining to print
        if (wordToPrint.length()==0)
            return;
        wordToPrint.replace(wordToPrint.length() - 1, wordToPrint.length(), "");
    }

}
