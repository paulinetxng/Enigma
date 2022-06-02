package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Pauline Tang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        String line = _input.nextLine();
        if (!line.contains("*")) {
            throw new EnigmaException("No settings inputted");
        }
        while (_input.hasNextLine()) {
            setUp(m, line);
            line = _input.nextLine();
            while (!line.startsWith("*")) {
                String mes = m.convert(line.replaceAll("\\s", ""));
                printMessageLine(mes);
                _output.println();
                if (_input.hasNextLine()) {
                    line = _input.nextLine();
                } else {
                    break;
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();

            ArrayList<Rotor> allR = new ArrayList<Rotor>();

            while (_config.hasNext()) {
                if (_config.hasNext("[\\(\\)]")) {
                    throw new EnigmaException("bad config");
                }
                Rotor r = readRotor();
                allR.add(r);
            }
            return new Machine(_alphabet, numRotors, pawls, allR);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String nameRotor = _config.next();
            if (nameRotor.contains("(") || nameRotor.contains(")")) {
                throw new EnigmaException("bad config");
            }
            String typeRotor = _config.next();
            String perm = "";

            while (_config.hasNext("\\(.*\\)")) {
                perm += _config.next();
            }

            Permutation permutate = new Permutation(perm, _alphabet);

            if (typeRotor.charAt(0) == 'M') {
                String notches = "";
                for (int i = 1; i < typeRotor.length(); i++) {
                    notches += typeRotor.charAt(i);
                }
                return new MovingRotor(nameRotor, permutate, notches);
            } else if (typeRotor.charAt(0) == 'R') {
                return new Reflector(nameRotor, permutate);
            } else {
                return new FixedRotor(nameRotor, permutate);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner sets = new Scanner(settings);

        String[] test = settings.split("\\s");

        for (int t = 1; t < test.length - 1; t++) {
            for (int j = t + 1; j < test.length - 1; j++) {
                if (test[t].equals(test[j])) {
                    throw new EnigmaException("duplicate rotors");
                }
            }
        }

        if (test.length < M.numRotors() + 1) {
            throw new EnigmaException("Not enough rotors");
        }

        String[] rotorArray = new String[M.numRotors()];
        if (!settings.contains("*")) {
            throw new EnigmaException("Invalid settings format");
        }

        sets.next();
        for (int i = 0; i < M.numRotors(); i++) {
            rotorArray[i] = sets.next();
        }


        String setting = sets.next();
        if (setting.length() != M.numRotors() - 1) {
            throw new EnigmaException("wrong amount of settings");
        }

        M.insertRotors(rotorArray);
        if (M.numMove() > M.numPawls()) {
            throw new EnigmaException("incorrect args");
        }

        M.setRotors(setting);

        String plug = "";
        while (sets.hasNext()) {
            plug += sets.next();
        }
        Permutation plugbd = new Permutation(plug, _alphabet);
        M.setPlugboard(plugbd);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            _output.print(msg.charAt(i));
            if ((i + 1) % 5 == 0) {
                _output.print(' ');
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Number of Moving rotors. */
    private int numMov;
}
