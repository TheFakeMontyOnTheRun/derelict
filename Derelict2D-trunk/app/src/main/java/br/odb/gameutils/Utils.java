package br.odb.gameutils;

public class Utils {

    public static final int SECOND_IN_MILISSECONDS = 1000;
    public static final int MINUTE_IN_MILISSECONDS = 60 * SECOND_IN_MILISSECONDS;

    private Utils() {
    }

    public static int clamp(int n, int min, int max) {

        if (min > max)
            return clamp(n, max, min);

        if (n < min)
            n = min;

        if (n > max)
            n = max;

        return n;
    }

    public static float clamp(float n, float min, float max) {

        if (min > max)
            return clamp(n, max, min);

        if (n < min)
            n = min;

        if (n > max)
            n = max;

        return n;
    }
}
