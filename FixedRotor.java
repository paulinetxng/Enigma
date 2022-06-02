package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Pauline Tang
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        _setting = 0;
        _setOnce = false;
        _perm = perm;
    }

    @Override
    int setting() {
        return _setting;
    }

    @Override
    void set(int posn) {
        if (!alphabet().contains(alphabet().toChar(posn))) {
            throw new EnigmaException("Setting out of bounds");
        }
        if (!_setOnce) {
            _setting = posn;
            _setOnce = true;
        }
    }

    @Override
    void set(char cposn) {
        if (!alphabet().contains(cposn)) {
            throw new EnigmaException("Setting out of bounds");
        }
        if (!_setOnce) {
            _setting = alphabet().toInt(cposn);
            _setOnce = true;
        }
    }

    @Override
    int convertForward(int p) {
        int contact = (p + this._setting) % size();
        int contactExited = _perm.permute(contact);
        int positionExited = (contactExited - this._setting) % size();
        if (positionExited < 0) {
            positionExited += size();
        }
        return positionExited;
    }

    @Override
    int convertBackward(int e) {
        int contact = (e + _setting) % size();
        int contactExited = _perm.invert(contact);
        int positionExited = (contactExited - _setting) % size();
        if (positionExited < 0) {
            positionExited += size();
        }
        return positionExited;
    }

    /** setting. */
    private int _setting;

    /** set one boolean. */
    private boolean _setOnce;

    /** permutation. */
    private Permutation _perm;
}
