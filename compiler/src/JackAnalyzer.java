import java.io.File;

public class JackAnalyzer {
    private String fname;
    private File input;
    private File output;

    public JackAnalyzer(String fname) {
        this.fname = fname;
        input = new File(fname);
        output = new File(fname.replaceFirst("(?s)(.*)jack", "$1xml"));
    }

    public static void main(String args []) {
        JackAnalyzer analyzer = new JackAnalyzer(args[0]);
    }
}
