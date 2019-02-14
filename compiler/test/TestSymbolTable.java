import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TestSymbolTable {
    @Test
    public void Test() {
        SymbolTable table = new SymbolTable();

        table.define("ayaka", "int", SymbolTable.VAR);
        table.define("yutaka", "int", SymbolTable.VAR);
        table.define("hoge", "String", SymbolTable.FIELD);

        assertEquals("int", table.typeOf("yutaka"));
        assertEquals(SymbolTable.VAR, table.kindOf("ayaka"));
        assertEquals(SymbolTable.NONE, table.kindOf("aya"));
        assertEquals(1, table.indexOf("yutaka"));
        assertEquals(2, table.varCount(SymbolTable.VAR));
        assertEquals(0, table.varCount(SymbolTable.STATIC));
    }
}
