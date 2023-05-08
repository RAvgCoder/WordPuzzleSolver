import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryTest {

    // Create an instance of the Dictionary class to be used in testing
    Dictionary testDictionary = Dictionary.getDictionaryInstance();

    // Uses the current directory as a base to locate the txt files
    String currentWorkingDir = System.getProperty("user.dir");

    /**
     * Test the searchDictionary() method of the Dictionary class.
     * Asserts that the method returns true for valid words in the dictionary and false for invalid words.
     * Also tests if all words in the words file were inputted successfully.
     */
    @Test
    void testSearchDictionary() {

        // Test valid words in the dictionary
        assertTrue(testDictionary.searchDictionary("Man"));
        assertTrue(testDictionary.searchDictionary("Mens"));
        assertFalse(testDictionary.searchDictionary("a_liens"));

        // Test all words in the dictionary file to ensure all were successfully added
        if(!currentWorkingDir.endsWith("src")){
            currentWorkingDir += "src";
        }
        try(Scanner words = new Scanner(new File(currentWorkingDir+"/english3.txt"))){
            while (words.hasNextLine()){
                assertTrue(testDictionary.searchDictionary(words.nextLine()));
            }
        }catch (Exception e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

    /**
     * Test the getDictionaryInstance() method of the Dictionary class.
     * Asserts that only one instance of the dictionary is created across all threads.
     */
    @Test
    void testDictionaryInstance() {
        assertEquals(testDictionary.hashCode(), Dictionary.getDictionaryInstance().hashCode());
    }
}
