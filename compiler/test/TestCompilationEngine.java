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
        File output = new File("./testFiles/ArrayTest/Main.vm");
        CompilationEngine ce = new CompilationEngine(input, output);

        ce.compileClass();
        ce.closeOutput();
    }

    @Test
    public void testSquare() {
        File input = new File("./testFiles/Square/Main.jack");
        File output = new File("./testFiles/mySquare/myMain.vm");
        CompilationEngine ce1 = new CompilationEngine(input, output);

        ce1.compileClass();
        ce1.closeOutput();

        input = new File("./testFiles/Square/Square.jack");
        output = new File("./testFiles/mySquare/mySquare.vm");
        CompilationEngine ce2 = new CompilationEngine(input, output);

        ce2.compileClass();
        ce2.closeOutput();

        input = new File("./testFiles/Square/SquareGame.jack");
        output = new File("./testFiles/mySquare/mySquareGame.vm");
        CompilationEngine ce3 = new CompilationEngine(input, output);

        ce3.compileClass();
        ce3.closeOutput();
    }

    @Test
    public void testExpressionLessSquare() {
        File input = new File("./testFiles/ExpressionLessSquare/Main.jack");
        File output = new File("./testFiles/myExpressionLessSquare/Main.xml");
        CompilationEngine ce1 = new CompilationEngine(input, output);

        ce1.compileClass();
        ce1.closeOutput();

        input = new File("./testFiles/ExpressionLessSquare/Square.jack");
        output = new File("./testFiles/myExpressionLessSquare/Square.xml");
        CompilationEngine ce2 = new CompilationEngine(input, output);

        ce2.compileClass();
        ce2.closeOutput();

        input = new File("./testFiles/ExpressionLessSquare/SquareGame.jack");
        output = new File("./testFiles/myExpressionLessSquare/SquareGame.xml");
        CompilationEngine ce3 = new CompilationEngine(input, output);

        ce3.compileClass();
        ce3.closeOutput();
    }
}
