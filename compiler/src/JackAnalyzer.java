import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class JackAnalyzer {
    private String fname;
    private ArrayList<String> files;
    private int index;
    private File input;
    private File output;
    private CompilationEngine ce;

    public JackAnalyzer(String fname) {
        this.fname = fname;
        File f = new File(fname);
        files = new ArrayList<>();

        if (f.isDirectory()) {
            final Stream<String> nameStream = Arrays.stream(f.list());
            nameStream
                    .filter(name -> name.endsWith("jack"))
                    .map(name -> String.join("/", fname, name))
                    .forEach(files::add);
        }

        index = 0;
    }

    public void execute() {
        while (index < files.size()) {
            input = new File(files.get(index));
            output = new File(files.get(index).replaceFirst("(?s)(.*)jack", "$1xml"));
            ce = new CompilationEngine(input, output);
            ce.compileClass();
            ce.closeOutput();
            index++;
        }
    }

    public static void main(String args []) {
        JackAnalyzer analyzer = new JackAnalyzer(args[0]);
        analyzer.execute();
    }
}
