import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author Jared Williams and Rocky Manrique
 *
 */
public class TCGMain {

    /**
     * character array of separators.
     */
    private static final char[] SEPS = { '\t', '.', '\n', ',', ' ', '[', '?',
            '!', ']' };

    /**
     * main method.
     *
     * @param args
     */
    public static void main(String[] args) {

        BufferedReader keyIn = new BufferedReader(
                new InputStreamReader(System.in));

        //input
        System.out.print("Input file: ");
        String inFile = null;
        try {
            inFile = keyIn.readLine();
        } catch (IOException e1) {
            System.err.println("Error reading from keyboard");
        }
        BufferedReader inFileReader = null;
        try {
            inFileReader = new BufferedReader(new FileReader(inFile));
        } catch (FileNotFoundException e1) {
            System.err.println(
                    "Error producing reader of input file (file not found)");
        }

        //output
        System.out.print("Output file: ");
        String outFile = null;
        PrintWriter outWriter = null;
        
        try {
            outFile = keyIn.readLine();
            outWriter = new PrintWriter(new File(outFile));
        } catch (IOException e1) {
            System.err.println("Error reading from keyboard");
        }

        //number of words
        System.out.println("Number of words to include: ");
        int numberOfWords = 0;
        try {
            numberOfWords = Integer.parseInt(keyIn.readLine());
        } catch (IOException e1) {
            System.err.println("Error reading from keyboard");
        }

        TCGenerator.generateTagCloud(inFileReader, outWriter, inFile, numberOfWords, SEPS);
        outWriter.close();
        try {
			inFileReader.close();
		} catch (IOException e) {
			System.err.println("Error closing file reader");
		}
    }

}
