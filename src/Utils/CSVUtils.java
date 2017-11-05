package Utils;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;

public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ',';

    public static void writeLine(Writer w, HashSet<String> result) throws IOException {
        writeLine(w, result, DEFAULT_SEPARATOR, ' ');
    }
    
    public static void writeLine(Writer w, String[] line) throws IOException {
    	StringBuilder sb = new StringBuilder();
    	for(String header : line) {
    		sb.append(header + ",");
    	}
    	w.append(sb.toString());
    }
    
    
    public static void writeLine(Writer w, String value) throws IOException {
    	StringBuilder sb = new StringBuilder();
    	sb.append(value);
    	sb.append("\n");
    	w.append(sb.toString());
    }

    public static void writeLine(Writer w, HashSet<String> values, char separators) throws IOException {
        writeLine(w, values, separators, ' ');
    }
    
    public static void newLine(Writer w) throws IOException {
    	writeLine(w,"\n");
    }

    //https://tools.ietf.org/html/rfc4180
    private static String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    public static void writeLine(Writer w, HashSet<String> result, char separators, char customQuote) throws IOException {

        boolean first = true;

        //default customQuote is empty

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (String value : result) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());


    }

}