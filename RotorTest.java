package enigma;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RotorTest {

    @Test
    public void testRotorSetting() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        Rotor r = new Rotor("Rotor I", p);
        assertEquals(0, r.setting());
        r.set(3);
        assertEquals(3, r.setting());
    }

    @Test (expected = EnigmaException.class)
    public void testRotorSettingInvalid() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        Rotor r = new Rotor("I", p);
        r.set(4);
    }

    @Test
    public void testRotorConvertF() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        Rotor r = new Rotor("I", p);
        assertEquals(0, r.convertForward(1));

        Alphabet c = new Alphabet("ABCDEFG54321");
        Permutation s = new Permutation("(ABC1) (D43) (EFG2) (5)", c);
        Rotor r2 = new Rotor("Rotor II", s);
        r2.set(2);
        assertEquals(2, r2.setting());
        assertEquals(10, r2.convertForward(9));
    }

    @Test
    public void testRotorConvertB() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        Rotor r = new Rotor("I", p);
        assertEquals(1, r.convertBackward(0));

        Alphabet c = new Alphabet("ABCDEFG54321");
        Permutation s = new Permutation("(ABC1) (D43) (EFG2) (5)", c);
        Rotor r2 = new Rotor("Rotor II", s);
        r2.set(2);
        assertEquals(2, r2.setting());
        assertEquals(9, r2.convertBackward(10));
    }

    @Test
    public void testFixedRotor() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        Rotor fR = new FixedRotor("fR", p);
        fR.set(2);
        assertEquals(2, fR.setting());
        fR.set(1);
        assertEquals(2, fR.setting());
    }

    @Test
    public void testMovingRotor() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        String notches = "C";
        Rotor mR = new MovingRotor("mR", p, notches);
        assertEquals(0, mR.setting());
        mR.advance();
        assertEquals(1, mR.setting());
        mR.advance();
        assertEquals(2, mR.setting());
        assertTrue(mR.atNotch());
    }








}
