import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class CompilationEngine {
    private File input;
    private File output;
    private FileWriter writer;
    private JackToknizer jt;
    private HashMap<Integer, String> keywordMap;

    public CompilationEngine(File input, File output) {
        this.input = input;
        this.output = output;
        jt = new JackToknizer(input);
        try {
            writer = new FileWriter(output, true);
        } catch (IOException e) {
            System.out.println(e);
        }
        keywordMap = new HashMap<>();
        keywordMap.put(JackToknizer.CLASS, "class");
        keywordMap.put(JackToknizer.CONSTRUCTOR, "constructor");
        keywordMap.put(JackToknizer.FUNCTION, "function");
        keywordMap.put(JackToknizer.METHOD, "method");
        keywordMap.put(JackToknizer.FIELD, "field");
        keywordMap.put(JackToknizer.STATIC, "static");
        keywordMap.put(JackToknizer.VAR, "var");
        keywordMap.put(JackToknizer.INT, "int");
        keywordMap.put(JackToknizer.CHAR, "char");
        keywordMap.put(JackToknizer.BOOLEAN, "boolean");
        keywordMap.put(JackToknizer.VOID, "void");
        keywordMap.put(JackToknizer.TRUE, "true");
        keywordMap.put(JackToknizer.FALSE, "false");
        keywordMap.put(JackToknizer.NULL, "null");
        keywordMap.put(JackToknizer.THIS, "this");
        keywordMap.put(JackToknizer.LET, "let");
        keywordMap.put(JackToknizer.DO, "do");
        keywordMap.put(JackToknizer.IF, "if");
        keywordMap.put(JackToknizer.ELSE, "else");
        keywordMap.put(JackToknizer.WHILE, "while");
        keywordMap.put(JackToknizer.RETURN, "return");
    }

    public void compileClass() {
        try {
            writer.write("<tokens>\n");
            while (jt.hasMoreTokens()) {
                jt.advance();
                writeToken();
            }
            writer.write("</tokens>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compileClassVarDec() {

    }

    public void compileSubroutine() {

    }

    public void compileVarDec() {

    }

    public void compileStatements() {

    }

    public void compileParameterList () {

    }

    public void compileDo() {
    }

    public void compileLet() {

    }

    public void compileWhile() {

    }

    public void compileReturn() {

    }

    public void compileIf() {

    }

    public void compileExpression() {

    }

    public void compileTerm() {

    }

    public void compileExpressionList() {

    }

    private void writeToken() {
        try {
            switch (jt.tokenType()) {
                case JackToknizer.KEYWORD:
                    writer.write("<keyword> " + keywordMap.get(jt.keyWord()) + " </keyword>\n");
                    break;
                case JackToknizer.SYMBOL:
                    writer.write("<symbol> " +
                            ((jt.symbol().equals("<")) ? "&lt;" :
                                    (jt.symbol().equals(">")) ? "&gt;" :
                                            (jt.symbol().equals("&")) ? "&amp;" : jt.symbol())
                            + " </symbol>\n");
                    break;
                case JackToknizer.IDENTIFIER:
                    writer.write("<identifier> " + jt.identifier() + " </identifier>\n");
                    break;
                case JackToknizer.INT_CONST:
                    writer.write("<integerConstant> " + jt.intVal() + " </integerConstant>\n");
                    break;
                case JackToknizer.STRING_CONST:
                    writer.write("<stringConstant> " + jt.stringVal() + " </stringConstant>\n");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeOutput() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

