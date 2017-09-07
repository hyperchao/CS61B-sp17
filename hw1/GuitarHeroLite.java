import edu.princeton.cs.introcs.StdAudio;
import synthesizer.GuitarString;
import static java.lang.System.out;

/** A client that uses the synthesizer package to replicate a plucked guitar string sound */
public class GuitarHeroLite {
    private static final double CONCERT_A = 440.0;
    private static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);

//    public static void main(String[] args) {
//        /* create two guitar strings, for concert A and C */
//        synthesizer.GuitarString stringA = new synthesizer.GuitarString(CONCERT_A);
//        synthesizer.GuitarString stringC = new synthesizer.GuitarString(CONCERT_C);
//
//        while (true) {
//
//            /* check if the user has typed a key; if so, process it */
//            if (StdDraw.hasNextKeyTyped()) {
//                char key = StdDraw.nextKeyTyped();
//                if (key == 'a') {
//                    stringA.pluck();
//                } else if (key == 'c') {
//                    stringC.pluck();
//                }
//            }
//
//        /* compute the superposition of samples */
//            double sample = stringA.sample() + stringC.sample();
//
//        /* play the sample on standard audio */
//            StdAudio.play(sample);
//
//        /* advance the simulation of each guitar string by one step */
//            stringA.tic();
//            stringC.tic();
//        }
//    }

    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        final int keyNum = 37;
        GuitarString[] strings = new GuitarString[keyNum];
        double sample = 0;

        for (int i = 0; i < keyNum; i++) {
            double frequency = 440 * Math.pow(2, (i - 24) / 12.0);
            strings[i] = new GuitarString(frequency);
        }

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index > 0) {
                    strings[index].pluck();
                }
            }

            sample = 0;
            for (GuitarString o : strings) {
                sample += o.sample();
            }

            StdAudio.play(sample);

            for (GuitarString o : strings) {
                o.tic();
            }
        }
    }

}

