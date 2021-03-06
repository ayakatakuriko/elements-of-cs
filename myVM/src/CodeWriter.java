import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * CodeWriter class of the Element of Computer Architecture.
 * Convert vm code int assemble code.
 * @author ayakatakuriko
 * */

public class CodeWriter {
    private FileWriter writer;
    private File output;
    private int labelNum;
    private int callFuncNum;
    private String input;

    public CodeWriter(String fname) {
        output = new File(fname);
        input = null;
        labelNum = 0;
        callFuncNum = 0;
        try {
            writer = new FileWriter(output, true);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void setFileName(String fileName) {
        input = fileName.replaceFirst("(?s)(.*).vm", "$1");
        input = (input.lastIndexOf("/") < 0) ?
                        input : input.substring(1 + input.lastIndexOf("/"), input.length());
    }

    public void writeArithmetic(String command) {
        String code = "\t@SP\n\tM=M-1\n\tA=M\n";
        String lnum = Integer.toString(labelNum);
        switch (command) {
            case "add":
                code = code + "\tD=M\n\t@SP\n\tM=M-1\n\tA=M\n\tM=M+D\n";
                break;
            case "sub":
                code = code + "\tD=M\n\t@SP\n\tM=M-1\n\tA=M\n\tM=M-D\n";
                break;
            case "neg":
                code = code + "\tM=-M\n";
                break;
            case "eq":
                code = code + "\tD=M\n\t@SP\n\tM=M-1\n\tA=M\n\tD=D-M\n\t@Jump" + lnum +
                        "\n\tD;JEQ\n\t@SP\n\tA=M\n\tM=0\n\t@End" + lnum +
                        "\n\t0;JMP\n(Jump" + lnum + ")\n\t@SP\n\tA=M\n\tM=-1\n(End" +
                        lnum + ")\n";
                labelNum++;
                break;
            case "gt":
                code = code + "\tD=M\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M-D\n\t@Jump" + lnum +
                        "\n\tD;JGT\n\t@SP\n\tA=M\n\tM=0\n\t@End" + lnum +
                        "\n\t0;JMP\n(Jump" + lnum + ")\n\t@SP\n\tA=M\n\tM=-1\n(End" +
                        lnum + ")\n";
                labelNum++;
                break;
            case "lt":
                code = code + "\tD=M\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M-D\n\t@Jump" + lnum +
                        "\n\tD;JLT\n\t@SP\n\tA=M\n\tM=0\n\t@End" + lnum +
                        "\n\t0;JMP\n(Jump" + lnum + ")\n\t@SP\n\tA=M\n\tM=-1\n(End" +
                        lnum + ")\n";
                labelNum++;
                break;
            case "and":
                code = code + "\tD=M\n\t@SP\n\tM=M-1\n\tA=M\n\tM=M&D\n";
                break;
            case "or":
                code = code + "\tD=M\n\t@SP\n\tM=M-1\n\tA=M\n\tM=M|D\n";
                break;
            case "not":
                code = code + "\tM=!M\n";
                break;
        }
        code = code + "\t@SP\n\tM=M+1\n";

        writeCode(code);
    }

    public void writePushPop(int cmdType, String segment, String index) {
        String code = null;

        switch (cmdType) {
            case Parser.C_PUSH:
                switch (segment) {
                    case "constant":
                        code = "\t@" + index + "\n\tD=A\n\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n";
                        break;
                    case "local":
                        code = "\t@" + index + "\n\tD=A\n\t@LCL\n\tA=M+D\n\tD=M\n\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n";
                        break;
                    case "argument":
                        code = "\t@" + index + "\n\tD=A\n\t@ARG\n\tA=M+D\n\tD=M\n\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n";
                        break;
                    case "this":
                        code = "\t@" + index + "\n\tD=A\n\t@THIS\n\tA=M+D\n\tD=M\n\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n";
                        break;
                    case "that":
                        code = "\t@" + index + "\n\tD=A\n\t@THAT\n\tA=M+D\n\tD=M\n\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n";
                        break;
                    case "pointer":
                        code = "\t@" + index + "\n\tD=A\n\t@THIS\n\tA=A+D\n\tD=M\n\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n";
                        break;
                    case "temp":
                        code = "\t@" + index + "\n\tD=A\n\t@R5\n\tA=A+D\n\tD=M\n\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n";
                        break;
                    case "static":
                        code = "\t@" + input + "." + index + "\n\tD=M\n\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n";
                        break;
                }
                break;
            case Parser.C_POP:
                switch (segment) {
                    case "local":
                        code = "\t@" + index + "\n\tD=A\n\t@LCL\n\tD=M+D\n\t@R15\n\tM=D\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n\t@R15\n\tA=M\n\tM=D\n";
                        break;
                    case "argument":
                        code = "\t@" + index + "\n\tD=A\n\t@ARG\n\tD=M+D\n\t@R15\n\tM=D\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n\t@R15\n\tA=M\n\tM=D\n";
                        break;
                    case "this":
                        code = "\t@" + index + "\n\tD=A\n\t@THIS\n\tD=M+D\n\t@R15\n\tM=D\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n\t@R15\n\tA=M\n\tM=D\n";
                        break;
                    case "that":
                        code = "\t@" + index + "\n\tD=A\n\t@THAT\n\tD=M+D\n\t@R15\n\tM=D\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n\t@R15\n\tA=M\n\tM=D\n";
                        break;
                    case "pointer":
                        code = "\t@" + index + "\n\tD=A\n\t@THIS\n\tD=A+D\n\t@R15\n\tM=D\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n\t@R15\n\tA=M\n\tM=D\n";
                        break;
                    case "temp":
                        code = "\t@" + index + "\n\tD=A\n\t@R5\n\tD=A+D\n\t@R15\n\tM=D\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n\t@R15\n\tA=M\n\tM=D\n";
                        break;
                    case "static":
                        code = "@SP\n\tM=M-1\n\tA=M\n\tD=M\n\t@" + input + "." + index + "\n\tM=D\n";
                        break;
                }
                break;
        }

        writeCode(code);
    }

    private void writeCode(String code) {
        try {
            writer.write(code);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void close() {
        try {
            writeCode("(END_INFINIT_LOOP)\n\t@END_INFINIT_LOOP\n\t0;JMP\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInit() {
        writeCode("\t@256\n\tD=A\n\t@SP\n\tM=D\n");
        writeCall("Sys.init", 0);
    }

    public void writeLabel(String label) {
        writeCode("(" + label + ")\n");
    }

    public void writeGoto(String label) {
        writeCode("\t@" + label + "\n\t0;JMP\n");
    }

    public void writeIf(String label) {
        writeCode("\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n\t@" + label + "\n\tD;JNE\n");
    }

    public void writeCall(String functionName, int numArgs) {
        String push = "\t@SP\n\tA=M\n\tM=D\n\t@SP\n\tM=M+1\n";
        writeCode("\t@" + functionName + "." + callFuncNum + ".returnAddress\n\tD=A\n" + push +
                "\t@LCL\n\tD=M\n" + push +
                "\t@ARG\n\tD=M\n" + push +
                "\t@THIS\n\tD=M\n" + push +
                "\t@THAT\n\tD=M\n" + push +
                "\t@SP\n\tD=M\n\t@5\n\tD=D-A\n\t@" + numArgs + "\n\tD=D-A" +
                "\n\t@ARG\n\tM=D\n\t@SP\n\tD=M\n\t@LCL\n\tM=D\n");
        writeGoto(functionName);
        writeCode("(" + functionName + "." + callFuncNum + ".returnAddress)\n");
        callFuncNum++;
    }

    public void writeReturn() {
        writeCode("\t@LCL\n\tD=M\n\t@R15\n\tM=D\n\t@5\n\tA=D-A\n\tD=M\n\t@R14\n\t" +
                "M=D\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M\n\t@ARG\n\tA=M\n\tM=D\n\t@ARG\n\tD=M+1\n\t@SP" +
                "\n\tM=D\n\t@R15\n\tA=M\n\tA=A-1\n\tD=M\n\t@THAT\n\tM=D\n\t@R15" +
                "\n\tA=M\n\tA=A-1\n\tA=A-1\n\tD=M\n\t@THIS\n\tM=D\n\t@R15\n\tA=M" +
                "\n\tA=A-1\n\tA=A-1\n\tA=A-1\n\tD=M\n\t@ARG\n\tM=D\n\t@R15\n\tA=M" +
                "\n\tA=A-1\n\tA=A-1\n\tA=A-1\n\tA=A-1\n\tD=M\n\t@LCL\n\tM=D\n\t" +
                "@R14\n\tA=M\n\t0;JMP\n");
    }

    public void writeFunction(String functionName, int numLocals) {
        writeCode("(" + functionName + ")\n\t@" + numLocals + "\n\tD=A\n\t@" +
                functionName + ".InitializeEnd\n\tD;JEQ\n(" + functionName +
                ".Initialize)\n\tD=D-1\n\t@SP\n\tA=M\n\tM=0\n\t@SP\n\tM=M+1\n\t@" +
                functionName + ".Initialize\n\tD;JNE\n(" + functionName +
                ".InitializeEnd)\n");
    }
}
