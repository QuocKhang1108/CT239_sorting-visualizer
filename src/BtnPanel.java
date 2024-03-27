import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BtnPanel extends JPanel {
    private static final int widthBtn = 200, heightBtn = 50;
    private SortButtonListener listener;
    private JButton[] buttons;
    private int number = 6;

    public BtnPanel(SortButtonListener listener) {
//        super();

//        this.listener = listener;

        buttons = new JButton[number];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();

//            design button
            buttons[i].setBackground(CustomColor.mainBackground);
            buttons[i].setBorder(null);
            buttons[i].setFocusPainted(false);
            buttons[i].setFont(new Font(null, Font.PLAIN, 20));
            buttons[i].setForeground(CustomColor.text);

            if (i == 0) buttons[i].setBounds(25, 0, widthBtn, heightBtn);
            else buttons[i].setBounds(25, 25 + (heightBtn + 20) * i, widthBtn, heightBtn);

            int button = i;
            buttons[i].addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    buttons[button].setBackground(CustomColor.btnPressed);
                    buttons[button].setFont(new Font(null, Font.BOLD, 20));
                    buttons[button].setForeground(CustomColor.text);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    listener.sortButtonClicked(button);

                    buttons[button].setBackground(CustomColor.btnPressed);
                    buttons[button].setFont(new Font(null, Font.BOLD, 20));
                    buttons[button].setForeground(CustomColor.textSelected);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (button == 0) buttons[button].setBounds(25, 0-5, widthBtn, heightBtn);
                    else buttons[button].setBounds(25, 25-5 + (heightBtn + 20) * button, widthBtn, heightBtn);

                    buttons[button].setBackground(CustomColor.btnEntered);
                    buttons[button].setFont(new Font(null, Font.PLAIN, 20));
                    buttons[button].setForeground(CustomColor.textSelected);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (button == 0) buttons[button].setBounds(25, 0, widthBtn, heightBtn);
                    else buttons[button].setBounds(25, 25 + (heightBtn + 20) * button, widthBtn, heightBtn);

                    buttons[button].setBackground(CustomColor.mainBackground);
                    buttons[button].setFont(new Font(null, Font.PLAIN, 20));
                    buttons[button].setForeground(CustomColor.text);
                }
            });
            add(buttons[i]);
        }

        setLayout(null);

        buttons[0].setText("Create array random");
        buttons[1].setText("Bubble Sort");
        buttons[2].setText("Selection Sort");
        buttons[3].setText("Insertion Sort");
        buttons[4].setText("Merge Sort");
        buttons[5].setText("Quick Sort");
    }

    public interface SortButtonListener {
        void sortButtonClicked(int button);
    }
}