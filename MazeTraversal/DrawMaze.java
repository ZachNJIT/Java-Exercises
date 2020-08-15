// 18.20 DrawMaze.java
// Helps display maze

import javax.swing.*;
import java.awt.*;

public class DrawMaze extends JPanel {

    private static final char WALL = '#';

    private int FRAME_HEIGHT;
    private int FRAME_WIDTH;
    private int dHEIGHT;
    private int dWIDTH;
    private int sWIDTH;
    private int sHEIGHT;
    private char[][] dmaze;

    public DrawMaze(char[][] maze, int frameHeight, int frameWidth) {
        this.dmaze = maze;
        this.FRAME_HEIGHT = frameHeight;
        this.FRAME_WIDTH = frameWidth;
        this.dWIDTH = maze[0].length;
        this.dHEIGHT = maze.length;
        sWIDTH = FRAME_WIDTH / dWIDTH;
        sHEIGHT = FRAME_HEIGHT / dHEIGHT;
    }

    public void setMaze(char[][] mazeS) {
        this.dmaze = mazeS;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < dHEIGHT; i ++) {
            for (int j = 0; j < dWIDTH; j++) {
                if (dmaze[j][i] == WALL) {
                    g.setColor(Color.BLACK);
                    g.fillRect(sHEIGHT * i, sWIDTH * j, sHEIGHT, sWIDTH);
                }
                g.setColor(Color.BLACK);
                g.setFont(new Font("Serif", Font.BOLD, ((int) ((float) sWIDTH/2.3))));
                g.drawString(Character.toString(dmaze[j][i]), (sHEIGHT/3) + sHEIGHT * i, ((int) ((float) sWIDTH/1.5)) + sWIDTH * j);
            }
        }
    }
}

