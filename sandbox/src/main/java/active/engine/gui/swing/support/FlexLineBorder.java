package active.engine.gui.swing.support;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.*;
import java.util.EnumMap;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class FlexLineBorder extends AbstractBorder {

    /*      +--> (x)
     *      |
     *      v (y)
     */
    private static class SideBorder {
        int thickness;
        Color lineColor;

        public SideBorder(){
            this(new Color(0,0,0,0), 1);
        }

        public SideBorder(Color lineColor, int thickness) {
            this.lineColor = lineColor;
            this.thickness = thickness;
        }
    }

    public enum Side {
        LEFT {
            @Override
            Path2D toSideShape(int x, int y, int width, int height, int thickness) {
                int t = thickness-1;
                int h = height-1;
                return pathOf(x, y,
                        x, y+h,
                        x+t, y+h-t,
                        x+t, y+t);
            }
        }, TOP{
            @Override
            Path2D toSideShape(int x, int y, int width, int height, int thickness) {
                int t = thickness-1;
                int w = width-1;
                return pathOf(x, y,
                        x+w, y,
                        x+w-t, y+t,
                        x+t, y+t);
            }
        }, RIGHT{
            @Override
            Path2D toSideShape(int x, int y, int width, int height, int thickness) {
                int t = thickness-1;
                int h = height-1;
                int w = width-1;
                return pathOf(x+w, y,
                        x+w, y+h,
                        x+w-t, y+h-t,
                        x+w-t, y+t);
            }
        }, BOTTOM{
            @Override
            Path2D toSideShape(int x, int y, int width, int height, int thickness) {
                int t = thickness-1;
                int h = height-1;
                int w = width-1;
                return pathOf(x+w, y+h,
                        x, y+h,
                        x+t, y+h-t,
                        x+w-t, y+h-t);
            }
        };

        abstract Path2D toSideShape(int x, int y, int width, int height, int thickness);

        protected Path2D pathOf(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4){
            Path2D.Float path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
            path.append(new Line2D.Float(x1, y1, x2, y2), true);
            path.append(new Line2D.Float(x2, y2, x3, y3), true);
            path.append(new Line2D.Float(x3, y3, x4, y4), true);
            path.append(new Line2D.Float(x4, y4, x1, y1), true);
//            path.append(new Line2D.Float(x3, y3, x1, y1), false);
//            path.append(new Line2D.Float(x2, y2, x4, y4), false);
            return path;
        }
    }

    private EnumMap<Side, SideBorder> sides = new EnumMap<>(Side.class);

    private SideBorder getBorder(Side side){
        return sides.computeIfAbsent(side, s -> new SideBorder());
    }

    private FlexLineBorder(SideBorder left, SideBorder top, SideBorder right, SideBorder bottom) {
        sides.put(Side.LEFT, left);
        sides.put(Side.TOP, top);
        sides.put(Side.RIGHT, right);
        sides.put(Side.BOTTOM, bottom);
    }
    private FlexLineBorder(Side side, SideBorder border){
        sides.put(side, border);
    }

    public static FlexLineBorder of(Color color){
        return of(color, 1);
    }
    public static FlexLineBorder of(Color color, int thickness){
        return of(color, color, color, color, thickness);
    }
    public static FlexLineBorder of(Color leftRight, Color topBottom){
        return of(leftRight, topBottom, leftRight, topBottom, 1);

    }
    public static FlexLineBorder of(Color leftColor, Color topColor, Color rightColor, Color bottomColor, int thickness){
        return of(leftColor, thickness, topColor, thickness, rightColor, thickness, bottomColor, thickness);
    }
    public static FlexLineBorder of(Color color, int leftThickness, int topThickness, int rightThickness, int bottomThickness){
        return of(color, leftThickness, color, topThickness, color, rightThickness, color, bottomThickness);
    }
    public static FlexLineBorder of(Color leftColor, int leftThickness, Color topColor, int topThickness,
                                    Color rightColor, int rightThickness, Color bottomColor, int bottomThickness){
        return new FlexLineBorder(
                new SideBorder(leftColor, leftThickness),
                new SideBorder(topColor, topThickness),
                new SideBorder(rightColor, rightThickness),
                new SideBorder(bottomColor, bottomThickness)
        );
    }
    public static FlexLineBorder left(Color color, int thickness){
        return new FlexLineBorder(Side.LEFT, new SideBorder(color, thickness));
    }
    public static FlexLineBorder top(Color color, int thickness){
        return new FlexLineBorder(Side.TOP, new SideBorder(color, thickness));
    }
    public static FlexLineBorder right(Color color, int thickness){
        return new FlexLineBorder(Side.RIGHT, new SideBorder(color, thickness));
    }
    public static FlexLineBorder bottom(Color color, int thickness){
        return new FlexLineBorder(Side.BOTTOM, new SideBorder(color, thickness));
    }


    public void setColor(Color left, Color top, Color right, Color bottom){
        getBorder(Side.LEFT).lineColor = left;
        getBorder(Side.TOP).lineColor = top;
        getBorder(Side.RIGHT).lineColor = right;
        getBorder(Side.BOTTOM).lineColor = bottom;
    }

    public void setThickness(int left, int top, int right, int bottom){
        getBorder(Side.LEFT).thickness = left;
        getBorder(Side.TOP).thickness = top;
        getBorder(Side.RIGHT).thickness = right;
        getBorder(Side.BOTTOM).thickness = bottom;
    }

    private int getThickness(Side side){
        SideBorder border = this.sides.get(side);
        return (border == null)? 0 : border.thickness;
    }

    /**
     * Paints the border for the specified component with the
     * specified position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (g instanceof Graphics2D){
            Graphics2D g2d = (Graphics2D) g;
            paintBorder(g2d, Side.LEFT, x, y, width, height);
            paintBorder(g2d, Side.TOP, x, y, width, height);
            paintBorder(g2d, Side.RIGHT, x, y, width, height);
            paintBorder(g2d, Side.BOTTOM, x, y, width, height);
        }
    }
    public void paintBorder(Graphics2D g2d, Side side, int x, int y, int width, int height) {
        SideBorder border = this.sides.get(side);
        if (border == null){
            return;
        }
        if ((border.thickness > 0)) {


            Color oldColor = g2d.getColor();
            g2d.setColor(border.lineColor);

            Shape outer;
            Shape inner;

            int offs = border.thickness;
            int size = offs + offs;
            Path2D path = side.toSideShape(x, y, width, height, border.thickness);
            g2d.fill(path);
//            System.out.println("Drawing " + path);
            g2d.draw(path);
            g2d.setColor(oldColor);
        }
    }

    /**
     * Reinitialize the insets parameter with this Border's current Insets.
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(getThickness(Side.TOP), getThickness(Side.LEFT), getThickness(Side.BOTTOM), getThickness(Side.RIGHT));
        return insets;
    }

    /**
     * Returns whether or not the border is opaque.
     */
    public boolean isBorderOpaque() {
        return true;
    }


}
