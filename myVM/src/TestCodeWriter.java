import org.junit.jupiter.api.Test;

public class TestCodeWriter {
    @Test
    public void TestWriteIntoFile() {
        CodeWriter cw = new CodeWriter("./testFiles/test.asm");
        cw.writeArithmetic("gt");
        cw.writePushPop(Parser.C_POP, "local", "12");
        cw.close();
    }
}
