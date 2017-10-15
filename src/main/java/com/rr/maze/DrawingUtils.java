package com.rr.maze;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public final class DrawingUtils {

    public static final Color BLACK = Color.BLACK;
    public static final Color BLUE = Color.BLUE;
    public static final Color GRAY = Color.GRAY;
    public static final Color RED = Color.RED;
    public static final Color WHITE = Color.WHITE;

    private static final int DEFAULT_SIZE = 512;
    private static int width = DEFAULT_SIZE;
    private static int height = DEFAULT_SIZE;

    private static boolean defer = false;

    private static double xmax, ymax;

    private static BufferedImage offscreenImage, onscreenImage;
    private static Graphics2D offscreen, onscreen;

    private static JFrame frame;

    private DrawingUtils() {
    }

    static {
        init();
    }

    private static void init() {
        if (frame != null) frame.setVisible(false);
        frame = new JFrame();
        offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        onscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        offscreen = offscreenImage.createGraphics();
        onscreen = onscreenImage.createGraphics();

        ImageIcon icon = new ImageIcon(onscreenImage);
        JLabel draw = new JLabel(icon);

        frame.setContentPane(draw);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("My maze");
        frame.pack();
        frame.requestFocusInWindow();
        frame.setVisible(true);
    }

    public static void setXscale(double max) { xmax = max; }

    public static void setYscale(double max) { ymax = max; }

    private static double scaleX(double x) { return width * (x) / (xmax); }

    private static double scaleY(double y) {
        return height * (ymax - y) / (ymax);
    }

    private static double factorX(double w) {
        return w * width / Math.abs(xmax);
    }

    private static double factorY(double h) {
        return h * height / Math.abs(ymax);
    }

    public static void setPenColor(Color color) { offscreen.setColor(color); }

    public static void line(double x0, double y0, double x1, double y1) {
        offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
        draw();
    }

    private static void pixel(double x, double y) {
        offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
    }

    public static void filledCircle(double x, double y, double radius) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * radius);
        double hs = factorY(2 * radius);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs));
        draw();
    }

    public static void pause(int t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

    public static void show() {
        onscreen.drawImage(offscreenImage, 0, 0, null);
        frame.repaint();
    }

    public static void draw() { if (!defer) show(); }

    public static void enableDefer() { defer = true; }

    public static void disableDefer() {
        defer = false;
    }
}