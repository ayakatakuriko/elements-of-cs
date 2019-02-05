import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parse of The Element of Computer Architecture.
 * Parse assemble commands into basic elements.
 *
 * @author ayakatakuriko
 * @data 2019/02/05
 */

public class Parser {
    private final int A_COMMAND = 100;
    private final int C_COMMAND = 200;
    private final int L_COMMAND = 300;
    private File inputFile;
    private BufferedReader br;
    private String next;
    private String crr;

    public Parser(String fname) {
        this.crr = null;
        try {
            this.inputFile = new File(fname);
            this.br = new BufferedReader(new FileReader(inputFile));
            this.next = this.br.readLine();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * If there are any command to read, return true.
     * Otherwise, return false.
     */
    public boolean hasMoreCommands() {
        return next != null;
    }

    /**
     * Return command.
     */
    public void advance() {
        try {
            do {
                if (!hasMoreCommands()) return;
                this.crr = this.next.replaceAll("\\s+", "");
                this.crr = this.crr.replaceAll("//.*", "");
                this.next = br.readLine();
            } while (crr.equals(""));
        } catch (IOException e) {
            System.out.println(e);
        }
        return;
    }

    /**
     * Return current command type.
     */
    public int commandType() {
        switch (crr.charAt(0)) {
            case '@':
                return A_COMMAND;
            case '(':
                return L_COMMAND;
            default:
                return C_COMMAND;
        }
    }

    /**
     * Return Xxx of @Xxx or (Xxx).
     * */
    public String symbol() {
        int type = this.commandType();

        switch (type) {
            case A_COMMAND:
                return crr.replaceAll("@", "");
            case L_COMMAND:
                return crr.replaceAll("[()]", "");
            default:
                return null;
        }
    }

    /**
     * Returns the dest mnemonic of the current C instruction.
     * */
    public String dest() {
        if (this.commandType() != C_COMMAND ||
                !this.crr.contains("=")) return null;

        return crr.substring(0, crr.indexOf("="));
    }

    public String comp() {
        if (this.commandType() != C_COMMAND) return null;

        int start = (crr.contains("=")) ? crr.indexOf("=") + 1: 0;
        int end = crr.contains(";") ? crr.indexOf(";"): crr.length();

        return crr.substring(start, end);
    }

    public String jump() {
        if (this.commandType() != C_COMMAND ||
                !this.crr.contains(";")) return null;

        return crr.substring(crr.indexOf(";") + 1, crr.length());
    }
}
