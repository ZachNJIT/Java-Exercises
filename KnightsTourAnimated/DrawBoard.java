import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DrawBoard extends JPanel {

    public int[][] dboard;
    public int moveNumDraw = -1;

    public DrawBoard() {
        this.addMouseListener(mouseListener);
        Timer timer = new Timer(500, repainter);
        timer.start();
    }

    ActionListener repainter = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            moveNumDraw++;
            repaint();
        }
    };

    MouseListener mouseListener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
            moveNumDraw = 65;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            moveNumDraw = 65;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            moveNumDraw = 65;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    };

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D gd = (Graphics2D) g;
        super.paintComponent(gd);
        for (int i = 0; i < 8; i += 2) {
            for (int j = 0; j < 8; j += 2) {
                gd.setColor(Color.BLACK);
                gd.fillRect(100*i,100*j,100,100);
                gd.setColor(Color.WHITE);
                gd.setFont(new Font("Serif", Font.BOLD, 30));
                if (dboard[i][j] < moveNumDraw) {
                    gd.drawString(Integer.toString(dboard[i][j]), 35 + 100 * i, 65 + 100 * j);
                }
            }
        }
        for (int i = 1; i < 8; i += 2) {
            for (int j = 1; j < 8; j += 2) {
                gd.setColor(Color.BLACK);
                gd.fillRect(100*i,100*j,100,100);
                gd.setColor(Color.WHITE);
                gd.setFont(new Font("Serif", Font.BOLD, 30));
                if (dboard[i][j] < moveNumDraw) {
                    gd.drawString(Integer.toString(dboard[i][j]), 35 + 100 * i, 65 + 100 * j);
                }
            }
        }
        for(int i = 1; i < 8; i += 2) {
            for (int j = 0; j < 8; j+=2) {
                gd.setColor(Color.BLACK);
                gd.setFont(new Font("Serif", Font.BOLD, 30));
                if (dboard[i][j] < moveNumDraw) {
                    gd.drawString(Integer.toString(dboard[i][j]), 35 + 100 * i, 65 + 100 * j);
                }
            }
        }
        for (int i = 0; i < 8; i+= 2) {
            for (int j = 1; j < 8; j+=2) {
                gd.setColor(Color.BLACK);
                gd.setFont(new Font("Serif", Font.BOLD, 30));
                if (dboard[i][j] < moveNumDraw) {
                    gd.drawString(Integer.toString(dboard[i][j]), 35 + 100 * i, 65 + 100 * j);
                }
            }
        }
        gd.drawRect(0,0,800,800);
    }
}

