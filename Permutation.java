package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Pauline Tang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        cycles = cycles.replaceAll("[(]", "");
        _cycles = cycles.split("\\)[\\s]*");
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char charOfP = _alphabet.toChar(wrap(p));
        char newChar = ' ';
        int indOfPerm = 0;
        for (int cyc = 0; cyc < _cycles.length; cyc++) {
            int size = _cycles[cyc].length();
            for (int i = 0; i < size; i++) {
                if (_cycles[cyc].charAt(i) == charOfP) {
                    indOfPerm = (i + 1) % size;
                    newChar = _cycles[cyc].charAt(indOfPerm);
                    return _alphabet.toInt(newChar);
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char charOfP = _alphabet.toChar(wrap(c));
        char newChar = ' ';
        int indOfPerm = 0;
        for (int cyc = 0; cyc < _cycles.length; cyc++) {
            int size = _cycles[cyc].length();
            for (int i = size - 1; i >= 0; i--) {
                if (_cycles[cyc].charAt(i) == charOfP) {
                    indOfPerm = (i - 1) % size;
                    if (indOfPerm < 0) {
                        indOfPerm += size;
                    }
                    newChar = _cycles[cyc].charAt(indOfPerm);
                    return _alphabet.toInt(newChar);
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int i = _alphabet.toInt(p);
        int permInd = permute(i);
        return _alphabet.toChar(permInd);
    }


    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int i = _alphabet.toInt(c);
        int invInd = invert(i);
        return _alphabet.toChar(invInd);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < size(); i++) {
            if (permute(i) == i) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** String array of cycles. */
    private String[] _cycles;
}
