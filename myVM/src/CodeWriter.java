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
    String input;

    public CodeWriter(String fname) {
        output = new File(fname);
        input = null;
        labelNum = 0;
        try {
            writer = new FileWriter(output, true);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void setFileName(String fileName) {
        input = fileName.replaceFirst("(?s)(.*).vm", "$1");
        input = (input.lastIndexOf("/") < 0) ?
                        input : input.substring(input.length() - input.lastIndexOf("/") + 1, input.length());
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
}
