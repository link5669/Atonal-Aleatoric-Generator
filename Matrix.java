import javax.sound.midi.*;

public class Matrix {
    private static int[] row = new int[12];
    private static int[][] matrix = new int[12][12];

    public static void main(String[] args) {
        generateRow();
        transposeRowToZero();
        copyPZeroToMatrix();
        copyInvertedRow();
        fillMatrix();
        printMatrix();
        generateMeasures();
    }

    private static void generateMeasures() {
        int measures = (int)(Math.random()*(36-5+1)+5);
        System.out.println(measures + " measures of music");
        for (int i = 0; i < measures; i++) {
            int row = (int)(Math.random()*(11));
            makeMusic(row);
        }
    }

    private static void fillMatrix() {
        for (int i = 1; i < 12; i++) {
            int diff = matrix[i][0];
            for (int j = 1; j < 12; j++) {
                matrix[i][j] = matrix[0][j];
                matrix[i][j] += diff;
                matrix[i][j] = mod(matrix[i][j]);
            }
        }
    }

    private static void transposeRowToZero() {
        if (row[0] != 0) {
            int diff = row[0];
            for (int i = 0; i< 12; i++) {
                row[i] = row[i] - diff;
                row[i] = mod(row[i]);
            }
        }
    }

    private static void copyInvertedRow() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                matrix[i][0] = invert(row[i]);
            }
        }
    }

    private static void copyPZeroToMatrix() {
        for (int i = 0; i < 12; i++) {
            matrix[0][i] = row[i];
        }
    }

    private static void generateRow() {
        int min = 0;
        int max = 11;
        for (int i = 0; i < 12; i++) {
            boolean repeat = true;
            while (repeat) {
                repeat = false;
                row[i] = (int)(Math.random()*(max-min+1)+min);
                for (int j = 0; j < 12; j++) {
                    if (j == i) {
                        break;
                    }
                    if (row[i] == row[j]) {
                        repeat = true;
                        break;
                    }
                }
            }
        }
    }

    private static void makeMusic(int row) {
        int numOfNotes = 12;
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Sequence sequence = new Sequence(Sequence.PPQ, 4);
            Track track = sequence.createTrack();
            for (int i = 0; i < 12; i++) {
                int octave = (int)(Math.random()*(3)+1);
                int rowType = (int)(Math.random()*(2));
                if (octave == 3) {
                    if (rowType == 1) {
                        track.add(makeEvent(144, 1, matrix[row][i] + 50 + 24, 100, i));
                    } else {
                        track.add(makeEvent(144, 1, matrix[i][row] + 50 + 24, 100, i));
                    }
                } else if (octave == 2) {
                    if (rowType == 1) {
                        track.add(makeEvent(144, 1, matrix[row][i] + 50 + 12, 100, i));
                    } else {
                        track.add(makeEvent(144, 1, matrix[i][row] + 50 + 12, 100, i));
                    }
                } else {
                    if (rowType == 1) {
                        track.add(makeEvent(144, 1, matrix[row][i] + 50, 100, i));
                    } else {
                        track.add(makeEvent(144, 1, matrix[i][row] + 50, 100, i));
                    }
                }
                track.add(makeEvent(128, 1, i, 100, i + 4));
            }
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(60);
            sequencer.start();
            while (true) {
                if (!sequencer.isRunning()) {
                    sequencer.close();
                    return;
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static MidiEvent makeEvent(int command, int channel, int note, int velocity, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(command, channel, note, velocity);
            event = new MidiEvent(a, tick);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return event;
    }

    private static int mod(int num) {
        if (num > 11) {
            num -= 12;
        } else if (num < 0) {
            num += 12;
        }
        return num;
    }


    private static int invert(int num) {
        if (num == 0) {
            return 0;
        } else if (num <= 6) {
            return 6 + (6 - num);
        } else {
            return 6 - (num - 6);
        }
    }

    private static void printMatrix() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (matrix[i][j] == 10) {
                    System.out.print("t ");
                    continue;
                } else if (matrix[i][j] == 11) {
                    System.out.print("e ");
                    continue;
                }
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
