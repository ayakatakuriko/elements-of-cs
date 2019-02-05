import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Assembler {
    private Parser parser;
    private Code code;
    private File output;
    private String outputFileName;
    private FileWriter writer;

    public Assembler(String fname) {
        parser = new Parser(fname);
        code = new Code();
        outputFileName = fname.replaceFirst("(?s)(.*)asm", "$1hack");
        output = new File(outputFileName);
        try {
            writer = new FileWriter(output, true);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Write a line of binary code into output file.
     * You have to check there are any assemble code to read before call
     * this method.
     */
    private void addCode() {
        String written;

        switch (parser.commandType()) {
            case Parser.A_COMMAND:
                written = aCommand();
                writeCommand(written);
                break;
            case Parser.C_COMMAND:
                written = cCommand();
                writeCommand(written);
                break;
            case Parser.L_COMMAND:
                //TODO
        }
    }


    private String aCommand() {
        String code = null;
        try {
            int value = Integer.parseInt(parser.symbol());
            code = Integer.toBinaryString(value);
        } catch (java.lang.NumberFormatException e) {
            // symbol is not value
            // TODO
        } finally {
            return code;
        }
    }

    private String cCommand() {
        return "111" + this.code.comp(parser.comp()) +
                this.code.dest(parser.dest()) + this.code.jump(parser.jump());
    }

    private void writeCommand(String code) {
        String c = devideBlock(code);
        try {
            writer.write(c + "\n");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Convert 1111000011110000 to
     * 1111 0000 1111 0000
     * */
    private String devideBlock(String code) {
        StringBuilder sb = new StringBuilder(code);
        sb.insert(12, " ");
        sb.insert(8, " ");
        sb.insert(4, " ");
        return sb.toString();
    }
}
