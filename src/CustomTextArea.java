import javax.swing.*;
import java.awt.*;

public class CustomTextArea extends JTextArea {
    public CustomTextArea() {
        super();

        setFont(new Font(null, Font.ITALIC, 15));
        setForeground(CustomColor.textInput);
        setLineWrap(true);
        setBackground(CustomColor.boxInput);
        setBorder(null);
        setWrapStyleWord(true);
        setCaretColor(CustomColor.text);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getText().isEmpty() && ! (FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setFont(new Font(null, Font.ITALIC, 15));
            g2.setColor(CustomColor.textHint);
            g2.drawString("Enter the array to sort. Ex: 1,0,..", 5, 15); // draws the hint text
            g2.dispose();
        }
    }
}
