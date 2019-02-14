import java.io.File;

public class VMWriter {
    private File output;
    public static final int CONST = 400;
    public static final int ARG = 401;
    public static final int LOCAL = 402;
    public static final int STATIC = 403;
    public static final int THIS = 404;
    public static final int THAT = 405;
    public static final int POINTERS = 406;
    public static final int TEMP = 407;

    public static final int ADD = 500;
    public static final int SUB = 501;
    public static final int NEG = 502;
    public static final int EQ = 503;
    public static final int GT = 504;
    public static final int LT = 505;
    public static final int AND = 506;
    public static final int OR = 507;
    public static final int NOT = 508;

    public VMWriter(File output) {
        this.output = output;
    }

    public void writePush(int segment, int index) {

    }

    public void writePop(int segment, int index) {

    }

    public void writeArithmetic(int command) {

    }

    public void writeLabel(String label) {

    }

    public void writeGoto(String label) {

    }

    public void writeIf(String label) {

    }

    public void writeCall(String name, int nArgs) {

    }

    public void writeFunction(String name, int nLocals) {

    }

    public void writeReturn() {

    }

    public void close() {

    }
}
