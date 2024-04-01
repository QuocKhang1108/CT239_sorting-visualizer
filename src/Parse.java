public class Parse {
    public static int parseHeight(int value) {
        return value * 6 + 6;
    }

    public static int parseValue(int height) {
        return (height - 6) / 6;
    }
}
