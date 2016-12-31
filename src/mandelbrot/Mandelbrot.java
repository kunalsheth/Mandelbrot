package mandelbrot;

/**
 * Copyright 2016 Kunal Sheth
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Iterates the function

public class Mandelbrot {

    public static double[] initState(final double ca, final double cb) {
        return new double[]{ca, cb, ca, cb};
    }

    public static double currentMagnitude(final double[] state) {
        return abs(state);
    }

    public static double nextMagnitude(final double[] state) {
        square(state);
        plusC(state);
        return abs(state);
    }

    private static void square(final double[] state) {
        final double a = state[0], b = state[1];
        state[0] = (a * a) - (b * b);
        state[1] = 2 * a * b;
    }

    private static void plusC(final double[] state) {
        state[0] += state[2];
        state[1] += state[3];
    }

    private static double abs(final double[] state) {
        final double a = state[0], b = state[1];
        return Math.sqrt((a * a) + (b * b));
    }
}