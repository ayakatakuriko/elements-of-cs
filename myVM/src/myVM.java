import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Main class to convert vm file into assemble file.
 *
 * @author ayakatakuriko
 */

public class myVM {
    private Parser parser;
    private CodeWriter cw;
    private ArrayList<String> files;
    private int index;

    public myVM(String fname) {
        files = new ArrayList<String>();
        File f = new File(fname);

        if (f.isDirectory()) {
            final Stream<String> nameStream = Arrays.stream(f.list());
            nameStream
                    .filter(name -> name.endsWith("vm"))
                    .forEach(files::add);
        } else
            files.add(fname);

        index = 0;
        cw = new CodeWriter((fname.endsWith(".vm")) ?
                fname.replaceFirst("(?s)(.*)vm", "$1asm") : fname + ".asm");
    }

    public void mainFunc() {
        while (index < files.size()) {
            parser = new Parser(files.get(index));
            if (!parser.hasMoreCommands()) {
                index++;
                continue;
            }
            do {
                parser.advance();
                addCode();
            } while (parser.hasMoreCommands());
            index++;
            parser.closeFile();
        }
        cw.close();
    }

    private void addCode() {
        switch (parser.commandType()) {
            case Parser.C_ARITHMETIC:
                cw.writeArithmetic(parser.arg1());
                break;
            case Parser.C_PUSH:
                cw.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
                break;
        }
    }

    public static void main (String args[]) {
        myVM vm = new myVM(args[0]);
        vm.mainFunc();
    }
}
