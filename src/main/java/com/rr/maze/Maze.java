package com.rr.maze;

import java.awt.Color;
import java.util.Random;
import java.util.Stack;

public class Maze {
    private int rows;
    private int cols;
    private int yStart;
    private int xStart;
    private int yEnd;
    private int xEnd;
    private boolean[][] north;
    private boolean[][] east;
    private boolean[][] south;
    private boolean[][] west;
    private boolean[][] visited;
    private boolean showSolvingProcess;
    private boolean done = false;
    private Random random;
    private Color colorOfSurplusPath;

    private Stack<StackArrayWrapper> path;

    public Maze(int rows, int cols, int yStart, int xStart, int yEnd, int xEnd, boolean showSolvingProcess, boolean showAllMoves) {
        path = new Stack<>();
        this.rows = rows;
        this.cols = cols;
        this.yStart = yStart;
        this.xStart = xStart;
        this.yEnd = yEnd;
        this.xEnd = xEnd;
        this.showSolvingProcess = showSolvingProcess;
        this.random = new Random(System.currentTimeMillis());
        if (showAllMoves)
            colorOfSurplusPath = DrawingUtils.GRAY;
        else
            colorOfSurplusPath = DrawingUtils.WHITE;
        DrawingUtils.setYscale(rows + 2);
        DrawingUtils.setXscale(cols + 2);
        init();
        generate();
    }

    private void init() {
        visited = new boolean[rows + 2][cols + 2];
        for (int y = 0; y < rows + 2; y++) {
            visited[y][0] = true;
            visited[y][cols + 1] = true;
        }
        for (int x = 0; x < cols + 2; x++) {
            visited[0][x] = true;
            visited[rows + 1][x] = true;
        }

        north = new boolean[rows + 2][cols + 2];
        east = new boolean[rows + 2][cols + 2];
        south = new boolean[rows + 2][cols + 2];
        west = new boolean[rows + 2][cols + 2];
        for (int y = 0; y < rows + 2; y++) {
            for (int x = 0; x < cols + 2; x++) {
                north[y][x] = true;
                east[y][x] = true;
                south[y][x] = true;
                west[y][x] = true;
            }
        }
    }

    private void generate(int y, int x) {
        visited[y][x] = true;
        while (!visited[y + 1][x] || !visited[y][x + 1] || !visited[y - 1][x] || !visited[y][x - 1]) {
            while (true) {
                double r = random.nextInt(4);
                if (r == 0 && !visited[y + 1][x]) {
                    north[y][x] = false;
                    south[y + 1][x] = false;
                    generate(y + 1, x);
                    break;
                } else if (r == 1 && !visited[y][x + 1]) {
                    east[y][x] = false;
                    west[y][x + 1] = false;
                    generate(y, x + 1);
                    break;
                } else if (r == 2 && !visited[y - 1][x]) {
                    south[y][x] = false;
                    north[y - 1][x] = false;
                    generate(y - 1, x);
                    break;
                } else if (r == 3 && !visited[y][x - 1]) {
                    west[y][x] = false;
                    east[y][x - 1] = false;
                    generate(y, x - 1);
                    break;
                }
            }
        }
    }

    private void generate() {
        generate(1, 1);
    }

    private void solve(int y, int x) {
        if (y == 0 || x == 0 || y == rows + 1 || x == cols + 1) return;
        if (done || visited[y][x]) return;
        visited[y][x] = true;

        DrawingUtils.setPenColor(DrawingUtils.BLUE);
        DrawingUtils.filledCircle(x + 0.5, y + 0.5, 0.25);

        if (!showSolvingProcess)
            DrawingUtils.enableDefer();
        else
            DrawingUtils.disableDefer();

        DrawingUtils.draw();
        DrawingUtils.pause(30);

        if (y == yEnd && x == xEnd) {
            done = true;
        }

        if (!done && !north[y][x]) {
            if (!path.contains(new StackArrayWrapper(new Integer[]{y + 1, x})))
                path.push(new StackArrayWrapper(new Integer[]{y + 1, x}));
            solve(y + 1, x);
        }
        if (!done && !east[y][x]) {
            if (!path.contains(new StackArrayWrapper(new Integer[]{y, x + 1})))
                path.push(new StackArrayWrapper(new Integer[]{y, x + 1}));
            solve(y, x + 1);
        }
        if (!done && !south[y][x]) {
            if (!path.contains(new StackArrayWrapper(new Integer[]{y - 1, x})))
                path.push(new StackArrayWrapper(new Integer[]{y - 1, x}));
            solve(y - 1, x);
        }
        if (!done && !west[y][x]) {
            if (!path.contains(new StackArrayWrapper(new Integer[]{y, x - 1})))
                path.push(new StackArrayWrapper(new Integer[]{y, x - 1}));
            solve(y, x - 1);
        }

        if (done) return;

        path.pop();
        DrawingUtils.setPenColor(colorOfSurplusPath);
        DrawingUtils.filledCircle(x + 0.5, y + 0.5, 0.25);
        if (showSolvingProcess)
            DrawingUtils.show();
        DrawingUtils.pause(30);
    }

    public void solve() {
        for (int y = 1; y <= rows; y++)
            for (int x = 1; x <= cols; x++)
                visited[y][x] = false;
        done = false;
        path.push(new StackArrayWrapper(new Integer[]{yStart, xStart}));
        solve(yStart, xStart);
    }

    public void draw() {
        DrawingUtils.setPenColor(DrawingUtils.RED);
        DrawingUtils.filledCircle(xStart + 0.5, yStart + 0.5, 0.375);
        DrawingUtils.filledCircle(xEnd + 0.5, yEnd + 0.5, 0.375);

        DrawingUtils.setPenColor(DrawingUtils.BLACK);

        for (int y = 0; y < rows; y++) {
            for (int x = 1; x <= cols; x++) {
                if (south[rows - y][x]) DrawingUtils.line(x, rows - y, x + 1, rows - y);
                if (north[rows - y][x]) DrawingUtils.line(x, rows - y + 1, x + 1, rows - y + 1);
                if (west[rows - y][x]) DrawingUtils.line(x, rows - y, x, rows - y + 1);
                if (east[rows - y][x]) DrawingUtils.line(x + 1, rows - y, x + 1, rows - y + 1);
            }
        }
        DrawingUtils.show();
        DrawingUtils.pause(1000);
    }

    public void printStack() {
        int batchPrintSize = 10;
        int i = -1;
        System.out.println("\nPath size: " + path.size());
        for (StackArrayWrapper obj : path) {
            ++i;
            if (obj.equals(path.elementAt(path.size() - 1)))
                System.out.print(obj);
            else
                System.out.print(obj + " -> ");
            if ((i + 1) % batchPrintSize == 0 && i != 0) {
                System.out.println();
            }
        }
    }
}