// Display.java
// Program writes the value of q JSlider on the screen
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class DisplayJSlider extends JFrame {
    private DisplayJSlider canvas;
    private JTextField display;
    private JLabel label;
    private JSlider slider;
    private JPanel panel;

    public DisplayJSlider() {
        super("DisplayJSlider");
        label = new JLabel("Value:");

        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2) );

        DisplayCanvas canvas = new DisplayCanvas();
        slider = new JSlider(SwingConstants.HORIZONTAL,0,999,10);
        slider.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        canvas.setValue(slider.getValue());
                        display.setText(String.valueOf(slider.getValue()));
                    }
                }
        );
        slider.setMajorTickSpacing(100);
        slider.setPaintTicks(true);

        display = new JTextField("0",5 );
        display.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int v = Integer.parseInt(display.getText());
                        //the following checks user input to make sure it is within proper range:
                        if ( v < slider.getMinimum() || v > slider.getMaximum() )
                            return;

                        canvas.setValue(v);
                        slider.setValue(v);
                    }
                }
        );
        panel.add(label);
        panel.add(display);

        getContentPane().add(canvas, BorderLayout.CENTER);
        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(slider, BorderLayout.SOUTH);
        setSize(400,400 );
        setVisible(true);
    }

    public static void main(String[] args) {
        DisplayJSlider dJSlider = new DisplayJSlider();
        dJSlider.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class DisplayCanvas extends JPanel {
    private int number;
    private Font font;

    public DisplayCanvas() {
        setBackground(Color.black);
        setSize(150, 150);
        font = new Font("Serif", Font.BOLD, 120);
    }

    public void setValue(int n) {
        number = n;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(font);
        g.setColor(Color.blue);
        if (number < 10) {
            g.drawString(String.valueOf(number), getSize().width / 2 - 30 , getSize().height / 2 + 35);
        } else if (number < 100) {
            g.drawString(String.valueOf(number), getSize().width / 2 - 55, getSize().height / 2 + 35);
        } else {
            g.drawString(String.valueOf(number), getSize().width / 2 - 85, getSize().height / 2 + 35);
        }
    }

    public int getNumber()  {return number;}
}