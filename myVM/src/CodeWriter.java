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

    public CodeWriter(String fname) {
        output = new File(fname);
        labelNum = 0;
        try {
            writer = new FileWriter(output, true);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void setFileName(String fileName) {

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
                        lnum + ")\n\t@SP\n\tA=M\n";
                labelNum++;
                break;
            case "gt":
                code = code + "\tD=M\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M-D\n\t@Jump" + lnum +
                        "\n\tD;JGT\n\t@SP\n\tA=M\n\tM=0\n\t@End" + lnum +
                        "\n\t0;JMP\n(Jump" + lnum + ")\n\t@SP\n\tA=M\n\tM=-1\n(End" +
                        lnum + ")\n\t@SP\n\tA=M\n";
                labelNum++;
                break;
            case "lt":
                code = code + "\tD=M\n\t@SP\n\tM=M-1\n\tA=M\n\tD=M-D\n\t@Jump" + lnum +
                        "\n\tD;JLT\n\t@SP\n\tA=M\n\tM=0\n\t@End" + lnum +
                        "\n\t0;JMP\n(Jump" + lnum + ")\n\t@SP\n\tA=M\n\tM=-1\n(End" +
                        lnum + ")\n\t@SP\n\tA=M\n";
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
                }
                break;
            case Parser.C_POP:
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
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
