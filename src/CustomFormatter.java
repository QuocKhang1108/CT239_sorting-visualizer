import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;

public class CustomFormatterTextField extends NumberFormatter {
    public CustomFormatter(NumberFormat format) {
        super(format);
    }

    public Object stringToValue(String text) throws ParseException {
        if ("".equals(text))
            return 0;
        return super.stringToValue(text);
    }
}
