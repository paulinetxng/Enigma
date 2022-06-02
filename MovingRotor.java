package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Pauline Tang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _setting = 0;
        _perm = perm;
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            char c = _notches.charAt(i);
            int ind = alphabet().toInt(c);
            if (this._setting == ind) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        _setting = (_setting + 1) % size();
    }

    @Override
    int setting() {
        return _setting;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void set(int posn) {
        if (!alphabet().contains(alphabet().toChar(posn))) {
            throw new EnigmaException("Setting out of bounds");
        }
        _setting = (posn % alphabet().size());
    }

    @Override
    void set(char cposn) {
        if (!alphabet().contains(cposn)) {
            throw new EnigmaException("Setting out of bounds");
        }
        _setting = alphabet().toInt(cposn);
    }

    @Override
    int convertForward(int p) {
        int contact = (p + _setting) % size();
        int contactExited = _perm.permute(contact);
        int positionExited = (contactExited - _setting) % size();
        if (positionExited < 0) {
            positionExited += size();
        }
        return positionExited;
    }

    @Override
    int convertBackward(int e) {
        int contact = (e + this._setting) % size();
        int contactExited = _perm.invert(contact);
        int positionExited = (contactExited - this._setting) % size();
        if (positionExited < 0) {
            positionExited += size();
        }
        return positionExited;
    }

    /** notches.
     * @return _notches string
     */
    String notches() {
        return _notches;
    }

    /** setting. */
    private int _setting;

    /** notches. */
    private String _notches;

    /** Permutation. */
    private Permutation _perm;

    /** Permutation.
     * @return _perm */
    Permutation permutation() {
        return _perm;
    }
}
