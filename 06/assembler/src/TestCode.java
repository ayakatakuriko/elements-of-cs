import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TestCode {
    @Test
    public void TestConvert() {
        Code c = new Code();

        assertEquals("0101010", c.comp("0"));
    }
}
