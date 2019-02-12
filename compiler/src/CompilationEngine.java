import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class CompilationEngine {
    private FileWriter writer;
    private JackToknizer jt;
    private HashMap<Integer, String> keywordMap;
    private HashSet<String> opSet;
    private String preToken;

    public CompilationEngine(File input, File output) {
        jt = new JackToknizer(input);
        preToken = null;
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

        opSet = new HashSet<>();
        opSet.add("+");
        opSet.add("-");
        opSet.add("*");
        opSet.add("/");
        opSet.add("&");
        opSet.add("|");
        opSet.add("<");
        opSet.add(">");
        opSet.add("=");
    }

    public void compileClass() {
        writeCode("<class>\n");
        /* class */
        jt.advance();
        writeToken();
        /* class name*/
        jt.advance();
        writeToken();
        /* { */
        jt.advance();
        writeToken();
        jt.advance();
        while (jt.tokenType() == JackToknizer.KEYWORD &&
                (jt.keyWord() == JackToknizer.STATIC ||
                        jt.keyWord() == JackToknizer.FIELD)) {
            compileClassVarDec();
            jt.advance();
        }

        while (jt.tokenType() == JackToknizer.KEYWORD &&
                (jt.keyWord() == JackToknizer.CONSTRUCTOR ||
                        jt.keyWord() == JackToknizer.FUNCTION ||
                        jt.keyWord() == JackToknizer.METHOD)) {
            compileSubroutine();
            jt.advance();
        }
        /* } */
        writeToken();

        writeCode("</class>\n");
    }

    public void compileClassVarDec() {
        writeCode("<classVarDec>\n");
        /* static | field*/
        writeToken();
        /* type */
        jt.advance();
        writeToken();
        /* var Name*/
        jt.advance();
        writeToken();

        jt.advance();
        while (jt.tokenType() == JackToknizer.SYMBOL && jt.symbol().equals(",")) {
            /* , */
            writeToken();
            /* var Name*/
            jt.advance();
            writeToken();
            jt.advance();
        }
        /* ; */
        writeToken();
        writeCode("</classVarDec>\n");
    }

    public void compileSubroutine() {
        writeCode("<subroutineDec>\n");
        /* constructor | function | method */
        writeToken();
        /* void | type */
        jt.advance();
        writeToken();
        /* subroutineName */
        jt.advance();
        writeToken();
        /* ( */
        jt.advance();
        writeToken();

        jt.advance();
        compileParameterList();

        /* ) */
        jt.advance();
        writeToken();

        writeCode("<subroutineBody>\n");
        /* { */
        jt.advance();
        writeToken();

        jt.advance();
        if (jt.tokenType() == JackToknizer.KEYWORD &&
                jt.keyWord() == JackToknizer.VAR) {
            compileVarDec();
            jt.advance();
        }

        compileStatements();

        /* } */
        writeToken();
        writeCode("</subroutineBody>\n");
        writeCode("</subroutineDec>\n");
    }

    public void compileVarDec() {
        writeCode("<varDec>\n");
        /* var */
        jt.advance();
        writeToken();
        /* type */
        jt.advance();
        writeToken();
        /* varName */
        jt.advance();
        writeToken();

        jt.advance();
        while (jt.tokenType() == JackToknizer.SYMBOL && jt.symbol().equals(",")) {
            /* , */
            writeToken();
            /* var Name*/
            jt.advance();
            writeToken();
            jt.advance();
        }
        /* ; */
        writeToken();
        writeCode("</varDec>\n");
    }

    public void compileStatements() {
        writeCode("<statements>\n");
        while (jt.tokenType() == JackToknizer.KEYWORD) {
            switch (jt.keyWord()) {
                case JackToknizer.LET:
                    compileLet();
                    jt.advance();
                    break;
                case JackToknizer.IF:
                    compileIf();
                    break;
                case JackToknizer.WHILE:
                    compileWhile();
                    jt.advance();
                    break;
                case JackToknizer.DO:
                    compileDo();
                    jt.advance();
                    break;
                case JackToknizer.RETURN:
                    compileReturn();
                    jt.advance();
                    break;
            }
        }
        writeCode("</statements>\n");
    }

    public void compileParameterList() {
        writeCode("<parameterList>\n");
        if (jt.tokenType() == JackToknizer.KEYWORD &&
                jt.tokenType() == JackToknizer.IDENTIFIER) {

            /* type */
            jt.advance();
            writeToken();
            /* var Name */
            jt.advance();
            writeToken();

            jt.advance();
            while (jt.tokenType() == JackToknizer.SYMBOL && jt.symbol().equals(",")) {
                /* , */
                writeToken();
                /* type */
                jt.advance();
                writeToken();
                /* var name */
                jt.advance();
                writeToken();
                jt.advance();
            }
        }
        writeCode("</parameterList>\n");
    }

    public void compileDo() {
        writeCode("<doStatement>\n");
        /* do */
        writeToken();

        jt.advance();
        compileTerm();

        /* ; */
        jt.advance();
        writeToken();
        writeCode("</doStatement>\n");
    }

    public void compileLet() {
        writeCode("<letStatement>\n");
        /* let */
        writeToken();
        /* ; */
        jt.advance();
        writeToken();

        jt.advance();
        if (jt.tokenType() == JackToknizer.SYMBOL &&
                jt.symbol().equals("[")) {
            writeToken();
            jt.advance();
            compileExpression();

            /* ] */
            writeToken();
            jt.advance();
        }
        /* = */
        writeToken();
        jt.advance();
        compileExpression();
        /* ; */
        writeToken();
        writeCode("</letStatement>\n");
    }

    public void compileWhile() {
        writeCode("<whileStatement>\n");
        /* while */
        writeToken();
        /* ( */
        jt.advance();
        writeToken();
        jt.advance();
        compileExpression();
        /* ) */
        writeToken();
        /* { */
        jt.advance();
        writeToken();
        jt.advance();
        compileStatements();
        /* } */
        writeToken();
        writeCode("</whileStatement>\n");
    }

    public void compileReturn() {
        writeCode("<returnStatement>\n");
        /* return */
        writeToken();
        jt.advance();
        if (jt.tokenType() != JackToknizer.SYMBOL) {
            compileExpression();
        }
        /* ; */
        writeToken();
        writeCode("</returnStatement>\n");
    }

    public void compileIf() {
        writeCode("<ifStatement>\n");
        /* if */
        writeToken();
        /* ( */
        jt.advance();
        writeToken();
        jt.advance();
        compileExpression();
        /* ) */
        writeToken();
        /* { */
        jt.advance();
        writeToken();
        jt.advance();
        compileStatements();
        /* } */
        writeToken();

        jt.advance();
        if (jt.tokenType() == JackToknizer.KEYWORD && jt.keyWord() == JackToknizer.ELSE) {
            /* else */
            writeToken();
            /* { */
            jt.advance();
            writeToken();
            jt.advance();
            compileExpression();
            /* } */
            writeToken();
            jt.advance();
        }
        writeCode("</ifStatement>\n");
    }

    public void compileExpression() {
        writeCode("<expression>\n");
        compileTerm();
        while (jt.tokenType() == JackToknizer.SYMBOL && opSet.contains(jt.symbol())) {
            /* op */
            writeCode("<op>\n");
            writeToken();
            writeCode("</op>\n");
            compileTerm();
        }
        writeCode("</expression>\n");
    }

    public void compileTerm() {
        writeCode("<term>\n");
        switch (jt.tokenType()) {
            case JackToknizer.SYMBOL:
                if (jt.symbol().equals("(")) {
                    /* (expression) */
                    /* ( */
                    writeToken();
                    jt.advance();
                    compileExpression();
                    /* ) */
                    writeToken();
                    jt.advance();
                } else {
                    /* unaryOp term */
                    writeCode("<unaryOp>\n");
                    writeToken();
                    jt.advance();
                    compileTerm();
                    writeCode("</unaryOp>\n");
                }
                break;
            case JackToknizer.INT_CONST:
                writeToken();
                jt.advance();
                break;
            case JackToknizer.STRING_CONST:
                writeToken();
                jt.advance();
                break;
            case JackToknizer.KEYWORD:
                writeCode("<KeywordConstant>\n");
                writeToken();
                writeCode("</KeywordConstant>\n");
                jt.advance();
                break;
            case JackToknizer.IDENTIFIER:
                preToken = jt.identifier();
                jt.advance();
                if (jt.tokenType() == JackToknizer.SYMBOL) {
                    if (jt.symbol().equals("(")) {
                        /* subroutineCall */
                        writeCode("<identifier> " + preToken + " </identifier>\n");
                        /* ( */
                        writeToken();
                        jt.advance();
                        compileExpression();
                        /* ) */
                        writeToken();
                        jt.advance();
                        break;
                    } else if (jt.symbol().equals(".")) {
                        /* subroutineCall */
                        writeCode("<identifier> " + preToken + " </identifier>\n");
                        /* . */
                        writeToken();
                        /* subroutineName */
                        jt.advance();
                        writeToken();
                        /* ( */
                        writeToken();
                        jt.advance();
                        compileExpressionList();
                        /* ) */
                        writeToken();
                        jt.advance();
                        break;
                    }else if (jt.symbol().equals("[")) {
                       /* varName [expression] */
                        writeCode("<identifier> " + preToken + " </identifier>\n");
                        /* [ */
                        writeToken();
                        jt.advance();
                        compileExpression();
                        /* ] */
                        writeToken();
                        jt.advance();
                        break;
                    }
                }
                /* varName */
                    writeCode("<identifier> " + preToken + " </identifier>\n");
        }
        writeCode("<term>\n");
    }

    public void compileExpressionList() {
        writeCode("<expressionList>\n");
        if (jt.tokenType() != JackToknizer.SYMBOL || !jt.symbol().equals("(")) {
            compileExpression();
            while (jt.tokenType() == JackToknizer.SYMBOL && jt.symbol().equals(",")) {
                writeToken();
                jt.advance();
                compileExpression();
            }
        }
        writeCode("</expressionList>\n");
    }

    private void writeToken() {

        switch (jt.tokenType()) {
            case JackToknizer.KEYWORD:
                writeCode("<keyword> " + keywordMap.get(jt.keyWord()) + " </keyword>\n");
                break;
            case JackToknizer.SYMBOL:
                writeCode("<symbol> " +
                        ((jt.symbol().equals("<")) ? "&lt;" :
                                (jt.symbol().equals(">")) ? "&gt;" :
                                        (jt.symbol().equals("&")) ? "&amp;" : jt.symbol())
                        + " </symbol>\n");
                break;
            case JackToknizer.IDENTIFIER:
                writeCode("<identifier> " + jt.identifier() + " </identifier>\n");
                break;
            case JackToknizer.INT_CONST:
                writeCode("<integerConstant> " + jt.intVal() + " </integerConstant>\n");
                break;
            case JackToknizer.STRING_CONST:
                writeCode("<stringConstant> " + jt.stringVal() + " </stringConstant>\n");
                break;
        }
    }

    public void closeOutput() {
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

