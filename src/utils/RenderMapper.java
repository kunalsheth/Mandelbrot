package utils;

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

public class RenderMapper {

    public final double glx, gty; // graph left x, graph top y
    public final double gw, gh; // graph width, graph height
    public final int iw, ih; // image width, image height

    public RenderMapper(final double glx,
                        final double gty,
                        final double gw,
                        final double gh,
                        final int iw,
                        final int ih) {
        this.glx = glx;
        this.gty = gty;
        this.gw = gw;
        this.gh = gh;
        this.iw = iw;
        this.ih = ih;
    }

    public double imageXToGraphA(final int x) {
        return glx + ((x / (double) iw) * gw);
    }

    public double imageYToGraphB(final int y) {
        return gty + ((y / (double) ih) * gh);
    }

    public int graphAToImageX(final double a) {
        return (int) Math.round(((a - glx) * iw) / gw);
    }

    public int graphBToImageY(final double b) {
        return (int) Math.round(((b - gty) * ih) / gh);
    }

    public String toString() {
        return "glx=" + glx +
                ",\ngty=" + gty +
                ",\ngw=" + gw +
                ",\ngh=" + gh +
                ",\niw=" + iw +
                ",\nih=" + ih;
    }
}
