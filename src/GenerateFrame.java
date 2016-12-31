
import mandelbrot.MandelbrotRenderer;
import utils.RenderMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

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

public class GenerateFrame {

    public static void main(final String[] args) {
        if (args.length < 7) {
            System.err.println("[left x coordinate of graph][top y coordinate of graph][graph width][graph height][image width][image height]");
            System.err.println();
            System.err.println("Some good parameters to start of with are:");
            System.err.println("-2 -1.25 3.5 2.5 2560 1828 mandelbrot.png");
            System.exit(1);
        }

        final double graphLeftX = Double.parseDouble(args[0]),
                graphTopY = Double.parseDouble(args[1]),
                graphWidth = Double.parseDouble(args[2]),
                graphHeight = Double.parseDouble(args[3]);
        final int imageWidth = Integer.parseInt(args[4]),
                imageHeight = Integer.parseInt(args[5]);

        final RenderMapper mapper = new RenderMapper(
                graphLeftX, graphTopY,
                graphWidth, graphHeight,
                imageWidth, imageHeight
        );
        System.out.println(mapper);

        final BufferedImage image = new BufferedImage(
                imageWidth,
                imageHeight,
                BufferedImage.TYPE_INT_RGB
        );

        IntStream.range(0, image.getWidth()).forEach(
                x -> IntStream.range(0, image.getHeight()).parallel().forEach(
                        y -> image.setRGB(x, y,
                                MandelbrotRenderer.apply(mapper.imageXToGraphA(x), mapper.imageYToGraphB(y))
                        )
                )
        );

        try {
            ImageIO.write(image, "PNG", new File(args[6]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
