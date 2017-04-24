import com.aparapi.Kernel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by the-magical-llamicorn on 4/23/17.
 */
public class Zoom {

    protected static Image image;

    public static void main(final String[] args) throws InterruptedException {
        final JFrame frame = new JFrame("Mandelbrot Zoom");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        final double aspectRatio = frame.getWidth() / (double) frame.getHeight();

        int maxIterations;
        double px;
        double py;
        try {
            maxIterations = Integer.parseInt(args[0]);
            px = Double.parseDouble(args[1]);
            py = Double.parseDouble(args[2]);
        } catch (final NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("[max iterations][zoom coordinate real][zoom coordinate imaginary]");
            System.err.println("Using Default Values");
            maxIterations = 100;
            px = -0.636754346582389978;
            py = 0.685031297083677301;
        }

        double gw = 3;
        final Mandelbrot mandelbrot = new MandelbrotFP32(maxIterations, 2, 0, 0, 0, 0, frame.getWidth(), frame.getHeight());
        mandelbrot.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.GPU);

        final JPanel panel = new JPanel() {
            public void paintComponent(final Graphics g) {
                g.drawImage(image, 0, 0, null);
            }
        };
        frame.getContentPane().add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        while (true) {
            gw /= 1.002;
            mandelbrot.setGraphBounds((float) (px - (gw / 2)), (float) (py - ((gw / aspectRatio) / 2)), (float) gw, (float) (gw / aspectRatio));
            image = (Image) mandelbrot.render();
            panel.repaint();
        }
    }
}
