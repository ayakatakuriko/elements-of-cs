import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {
    private File output;
    private FileWriter writer;
    public static final int CONST = 400;
    public static final int ARG = 401;
    public static final int LOCAL = 402;
    public static final int STATIC = 403;
    public static final int THIS = 404;
    public static final int THAT = 405;
    public static final int POINTER = 406;
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
        try {
            writer = new FileWriter(this.output, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writePush(int segment, int index) {
        String code = "push ";

        switch (segment) {
            case CONST:
                code += "constant ";
                break;
            case ARG:
                code += "argument ";
                break;
            case LOCAL:
                code += "local";
                break;
            case STATIC:
                code += "static ";
                break;
            case THIS:
                code += "this ";
                break;
            case THAT:
                code += "that ";
                break;
            case POINTER:
                code += "pointer";
                break;
            case TEMP:
                code += "temp ";
                break;
        }
        code += index + "\n";
        writeCode(code);
    }

    public void writePop(int segment, int index) {
        String code = "pop ";

        switch (segment) {
            case ARG:
                code += "argument ";
                break;
            case LOCAL:
                code += "local";
                break;
            case STATIC:
                code += "static ";
                break;
            case THIS:
                code += "this ";
                break;
            case THAT:
                code += "that ";
                break;
            case POINTER:
                code += "pointer";
                break;
            case TEMP:
                code += "temp ";
                break;
        }
        code += index + "\n";
        writeCode(code);
    }

    public void writeArithmetic(int command) {
        String code = null;

        switch (command) {
            case ADD:
                code = "add";
                break;
            case SUB:
                code = "sub";
                break;
            case NEG:
                code = "neg";
                break;
            case EQ:
                code = "eq";
                break;
            case GT:
                code = "gt";
                break;
            case LT:
                code = "lt";
                break;
            case AND:
                code = "and";
                break;
            case OR:
                code = "or";
                break;
            case NOT:
                code = "not";
                break;
        }
        code += "\n";
        writeCode(code);
    }

    public void writeLabel(String label) {
        writeCode("label " + label + "\n");
    }

    public void writeGoto(String label) {
    writeCode("goto " + label + "\n");
    }

    public void writeIf(String label) {
        writeCode("if-goto " + label + "\n");
    }

    public void writeCall(String name, int nArgs) {
        writeCode("call " + name + " " + nArgs + "\n");
    }

    public void writeFunction(String name, int nLocals) {
        writeCode("function " + name + " " + name + "\n");
    }

    public void writeReturn() {
        writeCode("return");
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeCode(String code) {
        try {
            writer.write(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
