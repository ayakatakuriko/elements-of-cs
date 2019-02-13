import java.util.LinkedList;

public class SymbolTable {
    public final int STATIC = 300;
    public final int FIELD = 301;
    public final int ARG = 302;
    public final int VAR = 303;
    public final int NONE = 304;
    private LinkedList<Symbol> classTable;
    private LinkedList<Symbol> subroutineTable;
    /* Current index */
    private int staticIndex;
    private int fieldIndex;
    private int argIndex;
    private int varIndex;

    private class Symbol {
        private String name;
        private String type;
        private int kind;
        private int index;

        public Symbol(String name, String type, int kind, int index) {
            this.name = name;
            this.type = type;
            this.kind = kind;
            this.index = index;
        }
    }

    public SymbolTable() {
        classTable = new LinkedList<>();
        subroutineTable = new LinkedList<>();
        staticIndex = -1;
        fieldIndex = -1;
        argIndex = -1;
        varIndex = -1;
    }

    public void startSubroutine() {
        subroutineTable = new LinkedList<>();
        argIndex = -1;
        varIndex = -1;
    }

    public void define(String name, String type, int kind) {
        if (kind == STATIC || kind == FIELD) {
            classTable.add(new Symbol(name, type, kind, incrementIndex(kind)));
        } else if (kind == ARG || kind == VAR) {
            subroutineTable.add(new Symbol(name, type, kind, incrementIndex(kind)));
        }
    }

    private int incrementIndex(int kind) {
        switch (kind) {
            case STATIC:
                return ++staticIndex;
            case FIELD:
                return ++fieldIndex;
            case ARG:
                return ++argIndex;
            case VAR:
                return ++varIndex;
        }
        return -1;
    }

    public int varCount(int kind) {
        switch (kind) {
            case STATIC:
                return staticIndex;
            case FIELD:
                return fieldIndex;
            case ARG:
                return argIndex;
            case VAR:
                return varIndex;
        }
        return -1;
    }

    public int kindOf(String name) {
        for (Symbol s: classTable) {
            if (s.name.equals(name)) return s.kind;
        }
        for (Symbol s: subroutineTable) {
            if (s.name.equals(name)) return s.kind;
        }
        return NONE;
    }

    public String typeOf(String name) {
        for (Symbol s: classTable) {
            if (s.name.equals(name)) return s.type;
        }
        for (Symbol s: subroutineTable) {
            if (s.name.equals(name)) return s.type;
        }
        return null;
    }

    public int indexOf(String name) {
        for (Symbol s: classTable) {
            if (s.name.equals(name)) return s.index;
        }
        for (Symbol s: subroutineTable) {
            if (s.name.equals(name)) return s.index;
        }
        return -1;
    }
}
