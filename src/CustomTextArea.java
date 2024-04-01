import javax.swing.*;
import java.awt.*;

public class CustomTextArea extends JTextArea {
    public CustomTextArea() {
        super();
        setLineWrap(true);
        setWrapStyleWord(true);
        setFont(new Font(null, Font.ITALIC, 15));
        setForeground(CustomColor.text);
        setBackground(CustomColor.mainBackground);
    }


}
