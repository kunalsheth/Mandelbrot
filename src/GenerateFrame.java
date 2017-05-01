import com.aparapi.Kernel;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by the-magical-llamicorn on 4/22/17.
 */
public class GenerateFrame {

    public static void main(final String[] args) {
        if (args.length < 7) {
            System.err.println("[left x coordinate of graph][top y coordinate of graph][graph width][graph height][image width][image height]");
            System.err.println();
            System.err.println("Some good parameters to start of with are:");
            System.err.println("-2 -1.25 3.5 2.5 2560 1828 mandelbrot.png");
            System.exit(1);
        }

        final float graphLeftX = Float.parseFloat(args[0]),
                graphTopY = Float.parseFloat(args[1]),
                graphWidth = Float.parseFloat(args[2]),
                graphHeight = Float.parseFloat(args[3]);
        final int imageWidth = Integer.parseInt(args[4]),
                imageHeight = Integer.parseInt(args[5]);

        final Mandelbrot mandelbrot = new MandelbrotFP32(
                100, 2,
                graphLeftX, graphTopY,
                graphWidth, graphHeight,
                imageWidth, imageHeight
        );

        mandelbrot.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.GPU);

        final RenderedImage image = mandelbrot.render();
        try {
            ImageIO.write(image, "PNG", new File(args[6]));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
