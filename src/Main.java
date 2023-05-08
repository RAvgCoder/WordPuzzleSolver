import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Main
{
    public static void main (String[] args)
    {
        Scanner input = new Scanner(System.in);

        long start = System.nanoTime();
        Dictionary dictionary = Dictionary.getDictionaryInstance();   // Creates a dictionary with valid words
        System.out.println("Dictionary Creation took: "+(System.nanoTime()-start)/1_000_000+"ms");

        String wordsToLookFor,digitsLookingFor = wordsToLookFor ="";

        // Makes sure the user doesn't just input no text at all
        boolean wordIsValid = false;
        while (wordsToLookFor.length()==0 || !wordIsValid){
            System.out.println("Input char you want to check for on a single line");
            wordsToLookFor = input.nextLine();
            wordIsValid = true;

            // Validate word if its only alphabetic
            if (!wordsToLookFor.matches("[a-zA-Z]+")) {
                wordIsValid = false;
                System.out.println("Input can only be alphabetic");
            }
        }



        //  Creates permutations for the given word
        String[] permutations = createPermutation(wordsToLookFor);

        // Makes sure the user doesn't just input no numbers at all
        while (digitsLookingFor.length() == 0){
            System.out.println("Input digit len you want to check for on a single line");
            digitsLookingFor=input.nextLine();
        }

        /*
        *   Collects input within a range from what's defined till the largest word length: -4 -> from length 4 till largest word length
        *   else they just input the number they want manually: 1 5 4 etc.
        */
        if (digitsLookingFor.charAt(0) == '-'){
            int ans = Integer.parseInt(digitsLookingFor.substring(1));
            digitsLookingFor = "";
            for (int i = ans; i <= permutations[permutations.length - 1].length(); i++)
                digitsLookingFor += i+" ";
        }

        int[] digitLookingFor = Arrays.stream(digitsLookingFor.trim().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();

        boolean printInDictionary = true;
        // Finds words that are valid
        for (String permute: permutations){
            if (dictionary.searchDictionary(permute) && lengthSearch(permute.length(),digitLookingFor)){
                if(printInDictionary) {
                    System.out.printf("These are the valid permutations for \"%s\" with lengths of %s :%n", wordsToLookFor, Arrays.toString(digitLookingFor));
                    printInDictionary = false;
                }
                System.out.println(permute);
            }
        }
        // If no valid permutations
        if (printInDictionary){
            System.out.printf("There are no valid permutations for \"%s\" with lengths of %s :%n", wordsToLookFor, Arrays.toString(digitLookingFor));
        }
    }

    /**
     * Searches from the length of desired words if the current word length matches it
     * @param wordLength Current word length
     * @param digitLookingFor List of word lengths available to chose from
     * @return  True if the word length matches any of the digits you are looking for
     */
    private static boolean lengthSearch(int wordLength, int[] digitLookingFor) {
        for (int lengths : digitLookingFor){
            if (wordLength == lengths) return true;
        }
        return false;
    }


    /**
     * Runs the python program which creates combinations of words
     * @param wordToLookFor Words to permute
     * @return Words created by permutation
     */
    private static String[] createPermutation(String wordToLookFor)
    {
        String permutationSolutions = "";
        try {
            String currentDirectory = System.getProperty("user.dir");
            if (!currentDirectory.endsWith("src"))
                currentDirectory+="/src";
            ProcessBuilder process = new ProcessBuilder("python3", currentDirectory+"/WordSubsetsAndPermutation.py");
            Process p = process.start();

            // Get the output and error streams from the process
            Scanner output = new Scanner(p.getInputStream());
            Scanner error = new Scanner(p.getErrorStream());

            // Write input to the process
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            writer.write(wordToLookFor);
            writer.newLine();
            writer.flush();
            writer.close();

            // Redirects the pythons console output into a variable for more processing
            permutationSolutions = output.nextLine();

            while (error.hasNextLine()) {
                System.out.println(error.nextLine());
            }

            output.close();
            error.close();
        }catch (Exception e) {
            System.out.println("Cannot create permutation from WordSubsetsAndPermutation.py");
            e.printStackTrace();
        }

        return permutationSolutions.trim().split("\\s+");
    }

}
