package br.odb.gameutils;

import java.io.Serializable;

public class Color implements Serializable {

    public int a;
    private int r;
    private int g;
    private int b;

    /**
     * @param argb a 32-bit integer with a ARGB colour encoded.
     */
    public Color(int argb) {
        set((argb & 0x00FF0000) >> 16, (argb & 0x0000FF00) >> 8, ((argb & 0x000000FF)), argb >> 24 & 0xFF);
    }

    public Color(int r, int g, int b, int a) {
        set(r, g, b, a);
    }

    public Color() {
        this(0, 0, 0, 255);
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(Color c) {
        this(c.r, c.g, c.b, c.a);
    }

    public Color(float r, float g, float b) {
        set((int) (r * 256), (int) (g * 256), (int) (b * 256), 255);
    }

    public Color(float r, float g, float b, float a) {
        set((int) (r * 256), (int) (g * 256), (int) (b * 256), (int) (a * 256));
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        String hex;

        hex = Integer.toString(r);
        sb.append(hex);
        sb.append(" ");

        hex = Integer.toString(g);
        sb.append(hex);
        sb.append(" ");

        hex = Integer.toString(b);
        sb.append(hex);
        sb.append(" ");

        hex = Integer.toString(a);
        sb.append(hex);

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + a;
        result = prime * result + b;
        result = prime * result + g;
        result = prime * result + r;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Color)) {
            return false;
        }
        Color other = (Color) obj;
        if (a != other.a) {
            return false;
        }
        if (b != other.b) {
            return false;
        }
        if (g != other.g) {
            return false;
        }

        return r == other.r;
    }

    public int getARGBColor() {

        int color;
        color = (a & 0xFF) << 24;
        color += (r & 0xFF) << 16;
        color += (g & 0xFF) << 8;
        color += b;

        return color;
    }

    private void set(int r, int g, int b, int a) {
        this.a = Utils.clamp(a, 0, 255);
        this.r = Utils.clamp(r, 0, 255);
        this.g = Utils.clamp(g, 0, 255);
        this.b = Utils.clamp(b, 0, 255);
    }

    public void set(int r, int g, int b) {
        set(r, g, b, 255);
    }

    public String getHTMLColor() {
        // 0x100 makes sure we have a 3 digit number, promptly culled with the
        // substring(1).
        String rHex = Integer.toString((r & 0xff) + 0x100, 16).substring(1);
        String gHex = Integer.toString((g & 0xff) + 0x100, 16).substring(1);
        String bHex = Integer.toString((b & 0xff) + 0x100, 16).substring(1);
        return "#" + rHex + gHex + bHex;
    }

    public void set(Color color) {

        if (color == null) {
            return;
        }

        set(color.r, color.g, color.b, color.a);
    }
}
