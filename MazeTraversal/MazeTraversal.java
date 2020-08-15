// Ex 18.20 MazeTraversal

import javax.swing.*;

public final class MazeTraversal extends JFrame {
    public static JFrame frame = new JFrame();

    private final char[][] maze;

    private static final char WALL = '#';
    private static final char DEAD = '0';
    private static final char OPEN = '.';
    private static final char PATH = 'X';

    private static final int WAIT_TIME = 250;

    private static final int FRAME_HEIGHT = 800;
    private static final int FRAME_WIDTH = 800;
    private final int HEIGHT;
    private final int WIDTH;

    private static int beginR;
    private static int beginC;
    private int endR;
    private int endC;

    private boolean inMaze (int row, int column) {
        return row >= 0 && row < HEIGHT && column >= 0 && column < WIDTH;
    }

    private boolean validMove (int row, int column) {
        return inMaze(row, column) && maze[row][column] == OPEN;
    }

    private boolean mazeDone (int row, int column) {
        return row == endR && column == endC;
    }

    private int[] findBeginEnd(char[][] mazeC) {
        int beginRT = 0;
        int beginCT = 0;
        int endRT = 0;
        int endCT = 0;
        int numfound = 0;
        int[] locations = new int[4];
        // look at top row
        for(int column = 0; column < WIDTH; column++) {
            if (mazeC[0][column] == OPEN && numfound == 0) {
                beginRT = 0;
                beginCT = column;
                numfound++;
            } else if (mazeC[0][column] == OPEN && numfound == 1) {
                endRT = 0;
                endCT = column;
                numfound++;
            }
        }
        // look at bottom row
        for(int column = 0; column < WIDTH; column++) {
            if (mazeC[HEIGHT - 1][column] == OPEN && numfound == 0) {
                beginRT = HEIGHT - 1;
                beginCT = column;
                numfound++;
            } else if (mazeC[0][column] == OPEN && numfound == 1) {
                endRT = 0;
                endCT = column;
                numfound++;
            }
        }
        // look at left edge
        for(int row = 0; row < HEIGHT; row++) {
            if (mazeC[row][0] == OPEN && numfound == 0) {
                beginRT = row;
                beginCT = 0;
                numfound++;
            } else if (mazeC[row][0] == OPEN && numfound == 1) {
                endRT = row;
                endCT = 0;
                numfound++;
            }
        }
        // look at right edge
        for(int row = 0; row < HEIGHT; row++) {
            if (mazeC[row][WIDTH - 1] == OPEN && numfound == 0) {
                beginRT = row;
                beginCT = WIDTH - 1;
                numfound++;
            } else if (mazeC[row][WIDTH - 1] == OPEN && numfound == 1) {
                endRT = row;
                endCT = WIDTH - 1;
                numfound++;
            }
        }
        locations[0] = beginRT;
        locations[1] = beginCT;
        locations[2] = endRT;
        locations[3] = endCT;
        return locations;
    }

    public MazeTraversal(String[] mazeStr) {
        this.WIDTH = mazeStr[0].length();
        this.HEIGHT = mazeStr.length;

        maze = new char[HEIGHT][WIDTH];
        for (int r = 0; r < HEIGHT; r++) {
            for (int c = 0; c < WIDTH; c++) {
                maze[r][c] = mazeStr[r].charAt(c);
            }
        }

        int[] locations = findBeginEnd(maze);

        this.beginR = locations[0];
        this.beginC = locations[1];
        this.endR = locations[2];
        this.endC = locations[3];
    }

    public static void wait(int ms){
        try {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean mazeTraversal(int row, int column, DrawMaze dMaze) {
        if (!validMove(row,column))
            return false;

        dMaze.setMaze(maze);
        dMaze.repaint();
        wait(WAIT_TIME);
        maze[row][column] = PATH;


        if (mazeDone(row,column))
            return true;

        if (mazeTraversal(row - 1, column, dMaze)) // up
            return true;
        if (mazeTraversal(row, column + 1, dMaze)) // right
            return true;
        if (mazeTraversal(row + 1, column, dMaze)) // down
            return true;
        if (mazeTraversal(row, column - 1, dMaze)) // left
            return true;

        maze[row][column] = DEAD;
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (int row = 0; row < HEIGHT; row++) {
            for (int column = 0; column < WIDTH; column++)
                result.append(maze[row][column] + (column == WIDTH - 1 ? "" : "  "));
            result.append(row == HEIGHT - 1 ? "" : "\n");
        }
        return result.toString();
    }

    public static void main(String[] args){
        frame.setSize(FRAME_HEIGHT,FRAME_HEIGHT);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final String[] mazeStr = {
                "############",
                "#...#......#",
                "..#.#.####.#",
                "###.#....#.#",
                "#....###.#..",
                "####.#.#.#.#",
                "#..#.#.#.#.#",
                "##.#.#.#.#.#",
                "#........#.#",
                "######.###.#",
                "#......#...#",
                "############"};

        MazeTraversal m = new MazeTraversal(mazeStr);
        DrawMaze drawMaze = new DrawMaze(m.maze, FRAME_HEIGHT, FRAME_WIDTH);
        frame.add(drawMaze);
        frame.setVisible(true);
        System.out.println("Maze: \n" + m);
        System.out.println("\nSolved: "+ m.mazeTraversal(beginR, beginC, drawMaze));
        drawMaze.repaint();
        System.out.println(m);
    }
}