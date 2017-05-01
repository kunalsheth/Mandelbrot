import com.aparapi.Kernel;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.RenderedImage;

/**
 * Created by the-magical-llamicorn on 4/19/17.
 */
public abstract class Mandelbrot extends Kernel {

    protected final BufferedImage image;
    protected final int[] data;
    protected int maxIterations;
    protected float infinity;
    protected int iw, ih;
    protected int renderCounter = 0;

    protected Mandelbrot(final int maxIterations, final float infinity, final int iw, final int ih) {
        this.maxIterations = maxIterations;
        this.infinity = infinity;
        this.iw = iw;
        this.ih = ih;

        image = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
        data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    protected void setRenderer(final int maxIterations, final float infinity, final int iw, final int ih) {
        this.maxIterations = maxIterations;
        this.infinity = infinity;
        this.iw = iw;
        this.ih = ih;
    }

    public abstract void setPixelBounds(final int x, final int y, final int w, final int h);

    public abstract void setGraphBounds(final double x, final double y, final double w, final double h);

    public RenderedImage render() {
        if (renderCounter++ % 25 == 0) {
            final long start = System.nanoTime();
            execute(iw * ih);
            final float time = (System.nanoTime() - start) / (float) 1_000_000_000;
            System.out.println("Device Type: " + getExecutionMode());
            System.out.println("Time: " + time + " seconds");
        } else execute(iw * ih);

        return image;
    }

    // adapted from Color.HSBtoRGB() for Aparapi
    protected int hsb2rgb(final float hue, final float saturation, final float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = (int) (brightness * 255.0f + 0.5f);
            g = r;
            b = g;
        } else {
            final float h = (hue - floor(hue)) * 6.0f;
            final float f = h - floor(h);
            final float p = brightness * (1.0f - saturation);
            final float q = brightness * (1.0f - saturation * f);
            final float t = brightness * (1.0f - (saturation * (1.0f - f)));
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
}
