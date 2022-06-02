package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Pauline Tang
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        _setting = 0;
    }

    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    int convertForward(int p) {
        return this.permutation().permute(p);
    }

    @Override
    boolean atNotch() {
        return false;
    }


    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    /** setting. */
    private int _setting;
}
