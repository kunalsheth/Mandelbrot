import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.Device;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.stream.IntStream;

/**
 * Created by the-magical-llamicorn on 4/22/17.
 */
public class MandelbrotFP64 extends Kernel implements Mandelbrot {

    public final int maxIterations;
    public final double infinity;
    public final double glx, gty;
    public final double gw, gh;
    public final int iw, ih;

    final int[] data;

    public MandelbrotFP64(
            final int maxIterations,
            final double infinity,
            final double glx, // graph left x
            final double gty, // graph top y
            final double gw, // graph width
            final double gh, // graph height
            final int iw, // image width
            final int ih //image height
    ) {
        this.maxIterations = maxIterations;
        this.infinity = infinity;
        this.glx = glx;
        this.gty = gty;
        this.gw = gw;
        this.gh = gh;
        this.iw = iw;
        this.ih = ih;

        data = new int[iw * ih];

        System.out.println("Device Type: " + getTargetDevice().getType());
    }

    public RenderedImage render() {
        execute(iw*ih);
        final BufferedImage image = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
        IntStream.range(0, iw).parallel().forEach(x -> IntStream.range(0, ih).forEach(y ->
                image.setRGB(x, y, data[x + (y * iw)])
        ));
        return image;
    }

    public void run() {
        final int index = getGlobalId();

        final double iA = ix2ga(index % iw);
        final double iB = iy2gb(index / iw);

        double color = 1 / exp(sqrt((iA * iA) + (iB * iB)));

        double a = iA;
        double b = iB;

        double mag, lA;
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

    // adapted from Color.HSBtoRGB() for Aparapi
    protected int hsb2rgb(final double hue, final double saturation, final double brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = (int) (brightness * 255.0f + 0.5f);
            g = r;
            b = g;
        } else {
            final double h = (hue - floor(hue)) * 6.0f;
            final double f = h - floor(h);
            final double p = brightness * (1.0f - saturation);
            final double q = brightness * (1.0f - saturation * f);
            final double t = brightness * (1.0f - (saturation * (1.0f - f)));
            final int hInt = (int) h;
            if (hInt == 0) {
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (t * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
            } else if (hInt == 1) {
                r = (int) (q * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
            } else if (hInt == 2) {
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (t * 255.0f + 0.5f);
            } else if (hInt == 3) {
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (q * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
            } else if (hInt == 4) {
                r = (int) (t * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
            } else if (hInt == 5) {
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (q * 255.0f + 0.5f);
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    // map image x coordinate to graph real coordinate
    protected double ix2ga(final int x) {
        return glx + ((x / (double) iw) * gw);
    }

    // map image y coordinate to graph imaginary coordinate
    protected double iy2gb(final int y) {
        return gty + ((y / (double) ih) * gh);
    }

    // map graph real coordinate to image x coordinate
    protected int ga2ix(final double a) {
        return (int) round(((a - glx) * iw) / gw);
    }

    // map graph imaginary coordinate to image y coordinate
    protected int gb2iy(final double b) {
        return (int) round(((b - gty) * ih) / gh);
    }
}
