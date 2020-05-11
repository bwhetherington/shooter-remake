import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// For parsing between characters:
// "\\A([^)]*?)\\B" Where A and B are the start and end, i.e. [ and ].

public class GMultiLabel extends GComponent {
	public static final String
		REGEX = "\\[([^)]*?)\\]",
		TAG_COLOR = "color",
		TAG_BOLD = "b",
		TAG_ITALICS = "i";
	
	private List<GLabel> labels;
	
	private void parseString(String str) {
		Matcher m = Pattern.compile(REGEX).matcher(str);
		List<Pair<Integer, Integer>> pairs = new ArrayList<>();
        while(m.find()) {
        	Pair<Integer, Integer> pair = new Pair<>(m.start() + 1, m.end() - 1);
        	pairs.add(pair);
        }
        
	}
	
	public static void main(String[] args) {
		String str = "This is a [ffffff]f[ff] test";
		Matcher m = Pattern.compile("\\[([^)]*?)\\]").matcher(str);
		List<Pair<Integer, Integer>> pairs = new ArrayList<>();
        while(m.find()) {
        	Pair<Integer, Integer> pair = new Pair<>(m.start() + 1, m.end() - 1);
        	pairs.add(pair);
        }
        for (Pair<Integer, Integer> pair : pairs) {
        	System.out.println(str.substring(pair.a, pair.b));
        }
//        System.out.println(pairs);
	}
} 
