import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TestCompilationEngine {
    @Test
    public void testToknizer() {
        File input = new File("./testFiles/testCode1.jack");
        File output = new File("./testFiles/testCode1.xml");
        CompilationEngine ce = new CompilationEngine(input, output);

        ce.compileClass();
        ce.closeOutput();
    }

    @Test
    public void testArrayTest() {
        File input = new File("./testFiles/ArrayTest/Main.jack");
        File output = new File("./testFiles/ArrayTest/myMain.xml");
        CompilationEngine ce = new CompilationEngine(input, output);

        ce.compileClass();
        ce.closeOutput();
    }

    @Test
    public void testSquare() {
        File input = new File("./testFiles/Square/Main.jack");
        File output = new File("./testFiles/Square/myMain.xml");
        CompilationEngine ce1 = new CompilationEngine(input, output);

        ce1.compileClass();
        ce1.closeOutput();

        input = new File("./testFiles/Square/Square.jack");
        output = new File("./testFiles/Square/mySquare.xml");
        CompilationEngine ce2 = new CompilationEngine(input, output);

        ce2.compileClass();
        ce2.closeOutput();

        input = new File("./testFiles/Square/SquareGame.jack");
        output = new File("./testFiles/Square/mySquareGame.xml");
        CompilationEngine ce3 = new CompilationEngine(input, output);

        ce3.compileClass();
        ce3.closeOutput();
    }
}
