package enigma;

import java.util.ArrayList;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Pauline Tang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _numMove = 0;
        _allRotors = new ArrayList<Rotor>();
        for (Rotor r : allRotors) {
            _allRotors.add(r);
        }
        _myRotors = new Rotor[_numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < _numRotors; i++) {
            for (int k = 0; k < _allRotors.size(); k++) {
                if (rotors[i].equals((_allRotors.get(k).name()))) {
                    _myRotors[i] = _allRotors.get(k);
                }
            }
        }

        for (int i = 0; i < _myRotors.length; i++) {
            if (_myRotors[i] == null) {
                throw new EnigmaException("bad rotor name");
            }
        }

        for (Rotor r : _myRotors) {
            if (r instanceof MovingRotor) {
                _numMove += 1;
            }
        }

        if (!_myRotors[0].reflecting()) {
            throw new EnigmaException("No Reflector in position");
        }

        if (!_myRotors[_numRotors - 1].rotates()) {
            throw new EnigmaException("Right-most rotor is not rotating");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numRotors - 1) {
            throw new EnigmaException("Wrong amount of settings inputted");
        }
        for (int i = 0; i < setting.length(); i++) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw new EnigmaException("Setting not in alphabet");
            }
            _myRotors[i + 1].set(setting.charAt(i));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] needsAdv = new boolean[_numRotors];
        needsAdv[_numRotors - 1] = true;

        for (int i = _numRotors - 2; i > 0; i--) {
            needsAdv[i] = (_myRotors[i + 1].atNotch()
                    || (_myRotors[i - 1].rotates() && _myRotors[i].atNotch()));
        }

        for (int i = 1; i < _numRotors; i++) {
            if (needsAdv[i]) {
                _myRotors[i].advance();
            }
        }

        int result = c;
        if (_plugboard != null) {
            result = _plugboard.permute(c);
        }
        for (int i = _numRotors - 1; i >= 0; i--) {
            result = _myRotors[i].convertForward(result);
        }
        for (int i = 1; i < _numRotors; i++) {
            result = _myRotors[i].convertBackward(result);
        }
        if (_plugboard != null) {
            result = _plugboard.invert(result);
        }
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == ' ') {
                result += ' ';
            } else {
                char c = msg.charAt(i);
                int ind = _alphabet.toInt(c);
                int conv = convert(ind);
                result += _alphabet.toChar(conv);
            }
        }
        return result;
    }

    /** my rotors.
     * @return _myRotors */
    Rotor[] myRotors() {
        return _myRotors;
    }

    /** Num of Moving Rotors.
     * @return _numMove */
    int numMove() {
        return _numMove;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Num of rotors and pawls in the Machine. */
    private int _numRotors, _pawls;

    /** Data structure of all the rotors in the Collection. */
    private ArrayList<Rotor> _allRotors;

    /** Data structure of all my rotors in the Machine. */
    private Rotor[] _myRotors;

    /** Machine's plugboard. */
    private Permutation _plugboard;

    /** Number of Moving Rotors. */
    private int _numMove;
}
