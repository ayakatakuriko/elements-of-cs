import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TestParser {
    @Test
    void testHasMoreCommands() {
        Parser p;
        p = new Parser("./test_files/Max.asm");
        p.advance();
        // crr=@R0
        assertEquals("R0", p.symbol());
        assertTrue(p.hasMoreCommands());
        assertEquals(100, p.commandType());
        p.advance();
        // crr=D=M
        assertEquals("D", p.dest());
        assertEquals("M", p.comp());
        assertEquals(null, p.jump());
        p.advance();
        p.advance();
        // crr = D=D-M
        assertEquals("D-M", p.comp());
        p.advance();
        p.advance();
        // crr=D;JGT
        assertEquals("JGT", p.jump());
        assertEquals("D", p.comp());
        p.advance();
        p.advance();
        p.advance();
        p.advance();
        p.advance();
        // crr = (OUTPUT_FIRST)
        assertEquals("OUTPUT_FIRST", p.symbol());
    }
}
