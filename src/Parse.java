public class Parse {
    public int parseHeight(int value) {
        return value * 6 + 6;
    }

    public int parseValue(int height) {
        return (height - 6) / 6;
    }
}
