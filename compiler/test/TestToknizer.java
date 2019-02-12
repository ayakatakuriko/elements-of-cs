import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.io.File;


public class TestToknizer {
    @Test
    public void testHasMoreToken() {
        File input = new File("./testFiles/testCode1.jack");
        JackToknizer jt = new JackToknizer(input);

        assertTrue(jt.hasMoreTokens());
    }

    @Test
    public void testAdvance() {
        File input = new File("./testFiles/testCode1.jack");
        JackToknizer jt = new JackToknizer(input);

        jt.advance();
        assertEquals(JackToknizer.KEYWORD, jt.tokenType());
        assertEquals(JackToknizer.IF, jt.keyWord());

        jt.advance();
        assertEquals(JackToknizer.SYMBOL, jt.tokenType());
        assertEquals("(", jt.symbol());

        jt.advance();
        assertEquals(JackToknizer.IDENTIFIER, jt.tokenType());
        assertEquals("x", jt.identifier());

        jt.advance();
        assertEquals(JackToknizer.SYMBOL, jt.tokenType());
        assertEquals("<", jt.symbol());

        jt.advance();
        assertEquals(JackToknizer.INT_CONST, jt.tokenType());
        assertEquals(153, jt.intVal());

        jt.advance();
        jt.advance();
        jt.advance();
        assertEquals(JackToknizer.KEYWORD, jt.tokenType());
        assertEquals(JackToknizer.LET, jt.keyWord());

        jt.advance();
        jt.advance();
        jt.advance();
        assertEquals(JackToknizer.STRING_CONST, jt.tokenType());
        assertEquals("Paris", jt.stringVal());

        jt.advance();
        assertEquals(JackToknizer.SYMBOL, jt.tokenType());
        assertEquals(";", jt.symbol());
    }
}
