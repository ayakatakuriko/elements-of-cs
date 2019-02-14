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
    private SymbolTable st;
    private String type, name, className;
    private int kind;
    private VMWriter vm;
    private int nLocals, nArgs;
    private HashMap<String, Integer> funcMap;
    private int label;

    public CompilationEngine(File input, File output) {
        label = 0;
        funcMap = new HashMap<>();
        st = new SymbolTable();
        jt = new JackToknizer(input);
        try {
            writer = new FileWriter(output, true);
        } catch (IOException e) {
            System.out.println(e);
        }
        vm = new VMWriter(writer);
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
        /* class */
        jt.advance();
        /* class name*/
        jt.advance();
        className = jt.identifier();
        /* { */
        jt.advance();
        /* classVerDec*/
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
    }

    public void compileClassVarDec() {
        define(false, false);
        jt.advance();
        while (jt.tokenType() == JackToknizer.SYMBOL && jt.symbol().equals(",")) {
            /* , */
            /* var Name*/
            jt.advance();
            define(false, true);
            jt.advance();
        }
        /* ; */
    }

    public void compileSubroutine() {
        int kindOfSubroutine;
        nArgs = nLocals = 0;
        String funcName;
        st.startSubroutine(className);
        /* constructor | function | method */
        kindOfSubroutine = jt.keyWord();
        /* void | type */
        jt.advance();
        /* subroutineName */
        jt.advance();
        funcName = jt.identifier();
        /* ( */
        jt.advance();
        /* paramList */
        jt.advance();
        if (kindOfSubroutine == JackToknizer.METHOD) {
            nArgs++;
            st.define("this", className, SymbolTable.ARG);
        }
        compileParameterList();
        /* Add function and number of args to map*/
        if (!funcMap.containsKey(funcName))
            funcMap.put(funcName, kindOfSubroutine);
        /* ) */
        /* { */
        jt.advance();
        /*varDec*/
        jt.advance();
        while (jt.tokenType() == JackToknizer.KEYWORD &&
                jt.keyWord() == JackToknizer.VAR) {
            compileVarDec();
            jt.advance();
        }
        vm.writeFunction(className + "." + funcName, nLocals);
        if (kindOfSubroutine == JackToknizer.CONSTRUCTOR) {
            int fields = st.varCount(SymbolTable.FIELD);
            writeCode("push constant " + fields + "\n" +
                    "call Memory.alloc 1\npop pointer 0\n");
        } else if (kindOfSubroutine == JackToknizer.METHOD) {
            writeCode("push argument 0\npop pointer 0\n");
        }
        compileStatements();

        /* } */
    }

    public void compileVarDec() {
        define(false, false);
        nLocals++;

        jt.advance();
        while (jt.tokenType() == JackToknizer.SYMBOL && jt.symbol().equals(",")) {
            /* , */
            /* var Name*/
            jt.advance();
            define(false, true);
            jt.advance();
            nLocals++;
        }
        /* ; */
    }

    public void compileStatements() {
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
    }

    public void compileParameterList() {
        if (jt.tokenType() == JackToknizer.KEYWORD ||
                jt.tokenType() == JackToknizer.IDENTIFIER) {
            define(true, false);

            jt.advance();
            while (jt.tokenType() == JackToknizer.SYMBOL && jt.symbol().equals(",")) {
                /* , */
                /* type */
                jt.advance();
                define(true, false);
                jt.advance();
            }
        }
    }

    public void compileDo() {
        String caller;
        String subroutine;
        nArgs = 0;
        /* do */
        /* subroutineName | className | varName */
        jt.advance();
        caller = jt.identifier();
        jt.advance();
        if (jt.symbol().equals("(")) {
            /*同一クラス内の関数を実行（この時点でメソッドが確定）*/
            if (!funcMap.containsKey(caller)) {
                funcMap.put(caller, JackToknizer.METHOD);
            }
            vm.writePush(VMWriter.POINTER, 0);
            nArgs++;
            /* ( */
            jt.advance();
            compileExpressionList();
            vm.writeCall(className + "." + caller, nArgs);
            /* ) */
        } else if (jt.symbol().equals(".")) {
            if (st.kindOf(caller) != SymbolTable.NONE) {
                /* XXX.yyy()の形でXXXがテーブルにある=XXXはインスタンスかつyyyはメソッド */
                nArgs++;
                switch (st.kindOf(caller)) {
                    case SymbolTable.ARG:
                        vm.writePush(VMWriter.ARG, st.indexOf(caller));
                        break;
                    case SymbolTable.STATIC:
                        vm.writePush(VMWriter.STATIC, st.indexOf(caller));
                        break;
                    case SymbolTable.VAR:
                        vm.writePush(VMWriter.LOCAL, st.indexOf(caller));
                        break;
                    case SymbolTable.FIELD:
                        vm.writePush(VMWriter.THIS, st.indexOf(caller));
                        break;
                }
            }
            /* . */
            /* subroutineName */
            jt.advance();
            subroutine = jt.identifier();
            /* ( */
            jt.advance();
            jt.advance();
            compileExpressionList();
            vm.writeCall(((st.kindOf(caller) == SymbolTable.NONE) ?
                    caller : st.typeOf(caller)) + "." + subroutine, nArgs);
            /* ) */
        }
        vm.writePop(VMWriter.TEMP, 0);
        /* ; */
        jt.advance();
    }

    public void compileLet() {
        String assigned;
        boolean isArray = false;
        /* let */
        /* varName */
        jt.advance();
        assigned = jt.identifier();

        jt.advance();
        if (jt.tokenType() == JackToknizer.SYMBOL &&
                jt.symbol().equals("[")) {
            jt.advance();
            compileExpression();
            switch (st.kindOf(assigned)) {
                case SymbolTable.ARG:
                    vm.writePush(VMWriter.ARG, st.indexOf(assigned));
                    break;
                case SymbolTable.STATIC:
                    vm.writePush(VMWriter.STATIC, st.indexOf(assigned));
                    break;
                case SymbolTable.VAR:
                    vm.writePush(VMWriter.LOCAL, st.indexOf(assigned));
                    break;
                case SymbolTable.FIELD:
                    vm.writePush(VMWriter.THIS, st.indexOf(assigned));
                    break;
            }
            vm.writeArithmetic(VMWriter.ADD);
            /* ] */
            jt.advance();
            isArray = true;
        }
        /* = */
        jt.advance();
        compileExpression();

        if (isArray) {
            vm.writePop(VMWriter.TEMP, 0);
            vm.writePop(VMWriter.POINTER, 1);
            vm.writePush(VMWriter.TEMP, 0);
            vm.writePop(VMWriter.THAT, 0);
        } else {
            switch (st.kindOf(assigned)) {
                case SymbolTable.ARG:
                    vm.writePop(VMWriter.ARG, st.indexOf(assigned));
                    break;
                case SymbolTable.STATIC:
                    vm.writePop(VMWriter.STATIC, st.indexOf(assigned));
                    break;
                case SymbolTable.VAR:
                    vm.writePop(VMWriter.LOCAL, st.indexOf(assigned));
                    break;
                case SymbolTable.FIELD:
                    vm.writePop(VMWriter.THIS, st.indexOf(assigned));
                    break;
            }
        }
        /* ; */
    }

    public void compileWhile() {
        int label = this.label++;
        /* while */
        vm.writeLabel("WHILE" + label);
        /* ( */
        jt.advance();
        jt.advance();
        compileExpression();
        vm.writeArithmetic(VMWriter.NOT);
        /* ) */
        vm.writeIf("INSIDE_WHILE" + label);
        /* { */
        jt.advance();
        jt.advance();
        compileStatements();
        vm.writeGoto("WHILE" + label);
        vm.writeLabel("INSIDE_WHILE" + label);
        /* } */
    }

    public void compileReturn() {
        /* return */
        jt.advance();
        if (jt.tokenType() != JackToknizer.SYMBOL) {
            compileExpression();
        } else {
            // return の後に何もないので、0をpush
            vm.writePush(VMWriter.CONST, 0);
        }
        vm.writeReturn();
        /* ; */
    }

    public void compileIf() {
        int label = this.label++;
        /* if */
        /* ( */
        jt.advance();
        jt.advance();
        compileExpression();
        /* ) */
        /* { */
        jt.advance();
        jt.advance();
        vm.writeIf("IF_TRUE" + label);
        vm.writeGoto("IF_FALSE" + label);
        vm.writeLabel("IF_TRUE" + label);
        compileStatements();
        /* } */
        jt.advance();
        if (jt.tokenType() == JackToknizer.KEYWORD && jt.keyWord() == JackToknizer.ELSE) {
            /* else */
            vm.writeGoto("IF_END" + label);
            vm.writeLabel("IF_FALSE" + label);
            /* { */
            jt.advance();
            jt.advance();
            compileStatements();
            /* } */
            jt.advance();
            vm.writeLabel("IF_END" + label);
        } else {
            vm.writeLabel("IF_FALSE" + label);
        }
    }

    public void compileExpression() {
        String op;
        compileTerm();
        while (jt.tokenType() == JackToknizer.SYMBOL && opSet.contains(jt.symbol())) {
            /* op */
            op = jt.symbol();
            jt.advance();
            compileTerm();
            compileOp(op);
        }
    }

    private void compileOp(String op) {
        switch (op) {
            case "=":
                vm.writeArithmetic(VMWriter.EQ);
                break;
            case "+":
                vm.writeArithmetic(VMWriter.ADD);
                break;
            case "-":
                vm.writeArithmetic(VMWriter.SUB);
                break;
            case "*":
                vm.writeCall("Math.multiply", 2);
                break;
            case "/":
                vm.writeCall("Math.divide", 2);
                break;
            case "&":
                vm.writeArithmetic(VMWriter.AND);
                break;
            case "|":
                vm.writeArithmetic(VMWriter.OR);
                break;
            case "<":
                vm.writeArithmetic(VMWriter.LT);
                break;
            case ">":
                vm.writeArithmetic(VMWriter.GT);
                break;
        }
    }

    public void compileTerm() {
        String op;
        String pre;
        switch (jt.tokenType()) {
            case JackToknizer.SYMBOL:
                if (jt.symbol().equals("(")) {
                    /* (expression) */
                    /* ( */
                    jt.advance();
                    compileExpression();
                    /* ) */
                    jt.advance();
                } else {
                    op = jt.symbol();
                    /* unaryOp term */
                    jt.advance();
                    compileTerm();
                    switch (op) {
                        case "~":
                            vm.writeArithmetic(VMWriter.NOT);
                            break;
                        case "-":
                            vm.writeArithmetic(VMWriter.NEG);
                            break;
                    }
                }
                break;
            case JackToknizer.INT_CONST:
                vm.writePush(VMWriter.CONST, jt.intVal());
                jt.advance();
                break;
            case JackToknizer.STRING_CONST:
                vm.writePush(VMWriter.CONST, jt.stringVal().length());
                vm.writeCall("String.new", 1);
                for (int i = 0; i < jt.stringVal().length(); i++) {
                    vm.writePush(VMWriter.CONST, (int) jt.stringVal().charAt(i));
                    writeCode("call String.appendChar 2\n");
                }
                jt.advance();
                break;
            case JackToknizer.KEYWORD:
                switch (jt.keyWord()) {
                    case JackToknizer.THIS:
                        vm.writePush(VMWriter.POINTER, 0);
                        break;
                    case JackToknizer.NULL:
                    case JackToknizer.FALSE:
                        vm.writePush(VMWriter.CONST, 0);
                        break;
                    case JackToknizer.TRUE:
                        vm.writePush(VMWriter.CONST, 1);
                        vm.writeArithmetic(VMWriter.NEG);
                }
                jt.advance();
                break;
            case JackToknizer.IDENTIFIER:
                pre = jt.identifier();
                jt.advance();
                if (jt.symbol().equals("(")) {
                    nArgs = 0;
                    /*同一クラス内の関数を実行*/
                    if (!funcMap.containsKey(pre)) {
                        funcMap.put(pre, JackToknizer.METHOD);
                    }
                    vm.writePush(VMWriter.POINTER, 0);
                    nArgs++;

                    /* ( */
                    jt.advance();
                    compileExpressionList();
                    vm.writeCall(className + "." + pre, nArgs);
                    /* ) */
                } else if (jt.symbol().equals(".")) {
                    nArgs = 0;
                    if (st.kindOf(pre) != SymbolTable.NONE) {
                        /* XXX.yyy()の形でXXXがテーブルにある=XXXはインスタンスかつyyyはメソッド */
                        nArgs++;
                        switch (st.kindOf(pre)) {
                            case SymbolTable.ARG:
                                vm.writePush(VMWriter.ARG, st.indexOf(pre));
                                break;
                            case SymbolTable.STATIC:
                                vm.writePush(VMWriter.STATIC, st.indexOf(pre));
                                break;
                            case SymbolTable.VAR:
                                vm.writePush(VMWriter.LOCAL, st.indexOf(pre));
                                break;
                            case SymbolTable.FIELD:
                                vm.writePush(VMWriter.THIS, st.indexOf(pre));
                                break;
                        }
                    }
                    /* . */
                    /* subroutineName */
                    jt.advance();
                    String subroutine = jt.identifier();
                    /* ( */
                    jt.advance();
                    jt.advance();
                    compileExpressionList();
                    vm.writeCall(((st.kindOf(pre) == SymbolTable.NONE) ?
                            pre : st.typeOf(pre)) + "." + subroutine, nArgs);
                    /* ) */
                    jt.advance();
                } else if (jt.symbol().equals("[")) {
                    /* varName [expression] */
                    switch (st.kindOf(pre)) {
                        case SymbolTable.ARG:
                            vm.writePush(VMWriter.ARG, st.indexOf(pre));
                            break;
                        case SymbolTable.STATIC:
                            vm.writePush(VMWriter.STATIC, st.indexOf(pre));
                            break;
                        case SymbolTable.VAR:
                            vm.writePush(VMWriter.LOCAL, st.indexOf(pre));
                            break;
                        case SymbolTable.FIELD:
                            vm.writePush(VMWriter.THIS, st.indexOf(pre));
                            break;
                    }
                    /* [ */
                    jt.advance();
                    compileExpression();
                    /* ] */
                    vm.writeArithmetic(VMWriter.ADD);
                    vm.writePop(VMWriter.POINTER, 1);
                    vm.writePush(VMWriter.THAT, 0);
                    jt.advance();
                    break;
                } else {
                    /* varName */
                    switch (st.kindOf(pre)) {
                        case SymbolTable.ARG:
                            vm.writePush(VMWriter.ARG, st.indexOf(pre));
                            break;
                        case SymbolTable.STATIC:
                            vm.writePush(VMWriter.STATIC, st.indexOf(pre));
                            break;
                        case SymbolTable.VAR:
                            vm.writePush(VMWriter.LOCAL, st.indexOf(pre));
                            break;
                        case SymbolTable.FIELD:
                            vm.writePush(VMWriter.THIS, st.indexOf(pre));
                            break;
                    }
                }
        }
    }


    public void compileExpressionList() {
        if (jt.tokenType() != JackToknizer.SYMBOL || !jt.symbol().equals(")")) {
            nArgs++;
            compileExpression();
            while (jt.tokenType() == JackToknizer.SYMBOL && jt.symbol().equals(",")) {
                jt.advance();
                compileExpression();
                nArgs++;
            }
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

    private void define(boolean isArg, boolean isSameAsBefore) {
        if (isSameAsBefore) {
            /* name*/
            name = jt.identifier();
            st.define(name, type, kind);
            return;
        }

        if (isArg) kind = SymbolTable.ARG;
        else {
            switch (jt.keyWord()) {
                case JackToknizer.FIELD:
                    kind = SymbolTable.FIELD;
                    break;
                case JackToknizer.STATIC:
                    kind = SymbolTable.STATIC;
                    break;
                case JackToknizer.VAR:
                    kind = SymbolTable.VAR;
                    break;
                default:
                    return;
            }
            jt.advance();
        }
        /* type */
        type = (jt.tokenType() == JackToknizer.KEYWORD) ?
                keywordMap.get(jt.keyWord()) : jt.identifier();
        jt.advance();
        /* name*/
        name = jt.identifier();
        st.define(name, type, kind);
    }

}

