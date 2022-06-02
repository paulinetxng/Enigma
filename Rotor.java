package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Pauline Tang
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        if (!alphabet().contains(alphabet().toChar(posn))) {
            throw new EnigmaException("Setting out of bounds");
        }
        _setting = (posn % alphabet().size());
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        if (!alphabet().contains(cposn)) {
            throw new EnigmaException("Setting out of bounds");
        }
        _setting = alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation.
     *
     *  convertForward should output the character passing through the
     *  entire rotor from front to back, so entering and then exiting*/
    int convertForward(int p) {
        int contact = (p + this._setting) % size();
        int contactExited = _permutation.permute(contact);
        int positionExited = (contactExited - this._setting) % size();
        if (positionExited < 0) {
            positionExited += size();
        }
        return positionExited;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int contact = (e + _setting) % size();
        int contactExited = _permutation.invert(contact);
        int positionExited = (contactExited - _setting) % size();
        if (positionExited < 0) {
            positionExited += size();
        }
        return positionExited;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** Instantiate setting to 0 at beginning. */
    private int _setting;
}
