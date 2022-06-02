package enigma;
import static enigma.EnigmaException.*;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Pauline Tang
 */
class Alphabet {

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        char[] charArray = chars.toCharArray();
        for (int k = 0; k < charArray.length - 1; k++) {
            int occur = 0;
            char character = charArray[k];
            for (char c: charArray) {
                if (c == character) {
                    occur += 1;
                }
                if (occur >= 2) {
                    throw new EnigmaException("Duplicate character detected");
                }
            }
        }
        _chars = chars;
        _size = chars.length();
    }

    /** characters. */
    private String _chars;

    /** size. */
    private int _size;

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _size;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _chars.indexOf(ch) != -1;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return _chars.indexOf(ch);
    }

}
