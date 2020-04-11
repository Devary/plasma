import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCases {
    public static void main(String[] args) {
        String line = "private String x;";
        findVariableWithRegexMatcher(line);
    }

    private static void findVariableWithRegexMatcher(String line) {

        final Matcher m = Pattern.compile("private*").matcher(line);
        //System.out.println(m.matches());
        final StringBuffer b = new StringBuffer(line.length());
        while (m.find())
            m.appendReplacement(b,line);
        System.out.println(m.groupCount());




    }

}
