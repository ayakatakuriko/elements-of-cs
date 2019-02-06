import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Parser class of the Element of Computer Architecture.
 * Parse one vm file.
 * @author ayakatakuriko
 * */

public class Parser {
    private File inputFile;
    private BufferedReader br;
    private String crr;
    private String next;
    private Set<String> arithmeticSet;
    static final int C_ARITHMETIC = 100;
    static final int C_PUSH = 200;
    static final int C_POP = 201;
    static final int C_LABEL = 300;
    static final int C_GOTO = 400;
    static final int C_IF = 500;
    static final int C_FUNCTION = 600;
    static final int C_RETURN = 700;
    static final int C_CALL = 800;

    public Parser(String fname) {
        crr = null;
        inputFile = new File(fname);
        arithmeticSet = new HashSet<>();
        arithmeticSet.add("add");
        arithmeticSet.add("sub");
        arithmeticSet.add("neg");
        arithmeticSet.add("eq");
        arithmeticSet.add("gt");
        arithmeticSet.add("lt");
        arithmeticSet.add("and");
        arithmeticSet.add("or");
        arithmeticSet.add("not");

        try {
            br = new BufferedReader(new FileReader(inputFile));
            next = br.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasMoreCommands() {
        return next != null;
    }

    public void advance() {
        try {
            do {
                if (!hasMoreCommands()) return;
                this.crr = this.next.replaceAll("//.*", "");
                this.crr = this.crr.replaceAll("\\s+$", "");
                this.crr = this.crr.replaceAll("^\\s+", "");
                this.next = br.readLine();
            } while (crr.equals(""));
        } catch (IOException e) {
            System.out.println(e);
        }
        return;
    }

    public int commandType() {
        String command = (crr.contains(" ")) ? crr.substring(0, crr.indexOf(" ")) : crr;
        if (arithmeticSet.contains(command)) return C_ARITHMETIC;
        else if (command.equals("push")) return C_PUSH;
        else if (command.equals("pop")) return C_POP;
        else return 0;
    }

    public String arg1() {
        if (commandType() == C_ARITHMETIC) return crr;
        String args[] = crr.split(" ", 3);
        return args[1];
    }

    public String arg2() {
        String args[] = crr.split(" ", 3);
        return args[2];
    }

    public void closeFile() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
