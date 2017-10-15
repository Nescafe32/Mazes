package com.rr.maze;

import java.util.Scanner;

public class TheMaze {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] mazeSize, startCoordinates, finishCoordinates;
        int rows, cols, yStart, xStart, yEnd, xEnd;
        boolean showGenerationProcess;

        System.out.println("Type maze size (rows and cols) separated by space");
        mazeSize = scanner.nextLine().split("\\s");
        rows = Integer.valueOf(mazeSize[0]);
        cols = Integer.valueOf(mazeSize[1]);

        System.out.println("Type coordinates of the start point");
        startCoordinates = scanner.nextLine().split("\\s");
        yStart = Integer.valueOf(startCoordinates[0]);
        xStart = Integer.valueOf(startCoordinates[1]);

        System.out.println("Type coordinates of the finish point");
        finishCoordinates = scanner.nextLine().split("\\s");
        yEnd = Integer.valueOf(finishCoordinates[0]);
        xEnd = Integer.valueOf(finishCoordinates[1]);

        System.out.println("Show maze generation process? (true|false)");
        showGenerationProcess = Boolean.valueOf(scanner.nextLine());

        System.out.println("Show maze solving process and surplus path? (true|false \"space\" true|false)");
        String[] s = scanner.nextLine().split("\\s");

        if (!showGenerationProcess)
            DrawingUtils.enableDefer();
        else
            DrawingUtils.disableDefer();

        Maze maze = new Maze(rows, cols, yStart, xStart, yEnd, xEnd, Boolean.valueOf(s[0]), Boolean.valueOf(s[1]));
        maze.draw();
        maze.solve();
        DrawingUtils.show();
        maze.printStack();
    }
}