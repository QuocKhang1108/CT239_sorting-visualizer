import java.awt.*;

public class Column {
    private final int marginColumn = 1;
    private int x, y, width, height;
    private Color color;

    public Column(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect(x + marginColumn, y - height, width - marginColumn * 2, height);
    }

    //    clear column when its swap
    public void clear(Graphics graphics) {
        graphics.setColor(CustomColor.canvasBackground);
        graphics.fillRect(x + marginColumn, y - height, width - marginColumn * 2, height);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
