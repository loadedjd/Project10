import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Jared Williams and Rocky Manrique
 *
 */
public class TCGenerator {

    /**
     * stores each word and how many times it occurs.
     */
    private static Map<String, Integer> wordCounts = new HashMap<String, Integer>();

    /**
     * Generates html code that generates the cloud word page. Calls all other
     * helper methods found in the class TCGEnerator.
     *
     * @param in
     *            SimpleReader variable for input
     * @param out
     *            SimpleWriter variable for output
     * @param inputName
     *            name of the input text file
     * @param numberOfWords
     *            number of words to be included in the generated tag cloud
     * @param seperators
     *            character array of the separators used to separate words
     */
    public static void generateTagCloud(BufferedReader in, PrintWriter out,
            String inputName, int numberOfWords, char[] seperators) {
        readWordsIntoMap(in, wordCounts, seperators);
        List<Entry<String, Integer>> sm = sortWords(wordCounts,
                numberOfWords);
        
        writeHtmlHeader(out, inputName, numberOfWords);
        writeHtmlBody(out, sm, inputName, numberOfWords);
        writeHtmlFooter(out);
    }

    /**
     * Begins generating the html code for the output file. The beginning
     * includes the header/title.
     *
     * @param out
     *            SimpleWriter variable for output
     * @param inputName
     *            name of the input text file
     * @param n
     *            holds the number of words to be included in the generated tag
     *            cloud
     */
    private static void writeHtmlHeader(PrintWriter out, String inputName,
            int n) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Top " + n + "words in " + inputName);
        out.println("</title>");
        out.println(
                "<link href='http://cse.osu.edu/software/2231/web-sw2/assignments/projects/tag-cloud-generator/data/tagcloud.css' rel=\"stylesheet\" type=\"text/css\">");
        out.println("</head>");
    }

    /**
     * Generates the body of the html code for each Map.Pair in the
     * sortingMachine and changes their font size based on the number of
     * occurrences counted.
     *
     * @param out
     *            SimpleWriter variable for output
     * @param words
     *            sorts the Map.Pairs of words and their number of occurences
     * @param inputName
     *            name of the input text file
     * @param n
     *            holds the number of words to be included in the generated tag
     *            cloud
     */
    private static void writeHtmlBody(PrintWriter out,
            List<Entry<String, Integer>> words, String inputName,
            int n) {

        out.println("<body>");
        out.println("<h2> Top " + n + " words in " + inputName + " </h2>");
        out.println("<hr>");

        out.println("<div class=\"cdiv\">\r\n" + "<p class=\"cbox\">");

        int max = 0;
        
        if (words.size() > 0) {
    		max = words.get(0).getValue();
    		
    		for (Entry<String, Integer> entry : words) {
    			if (entry.getValue() > max) {
    				max = entry.getValue();
    			}
    		}
    	}
        
        while (words.size() > 0) {
            Map.Entry<String, Integer> pair = words.remove(0);
            
            int font = calculateFont(max, pair.getValue());
            
            out.println("<span style='cursor:default' class=\"f" + font
                    + "\" title=\"count: " + pair.getValue() + "\">" + pair.getKey()
                    + " </span>");
        }

        out.println("</p>");
        out.println("</div>");

    }

    /**
     * Ends the html program.
     *
     * @param out
     *            SimpleWriter variable for output
     */
    private static void writeHtmlFooter(PrintWriter out) {
        out.println("</body>");
        out.println("</html>");
    }
    
    private static int calculateFont(int max, int count) {
    	double percentage = (double)count / (double)max;
    	Double font = percentage * 37;
    	
    	return font.intValue() + 11;
    }

    /**
     * Clears the given wordMap at the start of the method. Goes through each
     * line of the input text and adds each new word to the Map and counts all
     * of its occurrences.
     *
     * @param in
     *            SimpleReader variable for input
     * @param wordMap
     *            Map of all the words and their counts
     * @param seps
     *            character array of the separators used to separate words
     * @updates wordMap
     */
    private static void readWordsIntoMap(BufferedReader in,
            Map<String, Integer> wordMap, char[] seps) {
    	
    	
    	try {
    		String line = in.readLine();
    		wordMap.clear();
    		
    		line.replaceAll("", ":");
    		while (line != null) {
    			for (char sep : seps) {
    				line = line.replace(sep, ':');
    			}

    			String[] words = line.split(":");

    			for (String word : words) {
    				if (!wordMap.containsKey(word) && word != " ") {
    					wordMap.put(word, 1);
    				} else {
    					int count = wordMap.get(word);
    					count++;
    					wordMap.replace(word, count);
    				}
    			}
    			
    			line = in.readLine();
    		}
    	} catch (IOException e) {
    		System.err.print("Error reading from input file. " + e.getMessage());
    	}

    }

    /**
     * Takes in Map of words and the number of words to sort and returns the
     * sorted words. Uses IntComparator and AlphabeticalComparator to sort.
     *
     * @param words
     *            Map of all the words and their counts
     * @param numberOfWords
     *            number of words to be included in the generated tag cloud
     * @return SortingMachine of Pairs of each word and its count
     */
    private static List<Map.Entry<String, Integer>> sortWords(
            Map<String, Integer> words, int numberOfWords) {
    	
    	List<Map.Entry<String, Integer>> intList = new LinkedList<>();
    	List<Map.Entry<String, Integer>> alphList = new LinkedList<>();
    	
        AlphabeticalComparator stringComp = new AlphabeticalComparator();
        IntComparator intComp = new IntComparator();

        for (Entry<String, Integer> entry : words.entrySet()) {
        	intList.add(entry);
        }
        
        intList.sort(intComp);
        intList.remove(0); // Cannot figure out how to remove '' blank character from count
        
        for (int i = 0; i < numberOfWords; i++) {
        	alphList.add(intList.get(i));
        }
        
        alphList.sort(stringComp);

        return alphList;
    }

    /**
     * Compares Pairs by their int values.
     *
     * @author Jared Williams and Rocky Manrique
     *
     */
    private static class IntComparator
            implements Comparator<Entry<String, Integer>> {

        @Override
        public int compare(Entry<String, Integer> arg0,
                Entry<String, Integer> arg1) {
            return arg1.getValue() - arg0.getValue();
        }

    }

    /**
     * Compares Pairs by their alphabetical order.
     *
     * @author Jared Williams and Rocky Manrique
     *
     */
    private static class AlphabeticalComparator
            implements Comparator<Entry<String, Integer>> {

        @Override
        public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
            return o1.getKey().toLowerCase().compareTo(o2.getKey().toLowerCase());
        }

    }
}
