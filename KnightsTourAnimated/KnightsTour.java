import java.util.Arrays;
import java.security.SecureRandom;
import javax.swing.*;

public class KnightsTour extends JFrame {

    public static JFrame frame = new JFrame();
    static int[][] board = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};

    int squaresCovered = 1;
    int x = 0;
    int y = 0;

    int[][] accessibility =
            {{2, 3, 4, 4, 4, 4, 3, 2},
                    {3, 4, 6, 6, 6, 6, 4, 3},
                    {4, 6, 8, 8, 8, 8, 6, 4},
                    {4, 6, 8, 8, 8, 8, 6, 4},
                    {4, 6, 8, 8, 8, 8, 6, 4},
                    {4, 6, 8, 8, 8, 8, 6, 4},
                    {3, 4, 6, 6, 6, 6, 4, 3},
                    {2, 3, 4, 4, 4, 4, 3, 2}};

    final int[][] moves =
            {{-1,2}, {2,-1}, {-2, -1}, {-1,-2}, {1, 2}, {2, 1}, {1, -2}, {-2, 1}};

    public static void main(String[] args) {
        frame.setSize(815,835);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        DrawBoard drawBoard = new DrawBoard();

        SecureRandom rng = new SecureRandom();
        int x_init = rng.nextInt(8);
        int y_init = rng.nextInt(8);
        KnightsTour k = new KnightsTour(x_init, y_init);
        k.play();

        drawBoard.dboard = board;
        frame.add(drawBoard);
        frame.setVisible(true);
        if (k.isClosedTour()) {
            JOptionPane.showMessageDialog(frame, "             This will be a closed tour!\n     The knight will begin at square (" + (x_init+1) + ", " + (y_init+1) + ")\n(Click board anywhere to skip animation)");
        } else {
            JOptionPane.showMessageDialog(frame,"             This will be an open tour!\n     The knight will begin at square (" + (x_init+1) + ", " + (y_init+1) + ")\n(Click board anywhere to skip animation)");
        }

        System.out.println("Squares visited = " + k.squaresCovered);
        for (int i = 0; i < k.board.length; i++) {
            System.out.println(Arrays.toString(k.board[i]));
        }

    }

    public KnightsTour(int X, int Y) {
        x = X;
        y = Y;
    }

    public void play() {
        while (squaresCovered < 64) {
            board[x][y] = squaresCovered;
            int move = chooseMove();
            if (move < 0) {
                return;
            }
            x += moves[move][0];
            y += moves[move][1];
            squaresCovered ++;
        }
        board[x][y] = squaresCovered;
    }


    public int chooseMove(){
        int ans = -1;
        int ansScore = 100;

        for (int i = 0; i < moves.length; i++) {
            int score = moveScore(x, y, i);
            if(score>0 && score<ansScore) {
                ans = i;
                ansScore = score;
            }
            if(score>0 && score==ansScore) {
                int score1 = hypChooseMove(x + moves[ans][0], y + moves[ans][1]);
                int score2 = hypChooseMove(x + moves[i][0], y + moves[i][1]);
                if (score1 > score2) {
                    ans = i;
                    ansScore = score;
                }
            }
            int test = moveScoreMinus(i);
        }

        return ans;
    }

    public int moveScore(int x, int y, int moveNum) {
        int xTar = x + moves[moveNum][0];
        int yTar = y + moves[moveNum][1];
        if (xTar < 0 || xTar>= board.length || yTar < 0 || yTar>= board[xTar].length) {
            return -1;
        }
        if(board[xTar][yTar] > 0) {
            return -1;
        }

        return accessibility[xTar][yTar];
    }

    public int moveScoreMinus(int moveNum) {
        int xTar = x + moves[moveNum][0];
        int yTar = y + moves[moveNum][1];
        if (xTar < 0 || xTar>= board.length || yTar < 0 || yTar>= board[xTar].length) {
            return -1;
        }
        if(board[xTar][yTar] > 0) {
            return -1;
        }

        return accessibility[xTar][yTar]--;
    }

    public int hypChooseMove(int x, int y) {
        int ansScore = 100;

        for (int i = 0; i < moves.length; i++) {
            int score = moveScore(x, y, i);
            if (score > 0 && score < ansScore) {
                ansScore = score;
            }
        }

        return ansScore;
    }

    public boolean isClosedTour() {
        for (int i = 0; i < moves.length; i++) {
            int xTar = x + moves[i][0];
            int yTar = y + moves[i][1];
            if (xTar < 0 || xTar>= board.length || yTar < 0 || yTar>= board[xTar].length) {
                continue;
            } else if (board[x + moves[i][0]][y + moves[i][1]]==1) {
                return true;
            }
        }
        return false;

    }
}


