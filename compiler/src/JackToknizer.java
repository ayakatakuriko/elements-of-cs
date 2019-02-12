import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class JackToknizer {
    private File input;
    private String token;
    private BufferedReader br;
    private int crr;
    private int next;
    private HashSet<Integer> skipSet;
    private HashSet<Integer> symbolSet;
    private HashMap<String, Integer> keywordMap;
    static final int KEYWORD = 100;
    static final int SYMBOL = 101;
    static final int IDENTIFIER = 102;
    static final int INT_CONST = 103;
    static final int STRING_CONST = 104;
    static final int CLASS = 200;
    static final int METHOD = 201;
    static final int FUNCTION = 202;
    static final int CONSTRUCTOR = 203;
    static final int INT = 204;
    static final int BOOLEAN = 205;
    static final int CHAR = 206;
    static final int VOID = 207;
    static final int VAR = 208;
    static final int STATIC = 209;
    static final int FIELD = 210;
    static final int LET = 211;
    static final int DO = 212;
    static final int IF = 213;
    static final int ELSE = 214;
    static final int WHILE = 215;
    static final int RETURN = 216;
    static final int TRUE = 217;
    static final int FALSE = 218;
    static final int NULL = 219;
    static final int THIS = 220;

    public JackToknizer(File input) {
        skipSet = new HashSet<>();
        this.input = input;
        skipSet.add((int) ' ');
        skipSet.add((int) '\n');
        skipSet.add((int) '\t');
        skipSet.add((int) '\r');
        try {
            this.br = new BufferedReader(new FileReader((this.input)));
            do {
                this.next = br.read();
                if (this.next == '/') {
                    this.next = br.read();
                    if (next == '/') {
                        while (next != '\n')
                            this.next = br.read();
                    } else if (next == '*') {
                        while (true) {
                            this.next = br.read();
                            if (next == '*') {
                                next = br.read();
                                if (next == '/')
                                    break;
                            }
                        }
                        this.next = br.read();
                    }

                }
            } while (skipSet.contains(next));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        crr = 0;
        token = null;
        symbolSet = new HashSet<>();
        symbolSet.add((int)'{');
        symbolSet.add((int)'}');
        symbolSet.add((int)'(');
        symbolSet.add((int)')');
        symbolSet.add((int)'[');
        symbolSet.add((int)']');
        symbolSet.add((int)'.');
        symbolSet.add((int)',');
        symbolSet.add((int)';');
        symbolSet.add((int)'+');
        symbolSet.add((int)'-');
        symbolSet.add((int)'*');
        symbolSet.add((int)'/');
        symbolSet.add((int)'&');
        symbolSet.add((int)'|');
        symbolSet.add((int)'<');
        symbolSet.add((int)'>');
        symbolSet.add((int)'=');
        symbolSet.add((int)'~');
        keywordMap = new HashMap<>();
        keywordMap.put("class", CLASS);
        keywordMap.put("constructor", CONSTRUCTOR);
        keywordMap.put("function", FUNCTION);
        keywordMap.put("method", METHOD);
        keywordMap.put("field", FIELD);
        keywordMap.put("static", STATIC);
        keywordMap.put("var", VAR);
        keywordMap.put("int", INT);
        keywordMap.put("char", CHAR);
        keywordMap.put("boolean", BOOLEAN);
        keywordMap.put("void", VOID);
        keywordMap.put("true", TRUE);
        keywordMap.put("false", FALSE);
        keywordMap.put("null", NULL);
        keywordMap.put("this", THIS);
        keywordMap.put("let", LET);
        keywordMap.put("do", DO);
        keywordMap.put("if", IF);
        keywordMap.put("else", ELSE);
        keywordMap.put("while", WHILE);
        keywordMap.put("return", RETURN);
    }

    public boolean hasMoreTokens() {
        return next != -1;
    }

    public void advance() {
        if (!hasMoreTokens()) return;
        token = "";
        boolean isConstString = (next == '"') ? true : false;


        do {
            crr = next;
            try {
                next = br.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

            token += String.valueOf((char)crr);
            while (isConstString) {
                crr = next;
                token += String.valueOf((char)crr);
                isConstString = (next == '"') ? false: true;
                try {
                    next = br.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } while (!skipSet.contains(next) && !symbolSet.contains(crr)
                && !symbolSet.contains(next) && hasMoreTokens());

        while (skipSet.contains(next) && hasMoreTokens()) {
            try {
                this.next = br.read();
                if (this.next == '/') {
                    br.mark(1);
                    this.next = br.read();
                    br.reset();
                    if (next == '/') {
                        while (next != '\n' && next != -1)
                            this.next = br.read();
                    } else if (next == '*') {
                        while (next != '/')
                            this.next = br.read();
                        this.next = br.read();
                    } else {
                        next = '/';
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int tokenType() {
        if (keywordMap.containsKey(token)) return KEYWORD;
        else if (symbolSet.contains((int)token.charAt(0))) return SYMBOL;
        else if (token.charAt(0) == '"') return STRING_CONST;
        else {
            try {
                Integer.parseInt(token);
                return INT_CONST;
            } catch (NumberFormatException e) {
                return IDENTIFIER;
            }
        }
    }

    public int keyWord() {
        if (tokenType() != KEYWORD) return -1;
        return keywordMap.get(token);
    }

    public String symbol() {
        if (tokenType() != SYMBOL) return null;
        return token;
    }

    public String identifier() {
        return token;
    }

    public int intVal() {
        return Integer.parseInt(token);
    }

    public String stringVal() {
        return token.replaceAll("\"", "");
    }
}
