import javax.sound.midi.*;

public class Matrix {
    private static int[] row = new int[12];
    private static int[][] matrix = new int[12][12];

    public static void main(String[] args) {
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
        if (row[0] != 0) {
            int diff = row[0];
            for (int i = 0; i< 12; i++) {
                row[i] = row[i] - diff;
                row[i] = mod(row[i]);
            }
        }
        for (int i = 0; i < 12; i++) {
            matrix[0][i] = row[i];
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                matrix[i][0] = invert(row[i]);
            }
        }
        for (int i = 1; i < 12; i++) {
           int diff = matrix[i][0];
           for (int j = 1; j < 12; j++) {
               matrix[i][j] = matrix[0][j];
               matrix[i][j] += diff;
               matrix[i][j] = mod(matrix[i][j]);
           }
        }
        printMatrix();
        makeMusic(0);
        makeMusic(1);

    }

    private static void makeMusic(int row) {
        int numOfNotes = 12;
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Sequence sequence = new Sequence(Sequence.PPQ, 4);
            Track track = sequence.createTrack();
            for (int i = 0; i < 12; i++) {
                track.add(makeEvent(144, 1, matrix[row][i] + 50, 100, i));
                track.add(makeEvent(128, 1, i, 100, i + 2));
            }
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(60);
            sequencer.start();
            while (true) {
                if (!sequencer.isRunning()) {
                    sequencer.close();
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
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
