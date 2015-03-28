
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Janis
 */
public class FindSpellingErrorTest {
 
    private void execFSE(String a, String b, String expected) {
        String[] result = SpellCorrector.findSpellingError(a, b);
        assertEquals(expected, result[0] + "|" + result[1]);
    }
    
    @Test
    public void deletionTest() {
        execFSE("hme", "home", "h|ho");
    }
    
    @Test
    public void insertionTest() {
        execFSE("houme", "home", "ou|o");
    }
    
    @Test
    public void transpositionTest() {
        execFSE("hone", "home", "n|m");
    }
}
