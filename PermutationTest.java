package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Pauline Tang
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testSize() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        assertEquals(4, p.size());

        Alphabet b = new Alphabet("ABC123");
        Permutation q = new Permutation("(A1) (B2) (C3)", b);
        assertEquals(6, q.size());
    }

    @Test
    public void testPermuteInt() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        assertEquals(1, p.permute(3));
        assertEquals(2, p.permute(0));

        Alphabet b = new Alphabet("ABC123");
        Permutation q = new Permutation("(A1) (B2) (C3)", b);
        assertEquals(3, q.permute(0));
        assertEquals(1, q.permute(4));

        Alphabet c = new Alphabet("ABCDEFG54321");
        Permutation r = new Permutation("(ABC1) (D43) (EFG25)", c);
        assertEquals(9, r.permute(8));
        assertEquals(1, r.permute(0));
        assertEquals(4, r.permute(7));
        assertEquals(0, r.permute(11));

        Permutation s = new Permutation("(ABC1) (D43) (EFG2) (5)", c);
        assertEquals(7, s.permute(7));
        Permutation t = new Permutation("(ABC1) (D43) (5) (EFG2)", c);
        assertEquals(7, t.permute(7));
        Permutation u = new Permutation("(5) (ABC1) (D43) (EFG2)", c);
        assertEquals(7, u.permute(7));

        Alphabet d = new Alphabet("ABCDEFG");
        Permutation v = new Permutation("(ABC) (DE) (F)", d);
        assertEquals(6, v.permute(6));
    }

    @Test
    public void testInvertInt() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        assertEquals(3, p.invert(1));
        assertEquals(0, p.invert(2));

        Alphabet b = new Alphabet("ABC123");
        Permutation q = new Permutation("(A1) (B2) (C3)", b);
        assertEquals(0, q.invert(3));
        assertEquals(4, q.invert(1));

        Alphabet c = new Alphabet("ABCDEFG54321");
        Permutation r = new Permutation("(ABC1) (D43) (EFG25)", c);
        assertEquals(8, r.invert(9));
        assertEquals(0, r.invert(1));
        assertEquals(7, r.invert(4));
        assertEquals(11, r.invert(0));

        Permutation s = new Permutation("(ABC1) (D43) (EFG2) (5)", c);
        assertEquals(7, s.invert(7));
        Permutation t = new Permutation("(ABC1) (D43) (5) (EFG2)", c);
        assertEquals(7, t.invert(7));
        Permutation u = new Permutation("(5) (ABC1) (D43) (EFG2)", c);
        assertEquals(7, u.invert(7));

        Alphabet d = new Alphabet("ABCDEFG");
        Permutation v = new Permutation("(ABC) (DE) (F)", d);
        assertEquals(6, v.invert(6));
    }

    @Test
    public void testPermuteChar() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        assertEquals('A', p.permute('B'));
        assertEquals('B', p.permute('D'));

        Alphabet b = new Alphabet("ABC123");
        Permutation q = new Permutation("(A1) (B2) (C3)", b);
        assertEquals('1', q.permute('A'));
        assertEquals('A', q.permute('1'));
        assertEquals('C', q.permute('3'));

        Alphabet c = new Alphabet("ABCDEFG54321");
        Permutation r = new Permutation("(ABC1) (D43) (EFG25)", c);
        assertEquals('B', r.permute('A'));
        assertEquals('A', r.permute('1'));
        assertEquals('3', r.permute('4'));
        assertEquals('E', r.permute('5'));

        Permutation s = new Permutation("(ABC1) (D43) (EFG2) (5)", c);
        assertEquals('5', s.permute('5'));
        Permutation t = new Permutation("(ABC1) (D43) (5) (EFG2)", c);
        assertEquals('5', t.permute('5'));
        Permutation u = new Permutation("(5) (ABC1) (D43) (EFG2)", c);
        assertEquals('5', u.permute('5'));

        Alphabet d = new Alphabet("ABCDEFG");
        Permutation v = new Permutation("(ABC) (DE) (F)", d);
        assertEquals('G', v.permute('G'));
    }

    @Test
    public void testInvertChar() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));

        Alphabet b = new Alphabet("ABC123");
        Permutation q = new Permutation("(A1) (B2) (C3)", b);
        assertEquals('1', q.invert('A'));
        assertEquals('A', q.invert('1'));
        assertEquals('C', q.invert('3'));

        Alphabet c = new Alphabet("ABCDEFG54321");
        Permutation r = new Permutation("(ABC1) (D43) (EFG25)", c);
        assertEquals('1', r.invert('A'));
        assertEquals('4', r.invert('3'));
        assertEquals('5', r.invert('E'));
        assertEquals('F', r.invert('G'));

        Permutation s = new Permutation("(ABC1) (D43) (EFG2) (5)", c);
        assertEquals('5', s.invert('5'));
        Permutation t = new Permutation("(ABC1) (D43) (5) (EFG2)", c);
        assertEquals('5', t.invert('5'));
        Permutation u = new Permutation("(5) (ABC1) (D43) (EFG2)", c);
        assertEquals('5', u.invert('5'));

        Alphabet d = new Alphabet("ABCDEFG");
        Permutation v = new Permutation("(ABC) (DE) (F)", d);
        assertEquals('G', v.permute('G'));
    }

    @Test
    public void testDerangement() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD", a);
        assertTrue(p.derangement());

        Alphabet c = new Alphabet("ABCDEFG54321");
        Permutation s = new Permutation("(ABC1) (D43) (EFG2) (5)", c);
        assertFalse(s.derangement());

        Alphabet d = new Alphabet("ABCDEFG");
        Permutation v = new Permutation("(ABC) (DE) (F)", d);
        assertFalse(v.derangement());
    }

}
