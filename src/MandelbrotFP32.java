public class MandelbrotFP32 extends Mandelbrot {

    protected float glx, gty;
    protected float gw, gh;

    public MandelbrotFP32(
            final int maxIterations,
            final float infinity,
            final float glx, // graph left x
            final float gty, // graph top y
            final float gw, // graph width
            final float gh, // graph height
            final int iw, // image width
            final int ih //image height
    ) {
        super(maxIterations, infinity, iw, ih);
        this.glx = glx;
        this.gty = gty;
        this.gw = gw;
        this.gh = gh;
    }

    public void run() {
        final int index = getGlobalId();

        final float iA = ix2ga(index % iw);
        final float iB = iy2gb(index / iw);

        float color = 1 / exp(sqrt((iA * iA) + (iB * iB)));

        float a = iA;
        float b = iB;

        float mag, lA;
        for (int i = 0; i < maxIterations; i++) {
            lA = a;
            a = (a * a) - (b * b);
            b = 2 * lA * b;
            a += iA;
            b += iB;

            mag = sqrt((a * a) + (b * b));
            color += 1 / exp(mag);

            if (mag > infinity) {
                data[index] = hsb2rgb((color * 10) / maxIterations, 1f, 1f);
                return;
            }
        }
        data[index] = 0;
    }

    // map image x coordinate to graph real coordinate
    protected float ix2ga(final int x) {
        return glx + ((x / (float) iw) * gw);
    }

    // map image y coordinate to graph imaginary coordinate
    protected float iy2gb(final int y) {
        return gty + ((y / (float) ih) * gh);
    }

    public void setPixelBounds(final int x, final int y, final int w, final int h) {
        glx = ix2ga(x);
        gty = iy2gb(y);
        gw = ix2ga(w);
        gh = iy2gb(h);
    }

    public void setGraphBounds(final double x, final double y, final double w, final double h) {
        glx = (float) x;
        gty = (float) y;
        gw = (float) w;
        gh = (float) h;
    }
}
