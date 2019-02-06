import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TestParser {
    @Test
    public void testAdd() {
        Parser p = new Parser("./testFiles/SimpleAdd.vm");
        p.advance();
        assertEquals(p.commandType(), Parser.C_PUSH);
        assertEquals(p.arg1(), "constant");
        assertEquals(p.arg2(), "7");
        p.advance();
        assertEquals(p.commandType(), Parser.C_PUSH);
        assertEquals(p.arg1(), "constant");
        assertEquals(p.arg2(), "8");
        p.advance();
        assertEquals(p.commandType(), Parser.C_ARITHMETIC);
        assertEquals(p.arg1(), "add");
        assertTrue(!p.hasMoreCommands());
        p.advance();
    }
}
