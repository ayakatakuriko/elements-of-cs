import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Assembler {
    private Parser parser;
    private Code code;
    private File output;
    private String outputFileName;
    private FileWriter writer;
    private SymbolTable table;
    private int lineNum;
    private Parser pForLabel;

    public Assembler(String fname) {
        parser = new Parser(fname);
        code = new Code();
        table = new SymbolTable();
        pForLabel = new Parser(fname);
        lineNum = 0;
        outputFileName = fname.replaceFirst("(?s)(.*)asm", "$1hack");
        output = new File(outputFileName);
        try {
            writer = new FileWriter(output, true);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void convertToAssemble() {
        addLabelToTable();
        if (!parser.hasMoreCommands()) return;
        do {
            parser.advance();
            addCode();
        } while (parser.hasMoreCommands());
        parser.closeFile();
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        }
    }

    private void addLabelToTable() {
        if (!pForLabel.hasMoreCommands()) return;
        do {
            pForLabel.advance();
            if (pForLabel.commandType() == Parser.L_COMMAND) {
                table.addTable(pForLabel.symbol(), lineNum);
                continue;
            }
            lineNum++;
        } while (pForLabel.hasMoreCommands());
        pForLabel.closeFile();
    }

    private String aCommand() {
        String code = null;
        String symbol = parser.symbol();
        try {
            int value = Integer.parseInt(symbol);
            code = Integer.toBinaryString(value);
        } catch (java.lang.NumberFormatException e) {
            // symbol is not value
            if (!table.isContain(symbol)) {
                table.addTable(symbol);
            }
            code = table.symbolToBinary(symbol);
        } finally {
            while (code.length() < 16) {
                code = "0" + code;
            }
            return code;
        }
    }

    private String cCommand() {
        return "111" + this.code.comp(parser.comp()) +
                this.code.dest(parser.dest()) + this.code.jump(parser.jump());
    }

    private void writeCommand(String code) {
        try {
            writer.write(code + "\n");
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

    public static void main (String args []) {
        Assembler as = new Assembler(args[0]);
        as.convertToAssemble();
    }
}
