package extra;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Extra {
    public static Set<String> s=new TreeSet<>();
    @Test
    public static void extra(String str) {
        Pattern words = Pattern.compile("((?:[^a-zA-Z])[a-z]{2,})|([A-Z][a-z]+)|([A-Z]{2,})");
        Pattern words1 = Pattern.compile("([A-Za-z]+)");

        Matcher result = words.matcher(str);
        Matcher result1;
        int count = 0;
        while (result.find()) {
            count++;
            result1 = words1.matcher(result.group());
            if (result1.find()) {
                s.add(result1.group().toLowerCase());
                //System.out.print(result1.group().toLowerCase()+"|  |");
            } else
                System.out.println("??" + result.group() + "|  |\n");
        }
		
		/*StringTokenizer st = new StringTokenizer(text," ,?.!:\"\"''\n#");
		ArrayList<String> wordList = new ArrayList<>();
		while (st.hasMoreElements()) 
		{
		    wordList.add(st.nextToken().toLowerCase());
		}
		System.out.print(wordList);*/
    }
}
