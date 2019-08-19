package extra;

import util.ConnectionPool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Extra {
    public static Set<String> wordMeaningless = new TreeSet<>();
    public static Set<String> wordMeaningful = new TreeSet<>();

    public static void extra(String str) {
        Pattern words = Pattern.compile("((?:[^a-zA-Z])[a-z]{2,})|([A-Z][a-z]+)|([A-Z]{2,})");
        Pattern words1 = Pattern.compile("([A-Za-z]+)");

        Matcher result = words.matcher(str);
        Matcher result1;
        PreparedStatement p;
        int count = 0;
        while (result.find()) {
            count++;
            result1 = words1.matcher(result.group());
            if (result1.find()) {
                String word = result1.group().toLowerCase();
                p = ConnectionPool.getOne();
                ResultSet res = null;
                try {
                    p.setString(1, word);
                    long timeBegin = System.nanoTime();
                    res = p.executeQuery();//查询一次耗时40ms左右   加unique，btree索引后耗时0.075912ms
                    System.out.println("6System.nanoTime()-timeBegin = " + ((System.nanoTime() - timeBegin) / 1000000F));
                    if (res.next()) {
                        wordMeaningful.add(word);
                        System.out.println("useful  : " + word);
                    } else {
                        wordMeaningless.add(word);
                        System.out.println("useless : " + word);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (res != null)
                            res.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    ConnectionPool.realeaseOne(p);
                }
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
