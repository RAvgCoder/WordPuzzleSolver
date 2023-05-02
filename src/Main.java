import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main
{
    public static void main (String[] args)
    {
	    String dictionaryPath = "../engmix.txt";
        File file = new File(dictionaryPath);
        Scanner input = new Scanner(System.in);

        System.out.println("Input char you want to check for on a single line");
				char[] charToLookFor = input.nextLine().trim()
								.replaceAll("\\s+","")
								.toCharArray();
        System.out.println(Arrays.toString(charToLookFor));
                
    }
}
